package com.petclinic.billing.datalayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "bills")
@Setter
@Getter
@NoArgsConstructor
public class Bill {
    @Id
    private String id;

    @Indexed(unique = true)
    private Integer billId;

    private Integer customerId;

    private Integer vetId;

    private String visitType;

    private Instant date = Instant.now();

    private double amount;
}