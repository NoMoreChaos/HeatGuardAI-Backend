package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("""
    SELECT u From USER_TB u WHERE u.user_id = :userId
    """)
    Optional<UserEntity> findByUserId(@Param("userId") String userId);

    @Query("""
    SELECT (COUNT(u) > 0) FROM USER_TB u WHERE u.user_id = :userId
    """)
    boolean existsByUserId(@Param("userId") String userId);
}
