package com.lv.reg.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract", schema = "register")
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne()
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private Customer customer;
    private String region;
    private String district;
    private String orderType;
    private String orderStatus;
    private boolean isFinished;
    private String stage;
    private double totalPrice;
    private double payedAmount;
    private Date registered;
    private Date updated;
//    private int createdBy;
}
