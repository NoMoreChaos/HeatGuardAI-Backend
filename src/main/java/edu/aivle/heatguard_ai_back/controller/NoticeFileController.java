package edu.aivle.heatguard_ai_back.controller;

import edu.aivle.heatguard_ai_back.dto.ApiResponse;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeFileUploadResponse;
import edu.aivle.heatguard_ai_back.service.NoticeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice/file")
public class NoticeFileController {
    private final NoticeFileService noticeFileService;
    // admin 토큰 확인 (ROLE_ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")

    public ApiResponse<NoticeFileUploadResponse> uploadOne(
            @RequestPart("file")MultipartFile file
    ){
        return noticeFileService.uploadOne(file);

    }
}
