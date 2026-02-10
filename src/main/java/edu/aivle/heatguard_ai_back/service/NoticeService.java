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
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final UserRepository userRepository;
    private final S3NoticeFileService s3NoticeFileService;

    /**
     * 1.[GET] 게시판 전체 리스트
     * noticeType 게시글 유형(null:전체)
     * limitCount 최신 게시글 개수(null:전체)
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


        // noticeCd 목록
        // noticeCd 목록을 이용해서 CF_LOCATION을 '배치 조회(IN 쿼리)'
        // noticeCd를 모아서 한 번에 조회하는 방식
        // 개별 조회(N+1 쿼리) 방지하기 위한 처리
        List<Integer> noticeCds = entities.stream()
                .map(NoticeEntity::getNoticeCd)
                .toList();

        // location 배치 조회 -> Map으로 변환
        // NOTICE_TB와 CF_TB를 조인해서 (noticeCd, cfLocation) 조회
        // noticeCd를 key로, cfLocation을 value로 하는 Map 생성
        // DTO 변환 시 cfLocationMap.get(noticeCd) 참조
        Map<Integer, String> cfLocationMap =
                noticeCds.isEmpty()
                        ? Map.of() // 공지 리스트가 0건일 때 DB 쿼리 생략
                        : noticeRepository.findCfLocationsByNoticeCds(noticeCds).stream()
                        .filter(r -> r.getNoticeCd() != null)
                        .filter(r -> r.getCfLocation() != null) // value null 제거
                        .collect(Collectors.toMap(
                                NoticeRepository.NoticeCfLocationRow::getNoticeCd,
                                NoticeRepository.NoticeCfLocationRow::getCfLocation,
                                (a, b) -> a
                        ));

        //1-4. Entity -> Dto 변환
        List<NoticeListResponse.Item> items = entities.stream()
                .map(n -> new NoticeListResponse.Item(
                        n.getNoticeCd(),
                        n.getNoticeTitle(),
                        n.getNoticeType(),
                        n.getNoticeFixYn(),
                        n.getCreateDate(),
                        cfLocationMap.get(n.getNoticeCd())
                ))
                .toList();
        return new NoticeListResponse(items.size(),items);
    }
    /**
     * 2.[GET] 게시판 상세 조회
     * noticeCd
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
        String cfLocation = noticeRepository.findCfLocationByNoticeCd(noticeCd).orElse(null);

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
                .cfLocation(cfLocation)
                .noticeFile(fileDto)
                .build();
    }
    /**
     * 3.[POST] 게시판 생성
     */
    @Transactional
    public NoticeCreateResponse createNotice(NoticeCreateRequest req) {

        // 1) Notice 저장 (PK 생성)
        NoticeEntity notice = new NoticeEntity();
        if (req.getCfCd() != null){
            notice.setCfCd(req.getCfCd());
        }
        notice.setUserCd(req.getUserCd());
        notice.setCfCd(req.getCfCd());
        notice.setNoticeTitle(req.getNoticeTitle());
        notice.setNoticeType(req.getNoticeType());
        notice.setNoticeContent(req.getNoticeContent());
        notice.setNoticeFixYn(req.getNoticeFixYn());

        NoticeEntity saved = noticeRepository.save(notice);
        Integer noticeCd = saved.getNoticeCd();


        // 2) 파일이 있는 경우에만 notice_file_cd 추가
        Integer noticeFileCd = req.getNoticeFileCd();
        if (noticeFileCd != null) {
            NoticeFileEntity file = noticeFileRepository.findById(noticeFileCd)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 notice_file_cd 입니다."));

            if (file.getNoticeCd() != null) {
                throw new IllegalStateException("이미 공지에 연결된 첨부파일입니다.");
            }

            file.setNoticeCd(noticeCd);
            noticeFileRepository.save(file);
        }

        return new NoticeCreateResponse(noticeCd);
    }
    /**
     * [DELETE] /api/notice/{notice_cd}
     * - 게시글 삭제 + 연결된 파일들 S3 삭제 + 파일 DB 삭제
     * (추천 순서)
     * 1) S3 파일 삭제
     * 2) FILE DB 삭제
     * 3) NOTICE DB 삭제
     */
    @Transactional
    public void deleteNotice(int noticeCd) {

        NoticeEntity notice = noticeRepository.findById(noticeCd)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "게시글이 존재하지 않습니다."));

        List<NoticeFileEntity> files = noticeFileRepository.findAllByNoticeCd(noticeCd);

        // 1) S3 삭제
        for (NoticeFileEntity f : files) {
            String saveNm = f.getNoticeFileSaveNm();
            if (saveNm == null || saveNm.isBlank()) {
                throw new IllegalStateException("S3 삭제를 위한 저장파일명이 비어있습니다.");
            }
            String key = "notice/" + saveNm;
            s3NoticeFileService.delete(key);
        }


        // 2) 파일 DB 삭제
        noticeFileRepository.deleteAllByNoticeCd(noticeCd);

        // 3) 게시글 DB 삭제
        noticeRepository.delete(notice);
    }
}
