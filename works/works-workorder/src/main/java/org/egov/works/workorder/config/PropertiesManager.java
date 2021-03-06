package org.egov.works.workorder.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ramki on 11/11/17.
 */
@Configuration
@Getter
@ToString
public class PropertiesManager {

    @Value("${app.timezone}")
    private String appTimeZone;

    @Value("${egov.services.works.workorder.pageSize.default}")
    private String pageSize;

    @Value("${egov.services.works.workorder.pageNumber.default}")
    private String pageNumber;

    @Value("${egov.services.works.workorder.pagesize.max}")
    private String pageSizeMax;

}
