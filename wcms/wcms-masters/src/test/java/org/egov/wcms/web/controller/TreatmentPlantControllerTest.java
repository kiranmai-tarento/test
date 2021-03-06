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
package org.egov.wcms.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.service.TreatmentPlantService;
import org.egov.wcms.util.FileUtils;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TreatmentPlantController.class)
@Import(TestConfiguration.class)
public class TreatmentPlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreatmentPlantService treatmentPlantService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ValidatorUtils validatorUtils;

    @InjectMocks
    private TreatmentPlantController treatmentPlantController;

    @Test(expected = Exception.class)
    public void test_Should_Search_TreatmentPlant() throws Exception {

        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        final RequestInfo requestInfo = new RequestInfo();
        final ResponseInfo responseInfo = new ResponseInfo();
        final TreatmentPlant treatmentPlant = new TreatmentPlant();
        treatmentPlant.setTenantId("default");
        treatmentPlant.setName("test");
        treatmentPlant.setCode("12");
        treatmentPlant.setLocation("test");
        treatmentPlant.setCapacity(2d);
        treatmentPlant.setPlantType("test");
        treatmentPlant.setStorageReservoirId(2l);
        treatmentPlant.setPlantType("abcd");
        treatmentPlantList.add(treatmentPlant);

        final TreatmentPlantGetRequest treatmentPlantGetRequest = Mockito.mock(TreatmentPlantGetRequest.class);

        when(treatmentPlantService.getTreatmentPlant(treatmentPlantGetRequest)).thenReturn(treatmentPlantList);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);

        mockMvc.perform(post("/treatmentplants/_search").param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("treatplantresponse.json")));
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }

}
