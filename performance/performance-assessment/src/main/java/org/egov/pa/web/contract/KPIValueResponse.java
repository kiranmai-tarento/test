package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pa.model.KpiValue;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIValueResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KPIValueResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("kpiValues")
  private List<KpiValue> kpiValues = null;

  public KPIValueResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public KPIValueResponse kpiValues(List<KpiValue> kpiValues) {
    this.kpiValues = kpiValues;
    return this;
  }

  public KPIValueResponse addKpiValuesItem(KpiValue kpiValuesItem) {
    if (this.kpiValues == null) {
      this.kpiValues = new ArrayList<KpiValue>();
    }
    this.kpiValues.add(kpiValuesItem);
    return this;
  }

   /**
   * Get kpiValues
   * @return kpiValues
  **/

  @Valid

  public List<KpiValue> getKpiValues() {
    return kpiValues;
  }

  public void setKpiValues(List<KpiValue> kpiValues) {
    this.kpiValues = kpiValues;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIValueResponse kpIValueResponse = (KPIValueResponse) o;
    return Objects.equals(this.responseInfo, kpIValueResponse.responseInfo) &&
        Objects.equals(this.kpiValues, kpIValueResponse.kpiValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, kpiValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIValueResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    kpiValues: ").append(toIndentedString(kpiValues)).append("\n");
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

