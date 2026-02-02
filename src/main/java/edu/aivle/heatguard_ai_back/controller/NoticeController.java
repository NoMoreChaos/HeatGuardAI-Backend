package edu.aivle.heatguard_ai_back.controller;


import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.notice.request.NoticeCreateRequest;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeCreateResponse;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeDetailResponse;
import edu.aivle.heatguard_ai_back.service.NoticeService;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;
    // 게시판 리스트
    @GetMapping
    public ApiResponse<NoticeListResponse> getNoticeList(
            @RequestParam(name="notice_type",required = false) String noticeType,
            @RequestParam(name="limit_count",required = false) Integer limitCount
    ){
        NoticeListResponse data = noticeService.getNoticeList(noticeType,limitCount);
        return ApiResponse.success(data);
    }

    // 게시판 상세
    @GetMapping("/{notice_cd}")
    public ApiResponse<NoticeDetailResponse> getNoticeDetail(
            @PathVariable("notice_cd") Integer noticeCd //
    ) {
        NoticeDetailResponse data = noticeService.getNoticeDetail(noticeCd);
        return ApiResponse.success(data);
    }


    // 게시판 파일 다운로드
    @GetMapping("/{notice_file_cd}/download")
    public String getNoticeFileDownload(@PathVariable String notice_file_cd) {
        return notice_file_cd + "notice file download ok";
    }

    // 게시판 생성(작성)
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<NoticeCreateResponse>> createNotice(
            @Valid @RequestBody NoticeCreateRequest req
    ) {
        try {
            NoticeCreateResponse res = noticeService.createNotice(req);
            return ResponseEntity.ok(ApiResponse.success(res));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("공지 등록 중 오류가 발생했습니다."));
        }
    }

    // 게시판 삭제
    @DeleteMapping("/{notice_cd}")
    public String deleteNotice(@PathVariable String notice_cd) {
        return notice_cd + " deleted";
    }
}
