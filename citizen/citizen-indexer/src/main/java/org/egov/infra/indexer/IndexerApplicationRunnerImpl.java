package org.egov.infra.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;

import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Component
@Order(1)
public class IndexerApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public static ResourceLoader resourceLoader;
	    
    @Value("${egov.indexer.file.path}")
    private String yamllistfile;
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerApplicationRunnerImpl.class);

    public static ConcurrentHashMap<String, Mapping> mappingMaps  = new ConcurrentHashMap<>();
	
    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
    	try {
				logger.info("Reading yaml files......");			
			    readFiles();			
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
    }
       
    public void readFiles(){
    	ConcurrentHashMap<String, Mapping> mappingsMap = new ConcurrentHashMap<>();
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Services service = null;
		try {
			URL url = new URL(yamllistfile);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			try{
				String yamlLocation;
				while(null != (yamlLocation = bufferedReader.readLine())){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						logger.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
						    service = mapper.readValue(new InputStreamReader(yamlFile.openStream()), Services.class);
						} catch(Exception e) {
							logger.error("Exception while fetching service map for: "+yamlLocation+" = ",e);
							continue;
						}
						logger.info("Parsed to object: "+service);
						for(Mapping mapping: (service.getServiceMaps().getMappings())){
							mappingsMap.put(mapping.getTopic(), mapping);
						}
						
					} else if(yamlLocation.startsWith("file://")){
						logger.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								service = mapper.readValue(file, Services.class);
							 } catch(Exception e) {
									logger.error("Exception while fetching service map for: "+yamlLocation);
									continue;
							}
							logger.info("Parsed to object: "+service);
							for(Mapping mapping: (service.getServiceMaps().getMappings())){
								mappingsMap.put(mapping.getTopic(), mapping);
							}
					}
				}
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
		} catch (Exception e) {
			logger.error("Exception while loading file containing yaml locations: ",e);
		}
		//validateServiceMaps(mappingsMap);
		mappingMaps = mappingsMap;
    }
    
    public void validateServiceMaps(ConcurrentHashMap<String, Mapping> mappingsMap){
    	
    }

	public ConcurrentHashMap<String, Mapping> getMappingMaps(){
		return mappingMaps;
	}
}
