package org.egov.works.estimate.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectCodeSearchContract {
	
	@NotNull
	private String tenantId;
	
	private List<String> estimateNumbers;

	private List<String> workIdentificationNumbers;
	
	private String sortBy;
}
