package com.vocalink.crossproduct.component.rest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import javax.annotation.PostConstruct;

@Component
@Profile("component")
public class PathResolver {

    @Value("${test.iss.paths}")
    private String issPathsFile;

    @Value("${test.bps.paths}")
    private String bpsPathsFile;;

    private URLs issUrLs;

    private URLs bpsUrLs;

    @PostConstruct
    private void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        issUrLs = mapper.readValue(new File(issPathsFile), URLs.class);
        bpsUrLs = mapper.readValue(new File(bpsPathsFile), URLs.class);
    }

    public String getIssPath(String name) {
        return issUrLs.getPaths().stream()
                   .filter(path -> path.getName().contentEquals(name))
                   .findFirst().get().getPath();
    }

    public String getBpsPath(String name) {
        return bpsUrLs.getPaths().stream()
                      .filter(path -> path.getName().contentEquals(name))
                      .findFirst().get().getPath();
    }

}
