package com.team2.prescriptionservice.DataLayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prescriptionid", nullable = false)
    private Integer prescriptionId;

    @Column(name = "medication", nullable = false)
    private String medication;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "dateprinted")
    @Temporal(TemporalType.DATE)
    private Date datePrinted;

    @Column(name = "instructions")
    private String instructions;


}
