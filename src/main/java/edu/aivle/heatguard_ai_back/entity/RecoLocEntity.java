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

    // 복합 PK
    @EmbeddedId
    private RecoLocId id;

    // GEE_LOC_TB랑 조인 (GEE_LOC_CD)
    @MapsId("geeLocCd") // EmbeddedId 안의 geeLocCd를 FK로 사용하겠다는 뜻
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GEE_LOC_CD", nullable = false)
    private GEELocEntity geeLoc;

    @Column(name = "RECO_LOC_RANK", nullable = false)
    private Integer recoLocRank;

    @Column(name = "RECO_LOC_RISK", nullable = false)
    private Double recoLocRisk;

    @Lob
    @Column(name = "RECO_LOC_DESC")
    private String recoLocDesc;
}
