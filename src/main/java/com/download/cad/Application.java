package com.download.cad;

import com.download.cad.crawler.CadResponse;
import com.download.cad.crawler.CrawlerProperties;
import com.download.cad.crawler.DocumentRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        logger.info("Application Started... {}", args);
        DocumentRetriever documentRetriever = null;
        CrawlerProperties crawlerProperties = new CrawlerProperties();
        if(args.length > 0) {
            logger.info("Crawlier Properties provided in arguments");
            for (int i = 0; i < args.length; i+=2) {
                logger.info("Parsing argument {}", args[i]);
                if(args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("-productFile")) {
                    logger.info("Parsing argument Product File {}", args[i+1]);
                    crawlerProperties.setProductFile(args[i+1]);
                } else if(args[i].equalsIgnoreCase("-o") || args[i].equalsIgnoreCase("-outputFolder")) {
                    logger.info("Parsing argument Output Folder {}", args[i+1]);
                    crawlerProperties.setOutputFolder(args[i+1]);
                } else if(args[i].equalsIgnoreCase("-n") || args[i].equalsIgnoreCase("-name")) {
                    logger.info("Parsing argument Name {}", args[i+1]);
                    crawlerProperties.setName(args[i+1]);
                } else if(args[i].equalsIgnoreCase("-u") || args[i].equalsIgnoreCase("-user")) {
                    logger.info("Parsing argument User {}", args[i+1]);
                    crawlerProperties.setUser(args[i+1]);
                } else if(args[i].equalsIgnoreCase("-z") || args[i].equalsIgnoreCase("-zip")) {
                    logger.info("Parsing argument Zip {}", args[i+1]);
                    crawlerProperties.setZip(args[i+1]);
                } else if(args[i].equalsIgnoreCase("-d") || args[i].equalsIgnoreCase("-download")) {
                    logger.info("Parsing argument Download {}", args[i+1]);
                    crawlerProperties.setDownloadFile(Boolean.parseBoolean(args[i+1]));
                }
            }
        }
        if(crawlerProperties.isInitialized()) {
            documentRetriever = new DocumentRetriever(crawlerProperties);
        } else {
            logger.warn("Usage Application -p <ProductFileWithProductUrlSeparatedByLine> -o <OutputFolder> -d <true/false for downloading file> -u <user> -n <name> -z <zip>");
            return;
            //documentRetriever = new DocumentRetriever();
        }
        Map<String, CadResponse> cadResponseMap = documentRetriever.getCadResponses();
        logger.info("Response Details");
        logger.info("................................................................................................");
        logger.info("productId | cadAvailable | url | fileName | downloadCompleted");
        cadResponseMap.forEach((s, r) -> logger.info("{} | {} | {} | {} | {}",r.getProductId(), r.isCadAvailable(), r.getUrl(), r.getFileName(), r.isDownloadCompleted()));
        logger.info("................................................................................................");
        logger.info("Application Completed...");
    }
}
