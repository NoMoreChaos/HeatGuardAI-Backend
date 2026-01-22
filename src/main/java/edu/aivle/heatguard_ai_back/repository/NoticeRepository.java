package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, String> {

}
