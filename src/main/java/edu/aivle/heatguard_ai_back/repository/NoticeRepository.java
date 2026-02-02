package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.dto.notice.response.NoticeDetailResponse;
import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;


import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    // 1. 유형으로 조회 (limitCount 있을 때)
    Page<NoticeEntity> findByNoticeType(String noticeType, Pageable pageable);

    // 2. 유형으로 조회 (limitCount 없을 때)
    List<NoticeEntity> findByNoticeType(String noticeType, Sort sort);

    // 3. 게시물과 관련된 파일 상세 정보 조회(현재게시글 1개당 파일1개 구조)
    @Query("""
        select f
        from NoticeFileEntity f
        where f.noticeCd = :noticeCd
    """)
    Optional<NoticeFileEntity> findFileByNoticeCd(@Param("noticeCd") Integer noticeCd);
}
