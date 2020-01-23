package com.download.cad.crawler;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class CrawlerProperties {
    private String productFile;
    private String outputFolder;
    private String user;
    private String name;
    private String zip;
    private boolean downloadFile;

    public boolean isInitialized() {
        return !StringUtils.isEmpty(productFile) || !StringUtils.isEmpty(outputFolder) ||
                !StringUtils.isEmpty(user) || !StringUtils.isEmpty(name) || !StringUtils.isEmpty(zip);
    }
}
