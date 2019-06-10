package com.lv.reg.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="post_contract_info", schema = "register")
public class PostContractInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne()
    @JoinColumn(name = "contract_id")
    private Contract contract;
    private boolean isPaidToPerformer;
    private boolean isPaidToGeodez;
    private boolean isMeasurementDone;
}
