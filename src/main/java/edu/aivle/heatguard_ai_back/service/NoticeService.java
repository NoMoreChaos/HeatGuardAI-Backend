/*NoticeService : 게시판*/
// 1. [GET] 게시판 전체 리스트
// 2. 게시글 상세조회
// 3. 게시글 등록
// 4. 게시글 삭제
// 5. 게시글 수정

package edu.aivle.heatguard_ai_back.service;

import edu.aivle.heatguard_ai_back.dto.notice.request.NoticeCreateRequest;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeCreateResponse;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeDetailResponse;
import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeListResponse;
import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import edu.aivle.heatguard_ai_back.repository.NoticeFileRepository;
import edu.aivle.heatguard_ai_back.repository.NoticeRepository;
import edu.aivle.heatguard_ai_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final UserRepository userRepository;

    /**
     * 1.[GET] 게시판 전체 리스트
     * @pathvariable  noticeType 게시글 유형(null:전체)
     * @pathvariable  limitCount 최신 게시글 개수(null:전체)
     */

    public NoticeListResponse getNoticeList(String noticeType, Integer limitCount){
        //1-1. 파라미터 처리
        // notice_type
        String type = (noticeType==null || noticeType.trim().isEmpty())
                ?null
                :noticeType.trim();
        // limit_count
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
    /**
     * 2.[GET] 게시판 상세 조회
     * @pathvariable noticeCd
     */
    public NoticeDetailResponse getNoticeDetail(Integer noticeCd){
        NoticeEntity notice = noticeRepository.findById(noticeCd)
                .orElseThrow(()->
                        new IllegalArgumentException(noticeCd+"존재하지 않는 게시글입니다.")
                );
        //userNm : USER_TB에서 조회
        String userNm = userRepository.findUserNmByUserCd(notice.getUserCd());
        //userNm 없거나 NULL인 경우
        if (userNm == null) userNm ="";


        NoticeDetailResponse.NoticeFile fileDto = null;
        NoticeFileEntity file = noticeRepository.findFileByNoticeCd(noticeCd).orElse(null);

        if (file != null){
            fileDto = NoticeDetailResponse.NoticeFile.builder()
                    .noticeFileCd(file.getNoticeFileCd())
                    .noticeFileNm(file.getNoticeFileNm())
                    .noticeFileType(file.getNoticeFileType())
                    .noticeFileSize(file.getNoticeFileSize())
                    .noticeFileSavePath(file.getNoticeFileSavePath())
                    .build();
        }
        return NoticeDetailResponse.builder()
                .userCd(notice.getUserCd())
                .userNm(userNm)
                .noticeCd(notice.getNoticeCd())
                .noticeTitle(notice.getNoticeTitle())
                .noticeType(notice.getNoticeType())
                .createDate(notice.getCreateDate())
                .noticeContent(notice.getNoticeContent())
                .noticeFile(fileDto)
                .build();
    }
    /**
     * 3.[POST] 게시판 생성
     */
    @Transactional
    public NoticeCreateResponse createNotice(NoticeCreateRequest req) {

        // 1) 파일 존재 확인
        NoticeFileEntity file = noticeFileRepository.findById(req.getNoticeFileCd())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 notice_file_cd 입니다."));

        // 2) 이미 다른 공지에 연결된 파일이면 막기 (선택이지만 추천)
        if (file.getNoticeCd() != null) {
            throw new IllegalStateException("이미 공지에 연결된 첨부파일입니다.");
        }

        // 3) Notice 저장 (PK 생성)
        NoticeEntity notice = new NoticeEntity();
        notice.setUserCd(req.getUserCd());
        notice.setCfCd(req.getCfCd());
        notice.setNoticeTitle(req.getNoticeTitle());
        notice.setNoticeType(req.getNoticeType());
        notice.setNoticeContent(req.getNoticeContent());
        notice.setNoticeFixYn(req.getNoticeFixYn());

        NoticeEntity saved = noticeRepository.save(notice);
        Integer noticeCd = saved.getNoticeCd();

        // 4) NoticeFileEntity에 notice_cd 업데이트
        file.setNoticeCd(noticeCd);
        noticeFileRepository.save(file);

        return new NoticeCreateResponse(noticeCd);
    }

}

