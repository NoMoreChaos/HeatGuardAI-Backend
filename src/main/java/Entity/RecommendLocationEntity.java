package Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity(name = "RECO_LOC_TB")
public class RecommendLocationEntity {
    @Id @Column(name = "RECO_LOC_TYPE_CD")
    private String reco_loc_type_cd;

    @Column(name = "GEE_LOC_CD")
    private String gee_loc_cd;

    @Column(name = "RECO_LOC_RANK")
    private int reco_loc_rank;

    @Column(name = "RECO_LOC_RISK")
    private double reco_loc_risk;

    @Column(name = "RECO_LOC_DESC")
    private String reco_loc_desc;
}
