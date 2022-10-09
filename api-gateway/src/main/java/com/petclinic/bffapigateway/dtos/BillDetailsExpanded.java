package com.petclinic.bffapigateway.dtos;

import lombok.Data;

@Data
public class BillDetailsExpanded {
    private BillDetails billDetails;
    private OwnerDetails ownerDetails;
}
