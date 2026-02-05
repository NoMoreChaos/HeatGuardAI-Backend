package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeFileUploadResponse;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import edu.aivle.heatguard_ai_back.repository.NoticeFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class NoticeFileService {

    private final NoticeFileRepository noticeFileRepository;
    private final S3NoticeFileService s3NoticeFileService;

    @Value("${app.s3.base-prefix:notice}")
    private String basePrefix;

    @Value("${app.notice-file.max-size-bytes:10485760}")
    private long maxSizeBytes;

    @Value("${app.notice-file.allowed-ext:pdf,png,jpg,jpeg,hwp,doc,docx,xls,xlsx,ppt,pptx,txt}")
    private String allowedExtCsv;

    /**
     * [POST] /api/notice/file/upload
     * - 파일 1개 업로드
     * - 응답: notice_file_cd만
     * - noticeCd는 게시글 작성 시 연결할 거라면 지금은 null 허용
     */
    public NoticeFileUploadResponse uploadOne(MultipartFile file) {
        validateFile(file);

        try {
            String originalName = safeOriginalName(file.getOriginalFilename());
            String saveNm = uuidFileNameKeepExt(originalName); // UUID + 확장자
            String savePath = buildSavePath();                 // 예: notice/2026/02
            String key = savePath + "/" + saveNm;              // ✅ S3 KEY (URL 아님)

            String contentType = (file.getContentType() != null)
                    ? file.getContentType()
                    : MediaType.APPLICATION_OCTET_STREAM_VALUE;

            // 1) S3 업로드
            s3NoticeFileService.upload(file.getBytes(), key, contentType);

            // 2) DB 저장 (중요: SAVE_PATH에는 URL 금지. prefix만 저장)
            NoticeFileEntity e = new NoticeFileEntity();
            e.setNoticeFileNm(originalName);
            e.setNoticeFileSaveNm(saveNm);
            e.setNoticeFileSavePath(savePath);
            e.setNoticeFileType(contentType);     // 너 DB에 MIME 들어가길래 그대로
            e.setNoticeFileSize(file.getSize());
            // e.setNoticeCd(null);

            NoticeFileEntity saved = noticeFileRepository.save(e);

            return new NoticeFileUploadResponse(saved.getNoticeFileCd());

        } catch (Exception ex) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "파일 업로드 실패");
        }
    }

    // ---------------- helper ----------------

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ResponseStatusException(BAD_REQUEST, "파일이 비어있습니다.");
        if (file.getSize() > maxSizeBytes) throw new ResponseStatusException(BAD_REQUEST, "파일 크기가 제한을 초과했습니다.");

        String originalName = safeOriginalName(file.getOriginalFilename());
        String ext = extLower(originalName);
        if (ext == null) throw new ResponseStatusException(BAD_REQUEST, "파일 확장자가 없습니다.");

        String allowed = "," + allowedExtCsv.toLowerCase().replace(" ", "") + ",";
        if (!allowed.contains("," + ext + ",")) {
            throw new ResponseStatusException(BAD_REQUEST, "허용되지 않는 파일 확장자입니다: " + ext);
        }
    }

    private String safeOriginalName(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) return "file";
        int slash = Math.max(originalFilename.lastIndexOf('/'), originalFilename.lastIndexOf('\\'));
        return (slash >= 0) ? originalFilename.substring(slash + 1) : originalFilename;
    }

    private String uuidFileNameKeepExt(String originalFileName) {
        String ext = "";
        if (originalFileName != null) {
            int idx = originalFileName.lastIndexOf('.');
            if (idx > -1 && idx < originalFileName.length() - 1) {
                ext = originalFileName.substring(idx); // ".pdf"
            }
        }
        return UUID.randomUUID() + ext;
    }

    private String extLower(String originalFileName) {
        if (originalFileName == null) return null;
        int idx = originalFileName.lastIndexOf('.');
        if (idx == -1 || idx == originalFileName.length() - 1) return null;
        return originalFileName.substring(idx + 1).toLowerCase();
    }

    private String buildSavePath() {
        LocalDate now = LocalDate.now();
        return basePrefix ; //notice 폴더에 저장됨
    }
}
