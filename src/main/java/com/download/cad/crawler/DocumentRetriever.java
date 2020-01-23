package com.download.cad.crawler;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.io.*;
import java.util.*;

public class DocumentRetriever {
    private static Logger logger = LoggerFactory.getLogger(DocumentRetriever.class);
    private List<String> productList = Arrays.asList("https://www.bbman.com/catalog/product/10MP012-6CA1", "https://www.bbman.com/catalog/product/25A18");
    private String outputFolder = "/Users/gobi/projects/web_scraping/cds_download/src/main/resources/output_files";
    private String origin = "https://www.bbman.com";
    private String d = "bbman2";
    private String f = "prt";
    private String user = "abc@abc.com";
    private String name = "abc";
    private String zip = "07002";
    private String url = "https://www.product-config.net/catalog3/cad";
    boolean canDownload;

    public DocumentRetriever() {}

    public DocumentRetriever(CrawlerProperties crawlerProperties) {
        initializeCrawlerProperties(crawlerProperties);
    }

    private void initializeCrawlerProperties(CrawlerProperties crawlerProperties) {
        if(crawlerProperties.getProductFile() != null) {
            logger.info("Initialize Product List from file {}", crawlerProperties.getProductFile());
            try (BufferedReader reader = new BufferedReader(new FileReader(crawlerProperties.getProductFile()))) {
                String line = null;
                List<String> products = new ArrayList<>();
                while ((line = reader.readLine())!= null) {
                    products.add(line);
                }
                productList = products;
            } catch (IOException e) {
                logger.error("IO Exception occured", e);
                throw new RuntimeException("File Not Found to read products "+ crawlerProperties.getProductFile(), e);
            }
            logger.info("Product List initialized");
        }
        if(crawlerProperties.getOutputFolder() != null) {
            this.outputFolder = crawlerProperties.getOutputFolder();
        }
        if(crawlerProperties.getName() != null) {
            this.name = crawlerProperties.getName();
        }
        if(crawlerProperties.getUser() != null) {
            this.user = crawlerProperties.getUser();
        }
        if(crawlerProperties.getZip() != null) {
            this.zip = crawlerProperties.getZip();
        }
        if(crawlerProperties.isDownloadFile()) {
            this.canDownload = crawlerProperties.isDownloadFile();
        }
    }

    public Map<String, CadResponse> getCadResponses() {
        logger.info("Started Parsing");
        Map<String, CadResponse> cadResponseMap = new TreeMap<>();
        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        for(String product : productList) {
            logger.info("Processing Product {}", product);
            String id = null;
            if(product.lastIndexOf("/product/") > 0) {
                id = product.substring(product.lastIndexOf("/") + 1);
            }
            if(id != null) {
                try {
                    logger.info("Pull Product {}", id);
                    Form formData = Form.form().add("d", d).add("id", id).add("f", f).
                            add("cds-user", user).add("cds-fname", name).
                            add("cds-lname", name).add("cds-company", name).
                            add("cds-zip", zip);
                    Response response = Request.Post(url).addHeader("Origin", origin).
                            addHeader("Referer", product).bodyForm(formData.build()).execute();
                    Content content = response.returnContent();
                    logger.info("Content Response Type {}", content.getType());
                    logger.info("Content Response {}", content.asString());
                    Map<String, Object> jsonContent = jsonParser.parseMap(content.asString());
                    logger.info("Json Content {}", jsonContent);
                    if(id.equals(jsonContent.get("productID"))) {
                        CadResponse cadResponse = new CadResponse(jsonContent);
                        cadResponseMap.put(product, cadResponse);
                        if(canDownload && cadResponse.getUrl() != null) {
                            logger.info("Downloading {} for product {}", cadResponse.getUrl(), cadResponse.getProductId());
                            File file = new File(outputFolder + "/" + cadResponse.getFileName());
                            Request.Get(cadResponse.getUrl()).execute().saveContent(file);
                            cadResponse.setDownloadCompleted(true);
                            logger.info("Download Completed for product {}", cadResponse.getProductId());
                        }
                    }
                    Thread.sleep(1000); //sleeping for a second
                } catch (Exception e) {
                    logger.error("Error Occured while retrieving for product "+ product, e);
                }
            } else {
                logger.warn("Product url not in the correct. Expected ..../product/id {}", product);
            }
        }
        return cadResponseMap;
    }
}
