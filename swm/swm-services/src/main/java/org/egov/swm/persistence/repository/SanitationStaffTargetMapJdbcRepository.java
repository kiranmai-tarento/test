package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.SanitationStaffTargetMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SanitationStaffTargetMapJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_sst_collectionpoints";

	@Transactional
	public void delete(String tenantId, String sanitationStaffTarget) {
		delete(TABLE_NAME, tenantId, "sanitationStaffTarget", sanitationStaffTarget);
	}

	public List<SanitationStaffTargetMap> search(SanitationStaffTargetMap searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getSanitationStaffTarget() != null) {
			addAnd(params);
			params.append("sanitationStaffTarget =:sanitationStaffTarget");
			paramValues.put("sanitationStaffTarget", searchRequest.getSanitationStaffTarget());
		}

		if (searchRequest.getCollectionPoint() != null && searchRequest.getCollectionPoint() != null) {
			addAnd(params);
			params.append("collectionPoint =:collectionPoint");
			paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(SanitationStaffTargetMap.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

	}

}