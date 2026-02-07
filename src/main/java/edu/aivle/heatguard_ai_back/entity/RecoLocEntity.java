package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "RECO_LOC_TB")
public class RecoLocEntity {

    // 복합 PK (RECO_LOC_TYPE_CD + GEE_LOC_CD)
    @EmbeddedId
    private RecoLocId id;

    // GEE_LOC_TB 조인
    @MapsId("geeLocCd")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GEE_LOC_CD", nullable = false)
    private GEELocEntity geeLoc;

    @Column(name = "RECO_LOC_RANK", nullable = false)
    private Integer recoLocRank;

    @Column(name = "RECO_LOC_TOTAL_SCORE", nullable = false)
    private Integer recoLocTotalScore;

    @Column(name = "RECO_LOC_POPU_LEVEL", length = 10)
    private String recoLocPopuLevel; // 적음/보통/많음

    @Column(name = "RECO_LOC_VULNERABLE_SCORE")
    private Integer recoLocVulnerableScore; // 0~100

    @Column(name = "RECO_LOC_FEEL_TEMP", precision = 4, scale = 1)
    private java.math.BigDecimal recoLocFeelTemp;

    @Column(name = "RECO_LOC_LST_SCORE", precision = 4, scale = 1)
    private java.math.BigDecimal recoLocLstScore;

    @Column(name = "RECO_LOC_NDIVI_SCORE")
    private Integer recoLocNdiviScore; // (DB 컬럼명이 NDIVI라서 그대로 매핑)

    @Lob
    @Column(name = "RECO_LOC_DESC")
    private String recoLocDesc; // TEXT (DB) - API에서는 List<String>로 변환
}
