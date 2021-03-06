package org.egov.inv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PriceListSearchRequest {

    private String tenantId;

    private String ids;

    private String id;

    private String supplier;
    
    private String suppliers;
    
    private String rateType;

    private String rateContractNumber;

    private String agreementNumber;
    
    private String agreementNumbers;

    private Long rateContractDate;
    
    private Long agreementDate;
    
    private Long agreementStartDate;
    
    private Long agreementEndDate;
    
    private Boolean active;

    private Integer pageSize;

    private Integer offSet;

    private String sortBy;

}
