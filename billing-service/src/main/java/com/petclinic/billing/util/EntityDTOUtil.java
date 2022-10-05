package com.petclinic.billing.util;

import com.petclinic.billing.businesslayer.BillServiceImpl;
import com.petclinic.billing.datalayer.Bill;
import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.exceptions.InvalidInputException;
import lombok.Generated;
import org.springframework.beans.BeanUtils;

public class EntityDTOUtil {

    @Generated
    public EntityDTOUtil(){}
    public static BillDTO entityToDTO(Bill bill){
        BillDTO billDTO = new BillDTO();
        BeanUtils.copyProperties(bill, billDTO);
        return billDTO;
    }

    public static Bill dtoToEntity(BillDTO billDTO){
        billDTO.setBillId(ShortIdGen.getShortId());
        //checks for valid billId (might be removed soon)
        if(billDTO.getCustomerId() <= 0) {
            throw new InvalidInputException("That customer id is invalid");
        }

        //checks for valid visit type
        if(BillServiceImpl.visitTypePrices.getOrDefault(billDTO.getVisitType(), null) == null){
            throw new InvalidInputException("That visit type does not exist");
        }

        Bill bill = new Bill();
        BeanUtils.copyProperties(billDTO, bill);
        //sets amount based on visit type
        bill.setAmount(BillServiceImpl.visitTypePrices.get(bill.getVisitType()));
        return bill;
    }

    public static Integer verifyId(Integer id){
        if(id <= 0){
            throw new InvalidInputException("That id is invalid");
        }
        return id;
    }
}
