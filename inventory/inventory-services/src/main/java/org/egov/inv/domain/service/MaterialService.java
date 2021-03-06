package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.MaterialEntity;
import org.egov.inv.persistence.repository.MaterialJdbcRepository;
import org.egov.inv.persistence.repository.MaterialRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialService extends DomainService {

    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialJdbcRepository materialJdbcRepository;

    @Autowired
    private MaterialStoreMappingService materialStoreMappingService;

    @Value("${inv.materials.save.topic}")
    private String saveTopic;

    @Value("${inv.materials.save.key}")
    private String saveKey;

    @Value("${inv.materials.update.topic}")
    private String updateTopic;

    @Value("${inv.materials.update.key}")
    private String updateKey;

    public static final String SEQ_SERIAL_NO = "seq_material_code_serial_no";

    public MaterialResponse save(MaterialRequest materialRequest, String tenantId) {

        try {
            List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

            validate(materialRequest.getMaterials(), CREATE);

            List<String> materialIdList = materialJdbcRepository.getSequence(Material.class.getSimpleName(), materialRequest.getMaterials().size());

            for (int i = 0; i <= materialIdList.size() - 1; i++) {
                materialRequest.getMaterials().get(i)
                        .setId(materialIdList.get(i).toString());
                materialRequest.getMaterials().get(i)
                        .setCode(materialRequest.getMaterials().get(i).getMaterialType().getCode() + "/" + materialJdbcRepository.getSequence(SEQ_SERIAL_NO));
                if (isEmpty(materialRequest.getMaterials().get(i).getTenantId())) {
                    materialRequest.getMaterials().get(i).setTenantId(tenantId);
                }
                materialRequest.getMaterials().get(i).setAuditDetails(mapAuditDetails(materialRequest.getRequestInfo()));
                materialStoreMappings = buildMaterialStoreMapping(materialRequest.getMaterials().get(i).getCode(), materialRequest.getMaterials().get(i).getStoreMapping());
                uniqueCheck(materialRequest.getMaterials().get(i));
                validateAsset(materialRequest.getMaterials().get(i));
            }

            kafkaQue.send(saveTopic, saveKey, materialRequest);

            materialStoreMappingService.create(buildMaterialStoreRequest(materialRequest.getRequestInfo(), materialStoreMappings), tenantId);

            MaterialResponse response = MaterialResponse.builder()
                    .materials(materialRequest.getMaterials())
                    .responseInfo(getResponseInfo(materialRequest.getRequestInfo()))
                    .build();

            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

    public MaterialResponse update(MaterialRequest materialRequest, String tenantId) {
        try {
            List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

            validate(materialRequest.getMaterials(), UPDATE);
            for (Material material : materialRequest.getMaterials()) {
                if (isEmpty(material.getTenantId())) {
                    material.setTenantId(tenantId);
                }
                material.setAuditDetails(mapAuditDetails(materialRequest.getRequestInfo()));
                materialStoreMappings = buildMaterialStoreMapping(material.getCode(), material.getStoreMapping());
            }

            kafkaQue.send(updateTopic, updateKey, materialRequest);

            materialStoreMappingService.update(buildMaterialStoreRequest(materialRequest.getRequestInfo(), materialStoreMappings), tenantId);

            MaterialResponse response = new MaterialResponse();
            response.setResponseInfo(getResponseInfo(materialRequest.getRequestInfo()));
            response.setMaterials(materialRequest.getMaterials());
            return response;

        } catch (CustomBindException e) {
            throw e;
        }
    }

    public MaterialResponse search(MaterialSearchRequest materialSearchRequest, org.egov.common.contract.request.RequestInfo requestInfo) {

        Pagination<Material> searchMaterial = materialRepository.search(materialSearchRequest);

        for (Material material : searchMaterial.getPagedData()) {
            List<StoreMapping> storeMappings = new ArrayList<>();

            MaterialStoreMappingSearch materialStoreMappingSearch = MaterialStoreMappingSearch.builder()
                    .material(material.getCode())
                    .tenantId(material.getTenantId())
                    .build();

            List<MaterialStoreMapping> materialStoreMappings = materialStoreMappingService.search(materialStoreMappingSearch, requestInfo).getMaterialStoreMappings();

            materialStoreMappings.forEach(materialStoreMapping -> {
                        StoreMapping storeMapping = StoreMapping.builder()
                                .id(materialStoreMapping.getId())
                                .chartofAccount(materialStoreMapping.getChartofAccount())
                                .active(materialStoreMapping.getActive())
                                .store(materialStoreMapping.getStore())
                                .auditDetails(materialStoreMapping.getAuditDetails())
                                .build();
                        storeMappings.add(storeMapping);
                    }
            );
            material.setStoreMapping(storeMappings);
        }

        MaterialResponse response = new MaterialResponse();
        response.setMaterials(searchMaterial.getPagedData().size() > 0 ? searchMaterial.getPagedData() : Collections.emptyList());
        return response;
    }

    private void validate(List<Material> materials, String method) {

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materials == null) {
                        throw new InvalidDataException("materialstore", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        materials.forEach(material -> {
                            uniqueCheck(material);
                            validateAsset(material);
                        });
                    }
                }


                case Constants.ACTION_UPDATE: {
                    if (materials == null) {
                        throw new InvalidDataException("materialstore", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        materials.forEach(material -> {
                            uniqueCheck(material);
                            validateAsset(material);
                        });
                    }
                }

                break;
            }
        } catch (IllegalArgumentException e) {

        }

    }

    private List<MaterialStoreMapping> buildMaterialStoreMapping(String materialCode, List<StoreMapping> storeMappings) {
        List<MaterialStoreMapping> materialStoreMappings = new ArrayList<>();

        storeMappings.stream().forEach(
                storeMapping -> {
                    MaterialStoreMapping materialStoreMapping = MaterialStoreMapping.builder()
                            .id(storeMapping.getId())
                            .material(buildMaterial(materialCode))
                            .store(storeMapping.getStore())
                            .chartofAccount(storeMapping.getChartofAccount())
                            .active(storeMapping.getActive())
                            .auditDetails(storeMapping.getAuditDetails())
                            .delete(storeMapping.getDelete())
                            .build();

                    materialStoreMappings.add(materialStoreMapping);
                }
        );
        return materialStoreMappings;
    }

    private Material buildMaterial(String materialCode) {
        return Material
                .builder()
                .code(materialCode)
                .build();
    }

    private MaterialStoreMappingRequest buildMaterialStoreRequest(RequestInfo requestInfo,
                                                                  List<MaterialStoreMapping> materialStoreMappings) {
        return MaterialStoreMappingRequest.builder()
                .requestInfo(requestInfo)
                .materialStoreMappings(materialStoreMappings)
                .build();
    }

    private void validateAsset(Material material) {
        if (!isEmpty(material.getInventoryType()) &&
                material.getInventoryType().toString().equalsIgnoreCase("Asset")
                && (null == material.getAssetCategory() ||
                (null != material.getAssetCategory() && isEmpty(material.getAssetCategory().getCode())))) {
            throw new CustomException("inv.0012", "Asset Category is mandatory when Inventory type  is Asset");
        }
    }

    private void uniqueCheck(Material material) {

        if (!materialJdbcRepository.uniqueCheck("name", new MaterialEntity().toEntity(material))) {
            throw new CustomException("inv.010", "material name already exists " + material.getName());
        }
        if (!materialJdbcRepository.uniqueCheck("name", "code", new MaterialEntity().toEntity(material))) {
            throw new CustomException("inv.0011", "Combination of Code and Name Already Exists " + material.getName()
                    + ", " + material.getCode());
        }
    }

}
