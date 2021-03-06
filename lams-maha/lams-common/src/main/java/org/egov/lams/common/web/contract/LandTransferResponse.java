/*
 * eGov Land Management
 * APIs for Land management module. This modules handles Proposal for Land Acquisition (create,update,search of required land acquisition), land possession,transfer of land to manage the land acquisition within the ulb jurisdiction. 
 *
 * OpenAPI spec version: 1.0.0
 * Contact: contacts@egovernments.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.egov.lams.common.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.egov.common.contract.response.ResponseInfo;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@ApiModel(description = "Contract class to send response. Array of Land transfer detail items are used in case of search results or response for create, whereas single land transfer detail item is used for update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-26T05:34:54.775Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LandTransferResponse {

	@SerializedName("responseInfo")
	private ResponseInfo responseInfo = null;

	@SerializedName("landTransfer")
	private List<LandTransfer> landTransfer = null;

	public LandTransferResponse responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	/**
	 * Get responseInfo
	 * 
	 * @return responseInfo
	 **/
	@ApiModelProperty(value = "")
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public LandTransferResponse landTransfer(List<LandTransfer> landTransfer) {
		this.landTransfer = landTransfer;
		return this;
	}

	public LandTransferResponse addLandTransferItem(LandTransfer landTransferItem) {
		if (this.landTransfer == null) {
			this.landTransfer = new ArrayList<LandTransfer>();
		}
		this.landTransfer.add(landTransferItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return landTransfer
	 **/
	@ApiModelProperty(value = "Used for search result and create only")
	public List<LandTransfer> getLandTransfer() {
		return landTransfer;
	}

	public void setLandTransfer(List<LandTransfer> landTransfer) {
		this.landTransfer = landTransfer;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LandTransferResponse landTransferResponse = (LandTransferResponse) o;
		return Objects.equals(this.responseInfo, landTransferResponse.responseInfo)
				&& Objects.equals(this.landTransfer, landTransferResponse.landTransfer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, landTransfer);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class LandTransferResponse {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    landTransfer: ").append(toIndentedString(landTransfer)).append("\n");
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
