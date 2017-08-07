package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class BudgetDetailEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_budgetdetail";
    private String id;
    private String budgetId;
    private String budgetGroupId;
    private String usingDepartmentId;
    private String executingDepartmentId;
    private String fundId;
    private String functionId;
    private String schemeId;
    private String subSchemeId;
    private String functionaryId;
    private String boundaryId;
    private BigDecimal anticipatoryAmount;
    private BigDecimal originalAmount;
    private BigDecimal approvedAmount;
    private BigDecimal planningPercent;
    private BigDecimal budgetAvailable;
    private String statusId;
    private String documentNumber;
    private String uniqueNo;
    private String materializedPath;

    public BudgetDetail toDomain() {
        final BudgetDetail budgetDetail = new BudgetDetail();
        super.toDomain(budgetDetail);
        budgetDetail.setId(id);
        budgetDetail.setBudget(Budget.builder().id(budgetId).build());
        budgetDetail.setBudgetGroup(BudgetGroupContract.builder().id(budgetGroupId).build());
        budgetDetail.setUsingDepartment(Department.builder().id(usingDepartmentId).build());
        budgetDetail.setExecutingDepartment(Department.builder().id(executingDepartmentId).build());
        budgetDetail.setFund(FundContract.builder().id(fundId).build());
        budgetDetail.setFunction(FunctionContract.builder().id(functionId).build());
        budgetDetail.setScheme(SchemeContract.builder().id(schemeId).build());
        budgetDetail.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
        budgetDetail.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
        budgetDetail.setBoundary(Boundary.builder().id(boundaryId).build());
        budgetDetail.setAnticipatoryAmount(anticipatoryAmount);
        budgetDetail.setOriginalAmount(originalAmount);
        budgetDetail.setApprovedAmount(approvedAmount);
        budgetDetail.setPlanningPercent(planningPercent);
        budgetDetail.setBudgetAvailable(budgetAvailable);
        budgetDetail.setStatus(FinancialStatusContract.builder().id(statusId).build());
        budgetDetail.setDocumentNumber(documentNumber);
        budgetDetail.setUniqueNo(uniqueNo);
        budgetDetail.setMaterializedPath(materializedPath);
        return budgetDetail;
    }

    public BudgetDetailEntity toEntity(final BudgetDetail budgetDetail) {
        super.toEntity(budgetDetail);
        id = budgetDetail.getId();
        budgetId = budgetDetail.getBudget() != null ? budgetDetail.getBudget().getId() : null;
        budgetGroupId = budgetDetail.getBudgetGroup() != null ? budgetDetail.getBudgetGroup().getId() : null;
        usingDepartmentId = budgetDetail.getUsingDepartment() != null ? budgetDetail.getUsingDepartment().getId()
                : null;
        executingDepartmentId = budgetDetail.getExecutingDepartment() != null
                ? budgetDetail.getExecutingDepartment().getId() : null;
        fundId = budgetDetail.getFund() != null ? budgetDetail.getFund().getId() : null;
        functionId = budgetDetail.getFunction() != null ? budgetDetail.getFunction().getId() : null;
        schemeId = budgetDetail.getScheme() != null ? budgetDetail.getScheme().getId() : null;
        subSchemeId = budgetDetail.getSubScheme() != null ? budgetDetail.getSubScheme().getId() : null;
        functionaryId = budgetDetail.getFunctionary() != null ? budgetDetail.getFunctionary().getId() : null;
        boundaryId = budgetDetail.getBoundary() != null ? budgetDetail.getBoundary().getId() : null;
        anticipatoryAmount = budgetDetail.getAnticipatoryAmount();
        originalAmount = budgetDetail.getOriginalAmount();
        approvedAmount = budgetDetail.getApprovedAmount();
        planningPercent = budgetDetail.getPlanningPercent();
        budgetAvailable = budgetDetail.getBudgetAvailable();
        statusId = budgetDetail.getStatus() != null ? budgetDetail.getStatus().getId() : null;
        documentNumber = budgetDetail.getDocumentNumber();
        uniqueNo = budgetDetail.getUniqueNo();
        materializedPath = budgetDetail.getMaterializedPath();
        return this;
    }

}
