/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemandRowMapper implements ResultSetExtractor<List<Demand>> {

	@Override
	public List<Demand> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, Demand> demandMap = new HashMap<>();
		String demandIdRsName = "did";

		while (rs.next()) {

			try {
				String demandId = rs.getString(demandIdRsName);
				log.debug("demandid in row mapper" + demandId);
				Demand demand = demandMap.get(demandId);

				if (demand == null) {

					demand = new Demand();
					demand.setId(demandId);
					demand.setBusinessService(rs.getString("dbusinessservice"));
					demand.setConsumerCode(rs.getString("dconsumerCode"));
					demand.setConsumerType(rs.getString("dconsumerType"));
					demand.setTaxPeriodFrom(rs.getLong("dtaxPeriodFrom"));
					demand.setTaxPeriodTo(rs.getLong("dtaxPeriodTo"));
					demand.setTenantId(rs.getString("dtenantid"));

					demand.setMinimumAmountPayable(rs.getBigDecimal("dminimumAmountPayable"));

					Owner owner = new Owner();
					owner.setId(Long.valueOf(rs.getString("downer")));
					demand.setOwner(owner);

					AuditDetail auditDetail = new AuditDetail();
					auditDetail.setCreatedBy(rs.getString("dcreatedby"));
					auditDetail.setLastModifiedBy(rs.getString("dlastModifiedby"));
					auditDetail.setCreatedTime(rs.getLong("dcreatedtime"));
					auditDetail.setLastModifiedTime(rs.getLong("dlastModifiedtime"));
					demand.setAuditDetail(auditDetail);

					demand.setDemandDetails(new ArrayList<>());
					demandMap.put(demand.getId(),demand);
				}

				DemandDetail demandDetail = new DemandDetail();
				demandDetail.setId(rs.getString("dlid"));
				demandDetail.setDemandId(rs.getString("dldemandid"));
				
				//TaxHeadMaster headMaster = new TaxHeadMaster();
				//headMaster.setCode(rs.getString("dltaxheadcode"));
				demandDetail.setTaxHeadMasterCode(rs.getString("dltaxheadcode"));;
				demandDetail.setTenantId(rs.getString("dltenantid"));
				demandDetail.setTaxAmount(rs.getBigDecimal("dltaxamount"));
				demandDetail.setCollectionAmount(rs.getBigDecimal("dlcollectionamount"));

				AuditDetail dlauditDetail = new AuditDetail();
				dlauditDetail.setCreatedBy(rs.getString("dlcreatedby"));
				dlauditDetail.setCreatedTime(rs.getLong("dlcreatedtime"));
				dlauditDetail.setLastModifiedBy(rs.getString("dllastModifiedby"));
				dlauditDetail.setLastModifiedTime(rs.getLong("dllastModifiedtime"));
				demandDetail.setAuditDetail(dlauditDetail);

				if (demand.getId().equals(demandDetail.getDemandId()))
					demand.getDemandDetails().add(demandDetail);
			} catch (Exception e) {
				log.debug("exception in demandRowMapper : " + e);
				throw new RuntimeException("error while mapping object from reult set : " + e);
			}
		}
		log.debug("converting map to list object ::: " + demandMap.values());
		return new ArrayList<>(demandMap.values());
	}
}
