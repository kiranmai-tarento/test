package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.MaterialRequest;
import org.egov.inv.model.MaterialResponse;
import org.egov.inv.model.RequestInfo;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class MaterialsApiController implements MaterialsApi {



    public ResponseEntity<MaterialResponse> materialsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody MaterialRequest materialRequest) {
        // do some magic!
        return new ResponseEntity<MaterialResponse>(HttpStatus.OK);
    }

    public ResponseEntity<MaterialResponse> materialsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Material ") @RequestParam(value = "code", required = false) String code,
        @ApiParam(value = "name of the Material ") @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "description of the Material ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "old code of the Material ") @RequestParam(value = "oldCode", required = false) String oldCode,
        @ApiParam(value = "material type of the Material ") @RequestParam(value = "materialType", required = false) Long materialType,
        @ApiParam(value = "base uom of the Material ") @RequestParam(value = "baseUom", required = false) Long baseUom,
        @ApiParam(value = "inventory type of the Material ", allowableValues = "Asset, Consumable") @RequestParam(value = "inventoryType", required = false) String inventoryType,
        @ApiParam(value = "status of the Material ", allowableValues = "Active, Withdrawn, Obsolete") @RequestParam(value = "status", required = false) String status,
        @ApiParam(value = "purchase uom of the Material ") @RequestParam(value = "purchaseUom", required = false) Long purchaseUom,
        @ApiParam(value = "expense account of the Material ") @RequestParam(value = "expenseAccount", required = false) Long expenseAccount,
        @ApiParam(value = "min quantity of the Material ") @RequestParam(value = "minQuantity", required = false) Long minQuantity,
        @ApiParam(value = "max quantity of the Material ") @RequestParam(value = "maxQuantity", required = false) Long maxQuantity,
        @ApiParam(value = "staocking uom of the Material ") @RequestParam(value = "staockingUom", required = false) Long staockingUom,
        @ApiParam(value = "material class of the Material ", allowableValues = "HighUsage, MediumUsage, LowUsage") @RequestParam(value = "materialClass", required = false) String materialClass,
        @ApiParam(value = "reorder level of the Material ") @RequestParam(value = "reorderLevel", required = false) Long reorderLevel,
        @ApiParam(value = "reorder quantity of the Material ") @RequestParam(value = "reorderQuantity", required = false) Long reorderQuantity,
        @ApiParam(value = "material control type of the Material ", allowableValues = "LOTControl, Shelf_life_Control") @RequestParam(value = "materialControlType", required = false) String materialControlType,
        @ApiParam(value = "model of the Material ") @RequestParam(value = "model", required = false) String model,
        @ApiParam(value = "manufacture part no of the Material ") @RequestParam(value = "manufacturePartNo", required = false) String manufacturePartNo,
        @ApiParam(value = "techincal specs of the Material ") @RequestParam(value = "techincalSpecs", required = false) String techincalSpecs,
        @ApiParam(value = "terms of delivery of the Material ") @RequestParam(value = "termsOfDelivery", required = false) String termsOfDelivery,
        @ApiParam(value = "override material control type of the Material ") @RequestParam(value = "overrideMaterialControlType", required = false) Boolean overrideMaterialControlType,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<MaterialResponse>(HttpStatus.OK);
    }

    public ResponseEntity<MaterialResponse> materialsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody MaterialRequest materialRequest) {
        // do some magic!
        return new ResponseEntity<MaterialResponse>(HttpStatus.OK);
    }

}
