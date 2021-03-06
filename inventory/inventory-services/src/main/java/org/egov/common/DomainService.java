package org.egov.common;

 
 
import java.util.Date;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Page;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.ResponseInfo;
import org.egov.inv.model.ResponseInfo.StatusEnum;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {
	@Autowired
	protected LogAwareKafkaTemplate<String, Object> kafkaQue;
 
	public AuditDetails mapAuditDetails(RequestInfo requestInfo
			) {

			return AuditDetails.builder()
					.createdBy(requestInfo.getUserInfo().getId().toString())
					.lastModifiedBy(requestInfo.getUserInfo().getId().toString())
					.createdTime(requestInfo.getTs())
					.lastModifiedTime(requestInfo.getTs()).build();

		}

		public AuditDetails mapAuditDetailsForUpdate(RequestInfo requestInfo
			) {

			return AuditDetails.builder()
					.lastModifiedBy(requestInfo.getUserInfo().getId().toString())
					.lastModifiedTime(requestInfo.getTs()).build();
		}
 
 
	public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return new ResponseInfo().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status(StatusEnum.SUCCESSFUL);
	}

	protected <T> Page getPage(Pagination<T> search) {
		return new Page().currentPage(search.getCurrentPage() + 1).pageSize(search.getPageSize())
				.totalResults(search.getTotalResults()).totalPages(search.getTotalPages());
	}

	public AuditDetails getAuditDetails(RequestInfo requestInfo, String action) {
		AuditDetails auditDetails = new AuditDetails();
		if (action.equalsIgnoreCase(Constants.ACTION_CREATE)) {
			if (requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
				auditDetails.createdBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.lastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}
			auditDetails.createdTime(new Date().getTime());
			auditDetails.lastModifiedTime(new Date().getTime());
			return auditDetails;
		} else {
			if (requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
				auditDetails.lastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}
			auditDetails.lastModifiedTime(new Date().getTime());
			return auditDetails;
		}
	}

}
