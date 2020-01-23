package com.download.cad.crawler;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class CadResponse {
    /*
        {"productID":"10MP012-6CA1","cadAvailable":true,"cadDownloadAvailable":false,"cad3DViewAvailable":false,"
        cad2DViewAvailable":false,"authenticatedDownload":false,"authenticatedView":false,"authenticatedView2D":false,
        "viewFormat3D":"json","noteKeys":[],"noteURLs":[],"url":"//ms22.product-config.net/cgs/output/42257603/bandbmnfg_10mp012-6ca1.zip"}
     */
    private String productId;
    private boolean cadAvailable;
    private boolean cadDownloadAvailable;
    private boolean cad3DViewAvailable;
    private boolean cad2DViewAvailable;
    private boolean authenticatedDownload;
    private boolean downloadCompleted;
    private String url;
    private String fileName;

    public CadResponse(){}

    public CadResponse(Map<String, Object> jsonContent) {
        productId = getStringValue("productID", jsonContent);
        cadAvailable = getBooleanValue("cadAvailable", jsonContent);
        cadDownloadAvailable = getBooleanValue("cadDownloadAvailable", jsonContent);
        cad3DViewAvailable = getBooleanValue("cad3DViewAvailable", jsonContent);
        authenticatedDownload = getBooleanValue("authenticatedDownload", jsonContent);
        url = getStringValue("url", jsonContent);
        if(url != null) {
            url = "https:" + url;
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
    }

    private String getStringValue(String id, Map<String, Object> jsonContent) {
        if(jsonContent.containsKey(id) && !StringUtils.isEmpty(jsonContent.get(id))) {
            return jsonContent.get(id).toString();
        }
        return null;
    }

    private boolean getBooleanValue(String id, Map<String, Object> jsonContent) {
        if(jsonContent.containsKey(id) && jsonContent.get(id) instanceof Boolean) {
            return (Boolean) jsonContent.get(id);
        }
        return false;
    }
}
