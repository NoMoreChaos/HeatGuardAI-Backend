package edu.aivle.heatguard_ai_back.dto.notice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor

public class NoticeListResponse {

    //1)전체 게시글 수
    private long totalCount;

    //2)게시글 리스트
    private List<Item> noticeList;

    //게시글 하나(DTO)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor

    public static class Item {
        //2-1)게시글 코드
        private Integer noticeCd;
        //2-2)게시글 제목
        private String noticeTitle;
        //2-3)게시글 타입
        private String noticeType;
        //2-4)게시글 고정여부
        private boolean noticeFixYn;
        //2-5)게시글 등록날짜
        private LocalDateTime createDate;

        //2-6)게시글 등록 쿨링포그위치명
        private String cfLocation;
    }

}
