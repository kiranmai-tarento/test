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
package org.egov.demand.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.repository.TaxPeriodRepository;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaxPeriodQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TaxPeriodRepository.class);

    private static final String BASE_QUERY = "SELECT * FROM EGBS_TAXPERIOD taxperiod ";

    public String prepareSearchQuery(final TaxPeriodCriteria taxPeriodCriteria, final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        logger.info("prepareSearchQuery --> ");
        prepareWhereClause(selectQuery, preparedStatementValues, taxPeriodCriteria);
        logger.info("Search tax periods query from TaxPeriodQueryBuilder -> " + selectQuery);
        return selectQuery.toString();
    }

	private void prepareWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
                                    final TaxPeriodCriteria taxPeriodCriteria) {

        selectQuery.append(" WHERE ");

        if (StringUtils.isNotBlank(taxPeriodCriteria.getTenantId())) {
            selectQuery.append(" taxperiod.tenantId = ? ");
            preparedStatementValues.add(taxPeriodCriteria.getTenantId());
        }

        Set<String> service = taxPeriodCriteria.getService(); 
        if (service != null &&  !service.isEmpty()) {
            selectQuery.append(" and taxperiod.service IN (" + getQueryForCollection(service));
        }
        
        if(taxPeriodCriteria.getPeriodCycle() != null){
        	 selectQuery.append(" and taxperiod.periodcycle = ? ");
             preparedStatementValues.add(taxPeriodCriteria.getPeriodCycle());
        }
        
        if(taxPeriodCriteria.getFromDate() != null){
       	 selectQuery.append(" and taxperiod.fromdate >= ? ");
            preparedStatementValues.add(taxPeriodCriteria.getFromDate());
       }
        
        if(taxPeriodCriteria.getToDate() != null){
       	 selectQuery.append(" and taxperiod.todate <= ? ");
            preparedStatementValues.add(taxPeriodCriteria.getToDate());
       }

        if (StringUtils.isNotBlank(taxPeriodCriteria.getCode())) {
            selectQuery.append(" and taxperiod.code = ? ");
            preparedStatementValues.add(taxPeriodCriteria.getCode());
        }

        Set<String> ids = taxPeriodCriteria.getId();
        if (ids != null && !ids.isEmpty())
            selectQuery.append(" and taxperiod.id IN "+getQueryForCollection(ids));
    }

    public String getInsertQuery(){
        return "INSERT INTO egbs_taxperiod(id,service,code,fromdate,todate,financialYear, " +
                "createddate, lastmodifieddate, createdby, lastmodifiedby, tenantid) " +
                "values (?,?,?,?,?,?,?,?,?,?,?);";
    }

    public String getUpdateQuery(){
        return "UPDATE egbs_taxperiod SET service=?, code=?, fromdate=?, todate=?, financialyear=?, " +
                "lastmodifieddate=?, lastmodifiedby=? WHERE tenantid = ? and id = ?;";
    }

    public String prepareQueryForValidation(List<TaxPeriod> taxPeriodList, String mode){
        String baseQuery = "select exists (select * from egbs_taxperiod taxperiod where ";
        StringBuilder whereClause = new StringBuilder();
        int count = 0;
        for(TaxPeriod taxPeriod : taxPeriodList){
            whereClause = whereClause.append(" ( ");
            if(StringUtils.isNotBlank(taxPeriod.getService()))
                whereClause = whereClause.append(" taxperiod.service = '").append(taxPeriod.getService()).append("' and ");
            if(StringUtils.isNotBlank(taxPeriod.getCode()))
                whereClause = whereClause.append(" taxperiod.code = '").append(taxPeriod.getCode()).append("' and ");
            if("edit".equalsIgnoreCase(mode))
                whereClause = whereClause.append(" taxperiod.id != '").append(taxPeriod.getId()).append("' and ");
            if(StringUtils.isNotBlank(taxPeriod.getTenantId()))
                whereClause = whereClause.append(" taxperiod.tenantId = '").append(taxPeriod.getTenantId()).append("')");

            count++;
            if(taxPeriodList.size()>count)
                whereClause = whereClause.append(" or ");
        }
        return baseQuery.concat(whereClause.toString()).concat(" )");
    }

    private String getQueryForCollection(Set<String> values) {
        StringBuilder query = new StringBuilder();
        if (!values.isEmpty()) {
            String[] list = values.toArray(new String[values.size()]);
            query.append(" ('"+list[0]+"'");
            for (int i = 1; i < values.size(); i++)
                query.append("," + "'"+list[i]+"'");
        }
        return query.append(")").toString();
    }
}
