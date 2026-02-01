package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    // 2.유형으로 조회
    // 2-1.limitCount 있을 때
    Page<NoticeEntity> findByNoticeType(String noticeType, Pageable pageable);

    // 2-2. limitCount 없을 때
    List<NoticeEntity> findByNoticeType(String noticeType, Sort sort);
}
