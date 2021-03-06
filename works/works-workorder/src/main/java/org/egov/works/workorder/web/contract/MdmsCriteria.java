package org.egov.works.workorder.web.contract;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MdmsCriteria {

	private String tenantId;

	private ModuleDetails[] moduleDetails;

}
