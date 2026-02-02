// TODO(S3) S3로 변경 시 코드 수정 필요
package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeFileUploadResponse;
import edu.aivle.heatguard_ai_back.repository.NoticeFileRepository;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeFileService {

    private final NoticeFileRepository noticeFileRepository;

    // 파일 저장 위치(s3와 무관/ s3 사용 시 해당위치 사용 안 됨)
    @Value("${app.notice-file.upload-dir:./uploads/notice}")
    private String uploadDir;

    // file 최대 사이즈(10mb)
    @Value("${app.notice-file.max-size-bytes:10485760}")
    private long maxSizeBytes;

    @Value("${app.notice-file.allowed-ext:pdf,png,jpg,jpeg")
    private String allowedExt;

    public ApiResponse<NoticeFileUploadResponse> uploadOne(MultipartFile file) {
        // 0) 업로드 가능 파일인지 검사 (utility method)
        validateFile(file);

        //1) 저장 폴더 생성
        Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(dirPath);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"업로드 저장 폴더 생성 실패");
        }

        //2) 원본 파일명/확장자/저장 파일명 생성
        String originalName = StringUtils.cleanPath(
                Optional.ofNullable(file.getOriginalFilename()).orElse("")
        );
        String ext = extractExt(originalName);      //extractExt : 확장자 추출함수
        String savedName = UUID.randomUUID() + (ext.isEmpty()?"" : "."+ext); //UUID로 생성된 파일명 + 확장자

        //3) 로컬 저장
        Path savePath = dirPath.resolve(savedName).normalize();

        // 폴더 경로 이탈 시 차단
        if (!savePath.startsWith(dirPath)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 파일경로");
        }
        try {
            file.transferTo(savePath.toFile());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 저장 실패");
        }
        // TODO(S3): 위 file.transferTo(...) 대신 S3 putObject 업로드로 교체
        // TODO(S3): noticeFileSavePath에는 로컬 경로 대신 "S3 KEY"를 저장하도록 변경

        // 4) 메타데이터 DB에 저장 (공지 내용 업로드 전임으로 noticeCd = null)
        NoticeFileEntity entity = new NoticeFileEntity();
        entity.setNoticeFileNm(originalName);
        entity.setNoticeFileCd(null);
        entity.setNoticeFileSaveNm(savedName);
        entity.setNoticeFileType(ext);
        entity.setNoticeFileSize(file.getSize());
        entity.setNoticeFileSavePath(savePath.toString()); // TODO(S3): S3 KEY 저장

        NoticeFileEntity saved = noticeFileRepository.save(entity);

        // 5) 응답 : notice_file_cd 반환
        NoticeFileUploadResponse resp = NoticeFileUploadResponse.builder()
                .noticeFileCd(saved.getNoticeFileCd())
                .build();

        return ApiResponse.success(resp);
    }

    // utility method : validateFile, extractExt
    /**
    * validateFile
     * 업로드 가능한 파일형태인지 체크하는 함수
     * <p></p>(파일유무/용량초과/확장자 제한 위반)
    */

    private void validateFile(MultipartFile file){
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"파일 없음");
        }
        if (file.getSize() > maxSizeBytes) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"용량 초과");
        }
        String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("");
        String ext = extractExt(originalName);

        Set<String> allowed = Arrays.stream(allowedExt.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (ext.isEmpty() || ! allowed.contains(ext.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"지원 불가 확장자");
        }
    }

    /**
     * extractExt
     * : 파일 확장자 추출 함수
     */
    private String extractExt(String filename) {
        String clean = filename == null? "":filename.trim();
        int dot = clean.lastIndexOf('.');
        if (dot < 0 || dot == clean.length()-1) return "";
        return clean.substring(dot + 1).toLowerCase();
    }
}
