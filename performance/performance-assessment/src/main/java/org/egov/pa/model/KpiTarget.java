package org.egov.pa.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Target for the KPI Record for each Tenant ID
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T06:35:11.343Z")

public class KpiTarget   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("kpiCode")
  private String kpiCode = null;

  @JsonProperty("targetValue")
  private Long targetValue = null;

  @JsonProperty("targetDescription")
  private String targetDescription = null;

  @JsonProperty("instructions")
  private String instructions = null;

  @JsonProperty("createdBy")
  private Long createdBy = null;

  @JsonProperty("lastModifiedBy")
  private Long lastModifiedBy = null;

  @JsonProperty("createdDate")
  private Long createdDate = null;

  @JsonProperty("lastModifiedDate")
  private Long lastModifiedDate = null;

  public KpiTarget id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * unique identifier of a KpiTarget object
   * @return id
  **/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public KpiTarget kpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
    return this;
  }

   /**
   * Unique Code given to the KPI
   * @return kpiCode
  **/
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getKpiCode() {
    return kpiCode;
  }

  public void setKpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
  }

  public KpiTarget targetValue(Long targetValue) {
    this.targetValue = targetValue;
    return this;
  }

   /**
   * Value for the KPI Target
   * @return targetValue
  **/
  @NotNull


  public Long getTargetValue() {
    return targetValue;
  }

  public void setTargetValue(Long targetValue) {
    this.targetValue = targetValue;
  }

  public KpiTarget targetDescription(String targetDescription) {
    this.targetDescription = targetDescription;
    return this;
  }

   /**
   * Detailed description for target value in words
   * @return targetDescription
  **/

 @Pattern(regexp="^[a-zA-Z0-9 ]+$")
  public String getTargetDescription() {
    return targetDescription;
  }

  public void setTargetDescription(String targetDescription) {
    this.targetDescription = targetDescription;
  }

  public KpiTarget instructions(String instructions) {
    this.instructions = instructions;
    return this;
  }

   /**
   * Instructions on how to achieve the target
   * @return instructions
  **/

 @Pattern(regexp="^[a-zA-Z0-9 ]+$")
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public KpiTarget createdBy(Long createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * User ID of the User who created the record
   * @return createdBy
  **/


  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public KpiTarget lastModifiedBy(Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
    return this;
  }

   /**
   * User ID of the User who last updated the record
   * @return lastModifiedBy
  **/


  public Long getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public KpiTarget createdDate(Long createdDate) {
    this.createdDate = createdDate;
    return this;
  }

   /**
   * Date on which the record got created
   * @return createdDate
  **/


  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public KpiTarget lastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    return this;
  }

   /**
   * Date on which the record for modified
   * @return lastModifiedDate
  **/


  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KpiTarget kpiTarget = (KpiTarget) o;
    return Objects.equals(this.id, kpiTarget.id) &&
        Objects.equals(this.kpiCode, kpiTarget.kpiCode) &&
        Objects.equals(this.targetValue, kpiTarget.targetValue) &&
        Objects.equals(this.targetDescription, kpiTarget.targetDescription) &&
        Objects.equals(this.instructions, kpiTarget.instructions) &&
        Objects.equals(this.createdBy, kpiTarget.createdBy) &&
        Objects.equals(this.lastModifiedBy, kpiTarget.lastModifiedBy) &&
        Objects.equals(this.createdDate, kpiTarget.createdDate) &&
        Objects.equals(this.lastModifiedDate, kpiTarget.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, kpiCode, targetValue, targetDescription, instructions, createdBy, lastModifiedBy, createdDate, lastModifiedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiTarget {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    kpiCode: ").append(toIndentedString(kpiCode)).append("\n");
    sb.append("    targetValue: ").append(toIndentedString(targetValue)).append("\n");
    sb.append("    targetDescription: ").append(toIndentedString(targetDescription)).append("\n");
    sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
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

