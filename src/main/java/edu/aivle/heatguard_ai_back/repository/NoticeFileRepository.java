package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeFileRepository extends JpaRepository<NoticeFileEntity, Integer> {

    List<NoticeFileEntity> findAllByNoticeCd(Integer noticeCd);

    void deleteAllByNoticeCd(Integer noticeCd);
}
