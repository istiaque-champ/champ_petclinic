package com.team2.prescriptionservice.DataLayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "medication", nullable = false)
    private String medication;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "datePrinted")
    @Temporal(TemporalType.DATE)
    private Date datePrinted;

    @Column(name = "instructions")
    private String instructions;


}
