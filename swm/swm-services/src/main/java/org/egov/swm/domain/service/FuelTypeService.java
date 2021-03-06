package org.egov.swm.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class FuelTypeService {

	@Autowired
	private MdmsRepository mdmsRepository;

	public FuelType getFuelType(String tenantId, String code, RequestInfo requestInfo) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();

		responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.MODULE_CODE,
				Constants.FUELTYPE_MASTER_NAME, "code", code, requestInfo);

		if (responseJSONArray != null && responseJSONArray.size() > 0)
			return mapper.convertValue(responseJSONArray.get(0), FuelType.class);
		else
			throw new CustomException("FuelType", "Given FuelType is invalid: " + code);

	}

}