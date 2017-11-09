/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.MaterialStoreMappingRequest;
import org.egov.inv.model.MaterialStoreMappingResponse;
import org.egov.inv.model.RequestInfo;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Api(value = "materialstoremapping", description = "the materialstoremapping API")
public interface MaterialstoremappingApi {

    @ApiOperation(value = "Create  new  material store mappings", notes = "Create  new  material store mappings", response = MaterialStoreMappingResponse.class, tags={ "MaterialStoreMappings", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Material Store Mapping created Successfully", response = MaterialStoreMappingResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialstoremapping/_create",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialStoreMappingResponse> materialstoremappingCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "This object holds the material store mapping information."  )  @Valid @RequestBody MaterialStoreMappingRequest materialStoreMappingRequest);


    @ApiOperation(value = "Get the list of material store mappings", notes = "material store mappings", response = MaterialStoreMappingResponse.class, tags={ "MaterialStoreMappings", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Material Store Mapping retrieved Successfully", response = MaterialStoreMappingResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialstoremapping/_search",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialStoreMappingResponse> materialstoremappingSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo, @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,@ApiParam(value = "material code of material store mapping ") @RequestParam(value = "material", required = false) String material,@ApiParam(value = "store code of material store mapping ") @RequestParam(value = "store", required = false) String store,@ApiParam(value = "active flag of material store mapping ") @RequestParam(value = "active", required = false) Boolean active, @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,@ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy);


    @ApiOperation(value = "Update any of the materials", notes = "Update any of the materials", response = MaterialStoreMappingResponse.class, tags={ "MaterialStoreMappings", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Material store mapping updated Successfully", response = MaterialStoreMappingResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class) })
    
    @RequestMapping(value = "/materialstoremapping/_update",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<MaterialStoreMappingResponse> materialstoremappingUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "common Request info"  )  @Valid @RequestBody MaterialStoreMappingRequest materialStoreMappingRequest);

}
