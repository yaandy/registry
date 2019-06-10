package com.lv.reg.entities;

import com.lv.reg.formBean.ContractForm;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="contract", schema = "register")
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contractIdentifier;
    @OneToOne()
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private Customer customer;
    private String region;
    private String district;
    private String villageCouncil;
    private Double square;
    private String orderType;
    private String orderStatus;
    private boolean isFinished;
    private String stage;
    private double totalPrice;
    private double payedAmount;
    private double totalCosts;
    private Date registered;
    private Date updated;
    private String comment;
    @OneToOne()
    @JoinColumn(name = "createdBy", referencedColumnName = "id")
    private User createdBy;
    @OneToOne()
    @JoinColumn(name = "assignedTo",    referencedColumnName = "id")
    private User assignedTo;
    @OneToMany(mappedBy = "contract")
    private Collection<ContractLog> log;
    private transient int passedDaysAfterLastUpdated;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false)
    private PostContractInfo postContractInfo;

}
