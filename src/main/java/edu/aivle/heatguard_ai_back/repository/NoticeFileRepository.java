package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.NoticeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeFileRepository extends JpaRepository<NoticeFileEntity, Long> {
}
