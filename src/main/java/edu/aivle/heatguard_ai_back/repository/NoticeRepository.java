package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    // 1. 유형으로 조회 (limitCount 있을 때)
    Page<NoticeEntity> findByNoticeType(String noticeType, Pageable pageable);

    // 2. 유형으로 조회 (limitCount 없을 때)
    List<NoticeEntity> findByNoticeType(String noticeType, Sort sort);

    // (noticeCd, cfLocation)만 조회하기 위한 Projection 인터페이스 (엔티티 생성 없이 컬럼 매핑)
    public interface NoticeCfLocationRow {
        Integer getNoticeCd();

        String getCfLocation();
    }

    // 게시판 리스트
    // 여러 개의 noticeCd를 받아서
    // NOTICE_TB와 CF_TB를 CF_CD 기준으로 조인
    // 해당 공지글들의 CF_LOCATION 한 번에 조회
    @Query(
            value = """
                        select n.NOTICE_CD as noticeCd,
                            c.CF_LOCATION as cfLocation
                        from notice_tb n
                        left join cf_tb c on n.CF_CD = c.CF_CD
                        where n.NOTICE_CD in (:noticeCds)
                    """
            , nativeQuery = true)
    List<NoticeCfLocationRow> findCfLocationsByNoticeCds(@Param("noticeCds") List<Integer> noticeCds);

    // 게시판 상세
    // 단일 noticeCd에 대해 연결된 CF_LOCATION만 조회
    // location 값 하나만 필요하므로 String 단건 조회
    @Query(
            value = """
                        select c.CF_LOCATION
                        from notice_tb n
                        left join cf_tb c on n.CF_CD = c.CF_CD
                        where n.NOTICE_CD = :noticeCd
                    """
            , nativeQuery = true
    )
    Optional<String> findCfLocationByNoticeCd(@Param("noticeCd") Integer noticeCd);

    // 3. 게시물과 관련된 파일 상세 정보 조회(현재게시글 1개당 파일1개 구조)
    @Query("""
                select f
                from NoticeFileEntity f
                where f.noticeCd = :noticeCd
            """)
    Optional<NoticeFileEntity> findFileByNoticeCd(@Param("noticeCd") Integer noticeCd);
}
