package edu.aivle.heatguard_ai_back.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    // 게시판 리스트
    @GetMapping
    public String getNoticeList(@RequestParam String type) {
        return "notice type = " + type;
    }

    // 게시판 상세
    @GetMapping("/{notice_cd}")
    public String getNoticeDetail(@PathVariable String notice_cd) {
        return notice_cd + "notice detail ok";
    }

    // 게시판 파일 다운로드
    @GetMapping("/{notice_file_cd}/download")
    public String getNoticeFileDownload(@PathVariable String notice_file_cd) {
        return notice_file_cd + "notice file download ok";
    }

    // 게시판 작성
    @PostMapping("/create")
    public String postNoticeCreate() {
        return "notice create ok";
    }

    // 게시판 삭제
    @DeleteMapping("/{notice_cd}")
    public String deleteNotice(@PathVariable String notice_cd) {
        return notice_cd + " deleted";
    }
}
