package org.egov.works.estimate.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.*;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

@Service
public class EstimateValidator {

    @Autowired
    private EstimateUtils estimateUtils;

    @Autowired
    private AbstractEstimateService abstractEstimateService;

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;

    @Autowired
    private AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private EstimateTechnicalSanctionRepository estimateTechnicalSanctionRepository;

    @Autowired
    private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, Boolean isNew) {
        Map<String, String> messages = new HashMap<>();
        for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
        	validateEstimateDetails(estimate, messages);
        	if (estimate.getSpillOverFlag())
        		validateSpillOverData(estimate, messages);
            validateMasterData(estimate, abstractEstimateRequest.getRequestInfo(), messages, isNew);
            validateAbstractEstimateNumber(abstractEstimateRequest.getRequestInfo(), isNew, messages, estimate);
            validateAdminSanctionDetails(abstractEstimateRequest.getRequestInfo(), isNew, messages, estimate);
            validateEstimateAssetDetails(estimate, abstractEstimateRequest.getRequestInfo(), messages);

            if (!messages.isEmpty())
                throw new CustomException(messages);
        }
    }

	private void validateAdminSanctionDetails(RequestInfo requestInfo, Boolean isNew,
			Map<String, String> messages, final AbstractEstimate estimate) {
		AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
		searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
		List<AbstractEstimate> oldEstimates = abstractEstimateService
				.search(searchContract, requestInfo).getAbstractEstimates();
		if (!estimate.getSpillOverFlag() && !isNew && estimate.getWorkFlowDetails() != null
				&& Constants.APPROVE.equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
			if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
				messages.put(Constants.KEY_NULL_ADMINSANCTIONNUMBER, Constants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
			if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
				messages.put(Constants.KEY_UNIQUE_ADMINSANCTIONNUMBER, Constants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
		} else if (estimate.getSpillOverFlag()) {
			if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
				messages.put(Constants.KEY_NULL_ADMINSANCTIONNUMBER, Constants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
			if (estimate.getAdminSanctionDate() == null)
				messages.put(Constants.KEY_NULL_ADMINSANCTIONDATE, Constants.MESSAGE_NULL_ADMINSANCTIONDATE);
			if (estimate.getAdminSanctionDate() > new Date().getTime())
				messages.put(Constants.KEY_FUTUREDATE_ADMINSANCTIONDATE, Constants.MESSAGE_FUTUREDATE_ADMINSANCTIONDATE);
			if (estimate.getAdminSanctionDate() < estimate.getDateOfProposal())
				messages.put(Constants.KEY_ADMINSANCTION_PROPOSAL_DATE, Constants.MESSAGE_ADMINSANCTION_PROPOSAL_DATE);
			if (estimate.getAdminSanctionBy() == null || (estimate.getAdminSanctionBy() != null
					&& StringUtils.isBlank(estimate.getAdminSanctionBy().getUserName())))
				messages.put(Constants.KEY_NULL_ADMINSANCTIONBY, Constants.MESSAGE_NULL_ADMINSANCTIONBY);
			if ((isNew && !oldEstimates.isEmpty()) || (!isNew && !oldEstimates.isEmpty()
					&& !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId())))
				messages.put(Constants.KEY_UNIQUE_ADMINSANCTIONNUMBER, Constants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
		}
	}

	private void validateAbstractEstimateNumber(RequestInfo requestInfo, Boolean isNew,
			Map<String, String> messages, final AbstractEstimate estimate) {
		AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
		if (estimate.getId() != null)
		    searchContract.setIds(Arrays.asList(estimate.getId()));
		searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
		searchContract.setTenantId(estimate.getTenantId());
		List<AbstractEstimate> oldEstimates = abstractEstimateService
		        .search(searchContract, requestInfo).getAbstractEstimates();
		if (isNew && !oldEstimates.isEmpty())
		    messages.put(Constants.KEY_UNIQUE_ABSTRACTESTIMATENUMBER,
		            Constants.MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER);
	}

    private void validateSpillOverData(AbstractEstimate estimate, Map<String, String> messages) {
        if (estimate.getAbstractEstimateNumber() == null)
            messages.put(Constants.KEY_NULL_ABSTRACTESTIMATE_NUMBER,
                    Constants.MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER);
        for (AbstractEstimateDetails details : estimate.getAbstractEstimateDetails())
        	if (details.getProjectCode() == null)
        		messages.put(Constants.KEY_NULL_WIN_NUMBER,
                        Constants.MESSAGE_NULL_WIN_NUMBER);
        if (estimate.getDateOfProposal() != null
                && estimate.getDateOfProposal() > new Date().getTime())
            messages.put(Constants.KEY_FUTUREDATE_DATEOFPROPOSAL,
                    Constants.MESSAGE_FUTUREDATE_DATEOFPROPOSAL);
        	
    }

    private void validateEstimateDetails(AbstractEstimate estimate, Map<String, String> messages) {
        BigDecimal estimateAmount = BigDecimal.ZERO;
        if (estimate.getAbstractEstimateDetails().isEmpty())
        	messages.put(Constants.KEY_ABSTRACTESTIMATE_DETAILS_REQUIRED,
                    Constants.MESSAGE_ABSTRACTESTIMATE_DETAILS_REQUIRED);
        for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails())
            estimateAmount = estimateAmount.add(aed.getEstimateAmount());
        if (estimateAmount.compareTo(BigDecimal.ZERO) == -1)
            messages.put(Constants.KEY_INVALID_ESTIMATEAMOUNT,
                    Constants.MESSAGE_INVALID_ESTIMATEAMOUNT);
    }

    public void validateMasterData(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
                                   Map<String, String> messages, Boolean isNew) {

        Boolean isFinIntReq = false;
        JSONArray responseJSONArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT,
                CommonConstants.CODE, Constants.FINANCIAL_INTEGRATION_KEY,
                abstractEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isFinIntReq = true;
        }
        
        if (abstractEstimate.getSpillOverFlag() || (!isNew && AbstractEstimateStatus.CHECKED.equals(abstractEstimate.getStatus())
        		&& Constants.FORWARD.equalsIgnoreCase(abstractEstimate.getWorkFlowDetails().getAction())))
        	validateFinancialDetails(abstractEstimate, isFinIntReq, requestInfo, messages);
        validateTypeOfWork(abstractEstimate.getTypeOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(abstractEstimate.getSubTypeOfWork(), abstractEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(abstractEstimate.getDepartment(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateWard(abstractEstimate.getWard(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateLocality(abstractEstimate.getLocality(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateNatureOfWork(abstractEstimate.getNatureOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateReferenceType(abstractEstimate.getReferenceType(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateModeOfAllotment(abstractEstimate.getModeOfAllotment(), abstractEstimate.getTenantId(), requestInfo, messages);
    }


    private void validateModeOfAllotment(ModeOfAllotment modeOfAllotment, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
    	JSONArray responseJSONArray;
        if (modeOfAllotment != null && modeOfAllotment.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.MODEOFALLOTMENT_OBJECT,
                    CommonConstants.CODE, modeOfAllotment.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_MODEOFALLOTMENT_INVALID,
                        Constants.MESSAGE_MODEOFALLOTMENT_INVALID);
            }
        } else
            messages.put(Constants.KEY_MODEOFALLOTMENT_REQUIRED,
                    Constants.MESSAGE_MODEOFALLOTMENT_REQUIRED);
	}

	private void validateReferenceType(ReferenceType referenceType, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
    	JSONArray responseJSONArray;
        if (referenceType != null && referenceType.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.REFERENCETYPE_OBJECT,
                    CommonConstants.CODE, referenceType.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_REFERENCETYPE_INVALID,
                        Constants.MESSAGE_REFERENCETYPE_INVALID);
            }
        } else
            messages.put(Constants.KEY_REFERENCETYPE_REQUIRED,
                    Constants.MESSAGE_REFERENCETYPE_REQUIRED);
	}

	private void validateFinancialDetails(AbstractEstimate abstractEstimate, Boolean isFinIntReq, RequestInfo requestInfo,
                                          Map<String, String> messages) {
        validateFund(abstractEstimate.getFund(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateFunction(abstractEstimate.getFunction(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateScheme(abstractEstimate.getScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(abstractEstimate.getSubScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        // TODO: MDMS data needs to be added
//        validateBudgetGroup(abstractEstimate.getBudgetGroup(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateAccountCode(abstractEstimate.getAccountCode(), isFinIntReq, messages);
    }

	public void validateFund(Fund fund, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (fund != null && fund.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUND_OBJECT,
                    CommonConstants.CODE, fund.getCode(), tenantId, requestInfo,
                    Constants.EGF_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUND_INVALID,
                        Constants.MESSAGE_FUND_INVALID);
            }
        } else
            messages.put(Constants.KEY_FUND_REQUIRED,
                    Constants.MESSAGE_FUND_REQUIRED);
    }

    public void validateFunction(Function function, String tenantId, RequestInfo requestInfo,
                                 Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (function != null && function.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUNCTION_OBJECT,
                    CommonConstants.CODE, function.getCode(), tenantId, requestInfo,
                    Constants.EGF_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUNCTION_INVALID,
                        Constants.MESSAGE_FUNCTION_INVALID);
            }
        } else
               messages.put(Constants.KEY_FUNCTION_REQUIRED,
                    Constants.MESSAGE_FUNCTION_REQUIRED);
    }

    public void validateScheme(Scheme scheme, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (scheme != null && scheme.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SCHEME_OBJECT,
                    CommonConstants.CODE, scheme.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SCHEME_INVALID,
                        Constants.MESSAGE_SCHEME_INVALID);
            }
        }
    }

    public void validateSubScheme(SubScheme subScheme, String tenantId, RequestInfo requestInfo,
                                  Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subScheme != null && subScheme.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SUBSCHEME_OBJECT,
                    CommonConstants.CODE, subScheme.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBSCHEME_INVALID,
                        Constants.MESSAGE_SUBSCHEME_INVALID);
            }
        }
    }

    public void validateBudgetGroup(BudgetGroup budgetGroup, String tenantId, RequestInfo requestInfo,
                                    Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (budgetGroup != null && budgetGroup.getName() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.BUDGETGROUP_OBJECT,
                    CommonConstants.NAME, budgetGroup.getName(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_BUDGETGROUP_INVALID,
                        Constants.MESSAGE_BUDGETGROUP_INVALID);
            }
        } else
            messages.put(Constants.KEY_BUDGETGROUP_NAME_REQUIRED,
                    Constants.MESSAGE_BUDGETGROUP_NAME_REQUIRED);
    }
    
    private void validateAccountCode(String accountCode, Boolean isFinIntReq, Map<String, String> messages) {
    	//TODO: Validate once we get clarity on MDMS or fin integration
    	if (isFinIntReq && accountCode == null)
    		messages.put(Constants.KEY_ACCOUNTCODE_REQUIRED,
                    Constants.MESSAGE_ACCOUNTCODE_REQUIRED);
	}

    public void validateTypeOfWork(TypeOfWork typeOfWork, String tenantId, RequestInfo requestInfo,
                                   Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (typeOfWork != null && typeOfWork.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT,
                    CommonConstants.CODE, typeOfWork.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_TYPEOFWORK_INVALID,
                        Constants.MESSAGE_TYPEOFWORK_INVALID);
            }
        } else
            messages.put(Constants.KEY_TYPEOFWORK_REQUIRED,
                    Constants.MESSAGE_TYPEOFWORK_REQUIRED);
    }

    public void validateSubTypeOfWork(TypeOfWork subTypeOfWork, String tenantId, RequestInfo requestInfo,
                                      Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subTypeOfWork != null && subTypeOfWork.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT,
                    CommonConstants.CODE, subTypeOfWork.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBTYPEOFWORK_INVALID,
                        Constants.MESSAGE_SUBTYPEOFWORK_INVALID);
            }
        } else
            messages.put(Constants.KEY_DEPARTMENT_CODE_REQUIRED,
                    Constants.MESSAGE_DEPARTMENT_CODE_REQUIRED);
    }

    public void validateDepartment(Department department, String tenantId, RequestInfo requestInfo,
                                   Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (department != null && department.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.DEPARTMENT_OBJECT,
                    CommonConstants.CODE, department.getCode(), tenantId, requestInfo,
                    Constants.COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_DEPARTMENT_INVALID,
                        Constants.MESSAGE_DEPARTMENT_INVALID);
            }
        } else
            messages.put(Constants.KEY_DEPARTMENT_CODE_REQUIRED,
                    Constants.MESSAGE_DEPARTMENT_CODE_REQUIRED);
    }
    
	public void validateWard(Boundary ward, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (ward != null && ward.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(Constants.WARD_OBJECT, CommonConstants.CODE,
					ward.getCode(), tenantId, requestInfo, Constants.COMMON_MASTERS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(Constants.KEY_WARD_INVALID, Constants.MESSAGE_WARD_INVALID);
			}
		} else
			messages.put(Constants.KEY_WARD_CODE_REQUIRED, Constants.MESSAGE_WARD_CODE_REQUIRED);
	}
	
	public void validateLocality(Boundary locality, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (locality != null && locality.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(Constants.LOCALITY_OBJECT, CommonConstants.CODE,
					locality.getCode(), tenantId, requestInfo, Constants.COMMON_MASTERS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(Constants.KEY_LOCALITY_INVALID, Constants.MESSAGE_LOCALITY_INVALID);
			}
		} else
			messages.put(Constants.KEY_LOCALITY_CODE_REQUIRED, Constants.MESSAGE_LOCALITY_CODE_REQUIRED);
	}

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        for (DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            AbstractEstimate abstactEstimate = searchAbstractEstimate(detailedEstimate);
            if(abstactEstimate == null)
                messages.put(Constants.KEY_INVALID_ABSTRACTESTIMATE_DETAILS,
                        Constants.MESSAGE_INVALID_ABSTRACTESTIMATE_DETAILS);
            validateMasterData(detailedEstimate, requestInfo, messages);
            validateEstimateAdminSanction(detailedEstimate, messages, abstactEstimate);
            validateSpillOverEstimate(detailedEstimate, messages, abstactEstimate);
            validateActivities(detailedEstimate, messages, requestInfo);
            validateLocationDetails(detailedEstimate, requestInfo, messages);
            validateAssetDetails(detailedEstimate, requestInfo, messages, abstactEstimate);
            validateOverheads(detailedEstimate, requestInfo, messages);
            validateDocuments(detailedEstimate, requestInfo, messages);


        }
        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateDocuments(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String, String> messages) {
        if(detailedEstimate.getDocumentDetails() != null) {
        for(DocumentDetail documentDetail : detailedEstimate.getDocumentDetails()) {
            boolean fileExists = fileStoreRepository.searchFileStore(detailedEstimate.getTenantId(), documentDetail.getFileStore(), requestInfo);
            if(!fileExists)
                messages.put(Constants.KEY_INVALID_FILESTORE_ID, Constants.MESSAGE_INVALID_FILESTORE_ID);
         }
        }
    }

    private void validateEstimateAdminSanction(DetailedEstimate detailedEstimate, Map<String, String> messages,AbstractEstimate abstractEstimate) {
                if (abstractEstimate != null && detailedEstimate.getEstimateDate() != null && detailedEstimate.getEstimateDate() < abstractEstimate.getAdminSanctionDate()) {
                    messages.put(Constants.KEY_INVALID_ADMINSANCTION_DATE, Constants.MESSAGE_INVALID_ADMINSANCTION_DATE);
                }

    }

    public AbstractEstimate searchAbstractEstimate(DetailedEstimate detailedEstimate) {
        AbstractEstimate abstractEstimate = null;
        if(detailedEstimate.getAbstractEstimateDetail() != null) {
            String workIdentificationNumber = detailedEstimate.getAbstractEstimateDetail().getProjectCode().getCode();
            AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                        .workIdentificationNumbers(Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getProjectCode().getCode())).build();
                List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
                if (!abstractEstimates.isEmpty()) {
                    abstractEstimate = abstractEstimates.get(0);
                    for(AbstractEstimateDetails abstractEstimateDetails : abstractEstimate.getAbstractEstimateDetails()) {
                        if(abstractEstimateDetails.getProjectCode() != null && abstractEstimateDetails.getProjectCode().getCode().equalsIgnoreCase(workIdentificationNumber))
                            abstractEstimate.setAbstractEstimateDetails(Arrays.asList(abstractEstimateDetails));
                            return abstractEstimate;
                    }
                }
        }
        return null;
    }

    public void validateSpillOverEstimate(final DetailedEstimate detailedEstimate, Map<String, String> messages, AbstractEstimate abstractEstimate) {
        if(abstractEstimate != null && abstractEstimate.getDetailedEstimateCreated()) {
            if(StringUtils.isBlank(detailedEstimate.getEstimateNumber()))
                messages.put(Constants.KEY_NULL_DETAILEDESTIMATE_NUMBER,
                        Constants.MESSAGE_NULL_DETAILEDESTIMATE_NUMBER);
            else
               validateEstimateNumberUnique(detailedEstimate, messages);

           if(detailedEstimate.getEstimateDate() != null && detailedEstimate.getEstimateDate() > new Date().getTime())
                messages.put(Constants.KEY_FUTUREDATE_ESTIMATEDATE_SPILLOVER,
                        Constants.MESSAGE_FUTUREDATE_ESTIMATEDATE_SPILLOVER);
            validateTechnicalSanctionDetail(detailedEstimate, messages);
        }
    }

    private void validateEstimateNumberUnique(DetailedEstimate detailedEstimate, Map<String, String> messages) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                .tenantId(detailedEstimate.getTenantId()).estimateAmount(detailedEstimate.getEstimateNumber()).build();
        if(detailedEstimate.getId() != null)
            detailedEstimateSearchContract.setIds(Arrays.asList(detailedEstimate.getId()));
        List<DetailedEstimateHelper> lists= detailedEstimateJdbcRepository.search(detailedEstimateSearchContract);
        if(lists != null && !lists.isEmpty())
            messages.put(Constants.KEY_INVALID_ESTIMATNUMBER_SPILLOVER,
                    Constants.MESSAGE_INVALID_ESTIMATNUMBER_SPILLOVER);
    }

    public void validateOverheads(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages) {
        for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {

            if (estimateOverhead != null) {
                validateEstimateOverHead(estimateOverhead.getOverhead(),requestInfo,messages);

                if (estimateOverhead.getOverhead().getCode() == null) {
                    messages.put(Constants.KEY_ESIMATE_OVERHEAD_CODE, Constants.MESSAGE_ESIMATE_OVERHEAD_CODE);
                }
                if (estimateOverhead.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    messages.put(Constants.KEY_ESIMATE_OVERHEAD_AMOUNT, Constants.MESSAGE_ESIMATE_OVERHEAD_AMOUNT);
                }

            }
        }
    }

    private void validateEstimateOverHead(Overhead overhead, RequestInfo requestInfo, Map<String, String> messages) {
        if (overhead != null && StringUtils.isNotBlank(overhead.getCode())) {
            JSONArray  responseJSONArray = estimateUtils.getMDMSData(Constants.OVERHEAD_OBJECT,
                    CommonConstants.CODE, overhead.getCode(), overhead.getTenantId(), requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_ESTIMATE_OVERHEAD_CODE_INVALID, Constants.MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID);
            }
        }
    }

    public void validateActivities(final DetailedEstimate detailedEstimate, Map<String, String> messages, final RequestInfo requestInfo) {
    
        if(detailedEstimate.getWorkFlowDetails() != null && detailedEstimate.getWorkFlowDetails().getAction().equalsIgnoreCase(CommonConstants.WORKFLOW_ACTION_CREATE))
          if (detailedEstimate.getEstimateActivities() == null || detailedEstimate.getEstimateActivities().isEmpty())
              messages.put(Constants.KEY_ESTIMATE_ACTIVITY_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

        ScheduleOfRate sor = null;
        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
            if ((activity.getScheduleOfRate() != null && activity.getScheduleOfRate().getId() == null) || (activity.getNonSor() != null && activity.getNonSor().getId() == null))
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

            if (activity.getScheduleOfRate() == null && activity.getNonSor() == null)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_INVALID, Constants.MESSAGE_ESTIMATE_ACTIVITY_INVALID);
           //validate at pattern level
            if (activity.getQuantity() != null && activity.getQuantity() <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_QUANTITY, Constants.MESSAGE_ESTIMATE_ACTIVITY_QUANTITY);

             if (activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE, Constants.MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE);

            validateUOM(activity.getUom(), detailedEstimate.getTenantId(), requestInfo,messages);

           if (activity.getUnitRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID, Constants.MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID);

            if (activity.getScheduleOfRate() != null && StringUtils.isNotBlank(activity.getScheduleOfRate().getId())) {
                List<ScheduleOfRate> scheduleOfRates = worksMastersRepository.searchScheduleOfRates(activity.getTenantId(), Arrays.asList(activity.getScheduleOfRate().getId()), requestInfo);
                if (scheduleOfRates != null && scheduleOfRates.isEmpty())
                    messages.put(Constants.KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID, Constants.MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID);
                else if(sor != null && sor.getId().equals(activity.getScheduleOfRate().getId()))
                   messages.put(Constants.KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE, Constants.MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE);
            }

            if (activity.getEstimateMeasurementSheets() != null)
                for (final EstimateMeasurementSheet estimateMeasurementSheet : activity.getEstimateMeasurementSheets()) {
                    if(StringUtils.isBlank(estimateMeasurementSheet.getIdentifier()))
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED);
                    if (estimateMeasurementSheet.getQuantity() == null)
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED);
                    else if (estimateMeasurementSheet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID, Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID);
                }



        }

    }

    private void validateUOM(final UOM uom, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (uom != null && uom.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.UOM_OBJECT,
                    CommonConstants.CODE, uom.getCode(), tenantId, requestInfo,
                    Constants.COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_UOM_INVALID,
                        Constants.MESSAGE_UOM_INVALID);
            }
        }
    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT, CommonConstants.CODE, Constants.GIS_INTEGRATION_APPCONFIG,
                    detailedEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty()) {
                Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                if (jsonMap.get("value").equals("Yes") && (StringUtils.isBlank(detailedEstimate.getLocation())
                        || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
                    messages.put(Constants.KEY_ESTIMATE_LOCATION_REQUIRED, Constants.MESSAGE_ESTIMATE_LOCATION_REQUIRED);
            }
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages, AbstractEstimate abstractEstimate) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT, CommonConstants.CODE, Constants.ASSET_DETAILES_REQUIRED_APPCONFIG,
                detailedEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        boolean assetsAdded = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes")) {
                if (detailedEstimate.getAssets() != null && detailedEstimate.getAssets().isEmpty())
                    assetsAdded = true;

                for (AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
                    if (!assetsAdded && StringUtils.isBlank(assetsForEstimate.getLandAsset()))
                        messages.put(Constants.KEY_ESTIMATE_ASSET_DETAILS_REQUIRED, Constants.MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
            }
        }

        Asset asset = null;
        for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
            if (assetsForEstimate != null) {
                if (detailedEstimate.getNameOfWork() != null && detailedEstimate.getNatureOfWork().getExpenditureType() != null) {
                     mdmsArray = estimateUtils.getMDMSData(Constants.NATUREOFWORK_OBJECT, CommonConstants.EXPENDITURETYPE, detailedEstimate.getNatureOfWork().getExpenditureType().toString(),
                            detailedEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
                    if (mdmsArray != null && !mdmsArray.isEmpty())  {
                        Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                        if(jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.REVENUE.toString()) && assetsForEstimate.getAsset() == null)
                          messages.put(Constants.KEY_ESTIMATE_ASSET_REQUIRED, Constants.MESSAGE_ESTIMATE_ASSET_REQUIRED);

                        else if(jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.CAPITAL.toString()) && StringUtils.isBlank(assetsForEstimate.getLandAsset())
                                && abstractEstimate != null && abstractEstimate.getLandAssetRequired() != null && abstractEstimate.getLandAssetRequired())
                            messages.put(Constants.KEY_ESTIMATE_LAND_ASSET_REQUIRED, Constants.MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED);
                    }

                }


                //TODO FIX aset code validation
               /* else {
                    List<Asset> assets = assetRepository.searchAssets(assetsForEstimate.getTenantId(),assetsForEstimate.getAsset().getCode(),requestInfo);
                    if(assets != null && assets.isEmpty())
                        messages.put(Constants.KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID, Constants.MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID);
                }*/
                if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                    messages.put(Constants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS, Constants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
                asset = assetsForEstimate.getAsset();
            }
    }
    
    public void validateEstimateAssetDetails(final AbstractEstimate abstractEstimate, final RequestInfo requestInfo, Map<String, String> messages) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT, CommonConstants.CODE, Constants.ASSET_DETAILES_REQUIRED_APPCONFIG,
                abstractEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        boolean assetsAdded = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                if(abstractEstimate.getAssetDetails() != null && !abstractEstimate.getAssetDetails().isEmpty())
                    assetsAdded = true;

                for(AbstractEstimateAssetDetail abstractEstimateAssetDetail : abstractEstimate.getAssetDetails())
                  if(!assetsAdded && StringUtils.isBlank(abstractEstimateAssetDetail.getLandAsset()))
                    messages.put(Constants.KEY_ESTIMATE_ASSET_DETAILS_REQUIRED, Constants.MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
        }

        Asset asset = null;
        for (AbstractEstimateAssetDetail abstractEstimateAssetDetail : abstractEstimate.getAssetDetails())
            if (abstractEstimateAssetDetail != null) {
                if (abstractEstimate.getNatureOfWork() != null &&
                        (abstractEstimate.getNatureOfWork().getCode().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_ADDITION) ||
                                abstractEstimate.getNatureOfWork().getCode().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_REPAIRS))
                        && StringUtils.isBlank(abstractEstimateAssetDetail.getAsset().getCode()))
                    messages.put(Constants.KEY_ESTIMATE_ASSET_REQUIRED, Constants.MESSAGE_ESTIMATE_ASSET_REQUIRED);

                if(abstractEstimate.getNatureOfWork() != null && abstractEstimate.getNatureOfWork().getCode().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_NEW)
                  && StringUtils.isBlank(abstractEstimateAssetDetail.getLandAsset()))
                    messages.put(Constants.KEY_ESTIMATE_LAND_ASSET_REQUIRED, Constants.MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED);
                //TODO FIX aset code validation
               /* else {
                    List<Asset> assets = assetRepository.searchAssets(assetsForEstimate.getTenantId(),assetsForEstimate.getAsset().getCode(),requestInfo);
                    if(assets != null && assets.isEmpty())
                        messages.put(Constants.KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID, Constants.MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID);
                }*/
                if (asset != null && asset.getCode().equals(abstractEstimateAssetDetail.getAsset().getCode()))
                    messages.put(Constants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS, Constants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
            }
    }


    public void validateTechnicalSanctionDetail(final DetailedEstimate detailedEstimate, Map<String,String> messages) {
        if(detailedEstimate.getEstimateTechnicalSanctions() == null ||
                detailedEstimate.getEstimateTechnicalSanctions() != null && detailedEstimate.getEstimateTechnicalSanctions().isEmpty()) {
            messages.put(Constants.KEY_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED, Constants.MESSAGE_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED);
        }

        if(detailedEstimate.getEstimateTechnicalSanctions() != null )
            for(EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate.getEstimateTechnicalSanctions()) {

                if(StringUtils.isNotBlank(estimateTechnicalSanction.getTechnicalSanctionNumber()))
                   validateUniqueTechnicalSanctionForDetailedEstimate(detailedEstimate, estimateTechnicalSanction, messages);

                if(estimateTechnicalSanction.getTechnicalSanctionDate() != null && estimateTechnicalSanction.getTechnicalSanctionDate() > new Date().getTime())
                    messages.put(Constants.KEY_TECHNICAL_SANCTION_DATE_FUTUREDATE, Constants.MESSAGE_TECHNICAL_SANCTION_DATE_FUTUREDATE);

                if(detailedEstimate.getEstimateDate() != null && estimateTechnicalSanction.getTechnicalSanctionDate() != null && estimateTechnicalSanction.getTechnicalSanctionDate() < detailedEstimate.getEstimateDate())
                    messages.put(Constants.KEY_INVALID_TECHNICALSANCTION_DATE, Constants.MESSAGE_INVALID_TECHNICALSANCTION_DATE);
            }

    }

    private void validateUniqueTechnicalSanctionForDetailedEstimate(DetailedEstimate detailedEstimate, EstimateTechnicalSanction estimateTechnicalSanction, Map<String, String> messages) {
        TechnicalSanctionSearchContract technicalSanctionSearchContract = TechnicalSanctionSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                .technicalSanctionNumbers(Arrays.asList(estimateTechnicalSanction.getTechnicalSanctionNumber())).build();
        if(estimateTechnicalSanction.getId() != null) {
            technicalSanctionSearchContract.setIds(Arrays.asList(estimateTechnicalSanction.getId()));
            technicalSanctionSearchContract.setDetailedEstimateIds(Arrays.asList(detailedEstimate.getId()));
        }
        List<EstimateTechnicalSanction> technicalSanctions = estimateTechnicalSanctionRepository.search(technicalSanctionSearchContract);
        if(technicalSanctions != null && !technicalSanctions.isEmpty())
            messages.put(Constants.KEY_INVALID_ADMINSANCTION_DATE, Constants.MESSAGE_INVALID_ADMINSANCTION_DATE);
    }

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray = null;

        responseJSONArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT,
                CommonConstants.CODE, Constants.FINANCIAL_INTEGRATION_KEY,
                detailedEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                validateFinancialDetailsForDetailedEstmate(detailedEstimate, requestInfo, messages);
        }

        validateModeOfAllotment(detailedEstimate.getModeOfAllotment(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateNatureOfWork(detailedEstimate.getNatureOfWork(), detailedEstimate.getTenantId(), requestInfo, messages);
        //MDMS data not found
        //validateWardBoundary(detailedEstimate.getWard(), detailedEstimate.getTenantId(), requestInfo, messages);
       // validateLocalityBoundary(detailedEstimate.getLocality(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateTypeOfWork(detailedEstimate.getWorksType(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(detailedEstimate.getWorksSubtype(), detailedEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(detailedEstimate.getDepartment(), detailedEstimate.getTenantId(), requestInfo, messages);
    }


    private void validateNatureOfWork(NatureOfWork natureOfWork, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (natureOfWork != null && natureOfWork.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.NATUREOFWORK_OBJECT,
                    CommonConstants.CODE, natureOfWork.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_NATUREOFWORK_INVALID,
                        Constants.MESSAGE_NATUREOFWORK_INVALID);
            }
            else {
                Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
                if(jsonMap.get("expenditureType").equals(ExpenditureType.CAPITAL.toString()))
                  natureOfWork.setExpenditureType(ExpenditureType.CAPITAL);
                else
                  natureOfWork.setExpenditureType(ExpenditureType.REVENUE);
            }
        } else
            messages.put(Constants.KEY_NATUREOFWORK_REQUIRED,
                    Constants.MESSAGE_NATUREOFWORK_REQUIRED);
    }

    private void validateFinancialDetailsForDetailedEstmate(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
                                                            Map<String, String> messages) {
        validateFund(detailedEstimate.getFund(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateFunction(detailedEstimate.getFunction(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateScheme(detailedEstimate.getScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(detailedEstimate.getSubScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateBudgetGroup(detailedEstimate.getBudgetGroup(), detailedEstimate.getTenantId(), requestInfo, messages);
    }

    public boolean checkDetailedEstimateCreatedFlag(final DetailedEstimate detailedEstimate) {
        boolean isDetailedEstimateCreated = false;
        AbstractEstimateDetailsSearchContract searchContract = AbstractEstimateDetailsSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                .ids(Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getId())).build();
        List<AbstractEstimateDetails> abstractEstimateDetails = abstractEstimateDetailsJdbcRepository.search(searchContract);
        if (!abstractEstimateDetails.isEmpty()) {
            String abstractEstimateId = abstractEstimateDetails.get(0).getAbstractEstimate();
            AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                    .ids(Arrays.asList(abstractEstimateId)).build();
            List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
            if (!abstractEstimates.isEmpty()) {
                AbstractEstimate abstractEstimate = abstractEstimates.get(0);
                isDetailedEstimateCreated = abstractEstimate.getDetailedEstimateCreated();
            }

        }
        return isDetailedEstimateCreated;
    }
}

