package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Indent;
import org.egov.inv.model.MaterialIssueDetails;
import org.egov.inv.model.Store;
import org.egov.inv.model.WorkFlowDetails;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds information for Indent Issue, Non Indent Issue and Transfer Outward Note. 
 */
@ApiModel(description = "This object holds information for Indent Issue, Non Indent Issue and Transfer Outward Note. ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class MaterialIssue   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  /**
   * Gets or Sets issueType
   */
  public enum IssueTypeEnum {
    INDENTISSUE("IndentIssue"),
    
    NONINDENTISSUE("NonIndentIssue");

    private String value;

    IssueTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IssueTypeEnum fromValue(String text) {
      for (IssueTypeEnum b : IssueTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("issueType")
  private IssueTypeEnum issueType = null;

  @JsonProperty("issuingStore")
  private Store issuingStore = null;

  @JsonProperty("issueNoteNumber")
  private String issueNoteNumber = null;

  @JsonProperty("issueDate")
  private Long issueDate = null;

  /**
   * material issue status of the MaterialIssue 
   */
  public enum MaterialIssueStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    MaterialIssueStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MaterialIssueStatusEnum fromValue(String text) {
      for (MaterialIssueStatusEnum b : MaterialIssueStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("materialIssueStatus")
  private MaterialIssueStatusEnum materialIssueStatus = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("totalIssueValue")
  private BigDecimal totalIssueValue = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("indent")
  private Indent indent = null;

  @JsonProperty("issuedToEmployee")
  private String issuedToEmployee = null;

  @JsonProperty("issuedToDesignation")
  private String issuedToDesignation = null;

  /**
   * Applicable in case of None Indent Issue. This field holds information about Issuing purpose of the Non Indent Issue
   */
  public enum IssuePurposeEnum {
    WRITEOFFORSCRAP("WriteOffORScrap"),
    
    RETURNTOSUPPLIER("ReturnToSupplier");

    private String value;

    IssuePurposeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IssuePurposeEnum fromValue(String text) {
      for (IssuePurposeEnum b : IssuePurposeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("issuePurpose")
  private IssuePurposeEnum issuePurpose = null;

  @JsonProperty("materialIssueDetails")
  private List<MaterialIssueDetails> materialIssueDetails = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MaterialIssue id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Material Issue 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Material Issue ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialIssue tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Issue
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Material Issue")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialIssue issueType(IssueTypeEnum issueType) {
    this.issueType = issueType;
    return this;
  }

   /**
   * Get issueType
   * @return issueType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public IssueTypeEnum getIssueType() {
    return issueType;
  }

  public void setIssueType(IssueTypeEnum issueType) {
    this.issueType = issueType;
  }

  public MaterialIssue issuingStore(Store issuingStore) {
    this.issuingStore = issuingStore;
    return this;
  }

   /**
   * This field holds the issuing store information. 
   * @return issuingStore
  **/
  @ApiModelProperty(required = true, value = "This field holds the issuing store information. ")
  @NotNull

  @Valid

  public Store getIssuingStore() {
    return issuingStore;
  }

  public void setIssuingStore(Store issuingStore) {
    this.issuingStore = issuingStore;
  }

  public MaterialIssue issueNoteNumber(String issueNoteNumber) {
    this.issueNoteNumber = issueNoteNumber;
    return this;
  }

   /**
   * issue note number/outward note number.Auto generated number, read only. 
   * @return issueNoteNumber
  **/
  @ApiModelProperty(readOnly = true, value = "issue note number/outward note number.Auto generated number, read only. ")

 @Size(max=100)
  public String getIssueNoteNumber() {
    return issueNoteNumber;
  }

  public void setIssueNoteNumber(String issueNoteNumber) {
    this.issueNoteNumber = issueNoteNumber;
  }

  public MaterialIssue issueDate(Long issueDate) {
    this.issueDate = issueDate;
    return this;
  }

   /**
   * issue date of the MaterialIssue 
   * @return issueDate
  **/
  @ApiModelProperty(required = true, value = "issue date of the MaterialIssue ")
  @NotNull


  public Long getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Long issueDate) {
    this.issueDate = issueDate;
  }

  public MaterialIssue materialIssueStatus(MaterialIssueStatusEnum materialIssueStatus) {
    this.materialIssueStatus = materialIssueStatus;
    return this;
  }

   /**
   * material issue status of the MaterialIssue 
   * @return materialIssueStatus
  **/
  @ApiModelProperty(required = true, value = "material issue status of the MaterialIssue ")
  @NotNull

 @Size(min=5,max=50)
  public MaterialIssueStatusEnum getMaterialIssueStatus() {
    return materialIssueStatus;
  }

  public void setMaterialIssueStatus(MaterialIssueStatusEnum materialIssueStatus) {
    this.materialIssueStatus = materialIssueStatus;
  }

  public MaterialIssue description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the MaterialIssue
   * @return description
  **/
  @ApiModelProperty(value = "description of the MaterialIssue")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MaterialIssue totalIssueValue(BigDecimal totalIssueValue) {
    this.totalIssueValue = totalIssueValue;
    return this;
  }

   /**
   * total issue value of the MaterialIssue
   * @return totalIssueValue
  **/
  @ApiModelProperty(required = true, value = "total issue value of the MaterialIssue")
  @NotNull

  @Valid

  public BigDecimal getTotalIssueValue() {
    return totalIssueValue;
  }

  public void setTotalIssueValue(BigDecimal totalIssueValue) {
    this.totalIssueValue = totalIssueValue;
  }

  public MaterialIssue fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Indent Issue.
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Indent Issue.")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public MaterialIssue designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the created by user at the time of Indent Issue/Non Indent Issue created.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the created by user at the time of Indent Issue/Non Indent Issue created.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public MaterialIssue indent(Indent indent) {
    this.indent = indent;
    return this;
  }

   /**
   * Applicable for Indent Issue. This object holds the indent information for which the material is being issued. There could be multiple issues against an indent.
   * @return indent
  **/
  @ApiModelProperty(value = "Applicable for Indent Issue. This object holds the indent information for which the material is being issued. There could be multiple issues against an indent.")

  @Valid

  public Indent getIndent() {
    return indent;
  }

  public void setIndent(Indent indent) {
    this.indent = indent;
  }

  public MaterialIssue issuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
    return this;
  }

   /**
   * Applicable for Indent Issue and Transfer Outward Issue. Indent/Transfer Issued To Employee.
   * @return issuedToEmployee
  **/
  @ApiModelProperty(value = "Applicable for Indent Issue and Transfer Outward Issue. Indent/Transfer Issued To Employee.")


  public String getIssuedToEmployee() {
    return issuedToEmployee;
  }

  public void setIssuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
  }

  public MaterialIssue issuedToDesignation(String issuedToDesignation) {
    this.issuedToDesignation = issuedToDesignation;
    return this;
  }

   /**
   * Applicable for Indent Issue. Designation of the employee to whom the indent is issued.
   * @return issuedToDesignation
  **/
  @ApiModelProperty(value = "Applicable for Indent Issue. Designation of the employee to whom the indent is issued.")


  public String getIssuedToDesignation() {
    return issuedToDesignation;
  }

  public void setIssuedToDesignation(String issuedToDesignation) {
    this.issuedToDesignation = issuedToDesignation;
  }

  public MaterialIssue issuePurpose(IssuePurposeEnum issuePurpose) {
    this.issuePurpose = issuePurpose;
    return this;
  }

   /**
   * Applicable in case of None Indent Issue. This field holds information about Issuing purpose of the Non Indent Issue
   * @return issuePurpose
  **/
  @ApiModelProperty(value = "Applicable in case of None Indent Issue. This field holds information about Issuing purpose of the Non Indent Issue")


  public IssuePurposeEnum getIssuePurpose() {
    return issuePurpose;
  }

  public void setIssuePurpose(IssuePurposeEnum issuePurpose) {
    this.issuePurpose = issuePurpose;
  }

  public MaterialIssue materialIssueDetails(List<MaterialIssueDetails> materialIssueDetails) {
    this.materialIssueDetails = materialIssueDetails;
    return this;
  }

  public MaterialIssue addMaterialIssueDetailsItem(MaterialIssueDetails materialIssueDetailsItem) {
    if (this.materialIssueDetails == null) {
      this.materialIssueDetails = new ArrayList<MaterialIssueDetails>();
    }
    this.materialIssueDetails.add(materialIssueDetailsItem);
    return this;
  }

   /**
   * List of materials issued for indent and non indent items.
   * @return materialIssueDetails
  **/
  @ApiModelProperty(value = "List of materials issued for indent and non indent items.")

  @Valid

  public List<MaterialIssueDetails> getMaterialIssueDetails() {
    return materialIssueDetails;
  }

  public void setMaterialIssueDetails(List<MaterialIssueDetails> materialIssueDetails) {
    this.materialIssueDetails = materialIssueDetails;
  }

  public MaterialIssue workFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
    return this;
  }

   /**
   * Get workFlowDetails
   * @return workFlowDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public WorkFlowDetails getWorkFlowDetails() {
    return workFlowDetails;
  }

  public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
  }

  public MaterialIssue stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * State id of the Material Issue.     
   * @return stateId
  **/
  @ApiModelProperty(value = "State id of the Material Issue.     ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public MaterialIssue auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialIssue materialIssue = (MaterialIssue) o;
    return Objects.equals(this.id, materialIssue.id) &&
        Objects.equals(this.tenantId, materialIssue.tenantId) &&
        Objects.equals(this.issueType, materialIssue.issueType) &&
        Objects.equals(this.issuingStore, materialIssue.issuingStore) &&
        Objects.equals(this.issueNoteNumber, materialIssue.issueNoteNumber) &&
        Objects.equals(this.issueDate, materialIssue.issueDate) &&
        Objects.equals(this.materialIssueStatus, materialIssue.materialIssueStatus) &&
        Objects.equals(this.description, materialIssue.description) &&
        Objects.equals(this.totalIssueValue, materialIssue.totalIssueValue) &&
        Objects.equals(this.fileStoreId, materialIssue.fileStoreId) &&
        Objects.equals(this.designation, materialIssue.designation) &&
        Objects.equals(this.indent, materialIssue.indent) &&
        Objects.equals(this.issuedToEmployee, materialIssue.issuedToEmployee) &&
        Objects.equals(this.issuedToDesignation, materialIssue.issuedToDesignation) &&
        Objects.equals(this.issuePurpose, materialIssue.issuePurpose) &&
        Objects.equals(this.materialIssueDetails, materialIssue.materialIssueDetails) &&
        Objects.equals(this.workFlowDetails, materialIssue.workFlowDetails) &&
        Objects.equals(this.stateId, materialIssue.stateId) &&
        Objects.equals(this.auditDetails, materialIssue.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, issueType, issuingStore, issueNoteNumber, issueDate, materialIssueStatus, description, totalIssueValue, fileStoreId, designation, indent, issuedToEmployee, issuedToDesignation, issuePurpose, materialIssueDetails, workFlowDetails, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    issueType: ").append(toIndentedString(issueType)).append("\n");
    sb.append("    issuingStore: ").append(toIndentedString(issuingStore)).append("\n");
    sb.append("    issueNoteNumber: ").append(toIndentedString(issueNoteNumber)).append("\n");
    sb.append("    issueDate: ").append(toIndentedString(issueDate)).append("\n");
    sb.append("    materialIssueStatus: ").append(toIndentedString(materialIssueStatus)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    totalIssueValue: ").append(toIndentedString(totalIssueValue)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
    sb.append("    indent: ").append(toIndentedString(indent)).append("\n");
    sb.append("    issuedToEmployee: ").append(toIndentedString(issuedToEmployee)).append("\n");
    sb.append("    issuedToDesignation: ").append(toIndentedString(issuedToDesignation)).append("\n");
    sb.append("    issuePurpose: ").append(toIndentedString(issuePurpose)).append("\n");
    sb.append("    materialIssueDetails: ").append(toIndentedString(materialIssueDetails)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

