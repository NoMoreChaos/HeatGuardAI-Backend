package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(@Param("userId") String userId);

    boolean existsByUserId(@Param("userId") String userId);

    // notice 작성자명 조회 시 사용
    @Query("select u.userNm from UserEntity u where u.userCd = :userCd")
    String findUserNmByUserCd(@Param("userCd") String userCd);
}
