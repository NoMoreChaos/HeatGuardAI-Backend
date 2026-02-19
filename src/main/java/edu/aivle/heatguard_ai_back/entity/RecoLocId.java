package edu.aivle.heatguard_ai_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecoLocId implements Serializable {

    @Column(name = "RECO_LOC_TYPE_CD", nullable = false)
    private Integer recoLocTypeCd;

    @Column(name = "GEE_LOC_CD", length = 36, nullable = false)
    private String geeLocCd;
}
