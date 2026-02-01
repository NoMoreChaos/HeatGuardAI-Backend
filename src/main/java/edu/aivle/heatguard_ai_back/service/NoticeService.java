/*NoticeService : 게시판*/
// 1. [GET] 게시판 전체 리스트
// 2. 게시글 상세조회
// 3. 게시글 등록
// 4. 게시글 삭제
// 5. 게시글 수정

package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeListResponse;
import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import edu.aivle.heatguard_ai_back.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 1.[GET] 게시판 전체 리스트
     * @queryparam noticeType 게시글 유형(null:전체)
     * @queryparam limitCount 최신 게시글 개수(null:전체)
     */

    public NoticeListResponse getNoticeList(String noticeType, Integer limitCount){
        //1-1. 파라미터 처리
        // noticetype
        String type = (noticeType==null || noticeType.trim().isEmpty())
                ?null
                :noticeType.trim();
        // limitcount
        Integer limit = limitCount;
        if (limit != null && limit <= 0){
            throw new IllegalArgumentException("limit_count는 1이상이어야 합니다.");
        }

        //1-2.정렬 : 고정 + 최신순
        Sort sort = Sort.by(
                Sort.Order.desc("noticeFixYn"),
                Sort.Order.desc("createDate")
        );

        //1-3. 조회
        List<NoticeEntity> entities;
        //limitCount 있으면 최신 상위 limitCount개만 출력
        if (limit != null){
            PageRequest pr = PageRequest.of(0,limit,sort);

            if (type == null) {
                entities = noticeRepository.findAll(pr).getContent();
            }else{
                entities = noticeRepository.findByNoticeType(type, pr).getContent();
            }
        }
        //limitCount 없으면 개수 상관없이 전체 조회
        else{
            if (type == null){
                entities = noticeRepository.findAll(sort);
            }else {
                entities = noticeRepository.findByNoticeType(type,sort);
            }
        }

        //1-4. Entity -> Dto 변환
        List<NoticeListResponse.Item> items = entities.stream()
                .map(n -> new NoticeListResponse.Item(
                        n.getNoticeCd(),
                        n.getNoticeTitle(),
                        n.getNoticeType(),
                        n.getNoticeFixYn(),
                        n.getCreateDate()
                ))
                .toList();
        return new NoticeListResponse(items.size(),items);
    }

}
