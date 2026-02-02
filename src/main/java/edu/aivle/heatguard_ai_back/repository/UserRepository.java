package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(@Param("userId") String userId);
    boolean existsByUserId(@Param("userId") String userId);
}
