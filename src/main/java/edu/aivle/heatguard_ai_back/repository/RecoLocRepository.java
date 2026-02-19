package edu.aivle.heatguard_ai_back.repository;

import edu.aivle.heatguard_ai_back.entity.RecoLocEntity;
import edu.aivle.heatguard_ai_back.entity.RecoLocId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecoLocRepository extends JpaRepository<RecoLocEntity, RecoLocId> {

    // 서울시 전체 (gu/dong 없음)
    @EntityGraph(attributePaths = {"geeLoc"})
    @Query("""
                select r
                from RecoLocEntity r
                where r.id.recoLocTypeCd = :typeCd
                order by r.recoLocRank asc
            """)
    List<RecoLocEntity> findAllByType(@Param("typeCd") Integer typeCd, Pageable pageable);

    // 구까지만 선택
    @EntityGraph(attributePaths = {"geeLoc"})
    @Query("""
                select r
                from RecoLocEntity r
                where r.id.recoLocTypeCd = :typeCd
                  and r.geeLoc.geeCityGu = :gu
                order by r.recoLocRank asc
            """)
    List<RecoLocEntity> findAllByTypeAndGu(@Param("typeCd") Integer typeCd,
                                           @Param("gu") String gu,
                                           Pageable pageable);

    // 구+동 선택
    @EntityGraph(attributePaths = {"geeLoc"})
    @Query("""
                select r
                from RecoLocEntity r
                where r.id.recoLocTypeCd = :typeCd
                  and r.geeLoc.geeCityGu = :gu
                  and r.geeLoc.geeCityDong = :dong
                order by r.recoLocRank asc
            """)
    List<RecoLocEntity> findAllByTypeAndGuAndDong(@Param("typeCd") Integer typeCd,
                                                  @Param("gu") String gu,
                                                  @Param("dong") String dong,
                                                  Pageable pageable);
}
