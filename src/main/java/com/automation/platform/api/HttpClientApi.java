package com.automation.platform.api;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientApi {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientApi.class);

    @Autowired
    private Configvariable configvariable;

    private MultipartEntityBuilder multipartEntityBuilder = null;
    private HttpResponse httpresponse = null;
    private String responseBody = null;
    private CloseableHttpClient httpClient = null;

    private Map<String, String> sendHeaders = new HashMap<String, String>();
    private HttpPost httpPost = null;
    private HttpGet httpGet = null;
    private String endpointUrl;
    private String requestBody = null;

    private static String PROXY_USER = System.getProperty("proxy.user");
    private static String PROXY_PASS = System.getProperty("proxy.pass");
    private static String PROXY_HOST = System.getProperty("proxy.host");
    private static int PROXY_PORT = 8080;

    public String getUrl() {
        return endpointUrl;
    }

    public void setUrl(String endpointUrl) {
        logger.info("Endpoint url is " + endpointUrl);
        this.endpointUrl = endpointUrl;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        logger.info("Request body is " + requestBody);
        this.requestBody = requestBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }


    public Map<String, String> getSendHeaders() {
        return sendHeaders;
    }

    public void setSendHeaders(Map<String, String> sendHeaders) {
        logger.info("Header map is  " + sendHeaders);
        this.sendHeaders = sendHeaders;
    }

    public void setSendHeaders(String key, String value) {
        logger.info("Set header key as  " + key + " and value as " + value);
        if (sendHeaders == null) {
            sendHeaders = new HashMap<String, String>();
        }
        if (sendHeaders.containsKey(key)) {
            sendHeaders.remove(key);
        }
        sendHeaders.put(key, value);
    }

    public CloseableHttpClient createHttpClient(String targetHostUser, String targetHostPassword) {
        CredentialsProvider result = new BasicCredentialsProvider();
        result.setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT), new UsernamePasswordCredentials(targetHostUser, targetHostPassword));
//        result.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(targetHostUser, targetHostPassword));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(result).build();
        return httpClient;
    }

    public CloseableHttpClient createHttpClient1() {
        if (httpClient != null) {
            closeHttpClent();
            resetVariables();
            sendHeaders = null;
        }
        httpClient = HttpClients.createDefault();
        return httpClient;
    }

    public void closeHttpClent() {
        httpClient.getConnectionManager().shutdown();
        httpresponse = null;
    }

    public RequestConfig setProxyConfig() {
        HttpHost proxyHost = new HttpHost(PROXY_HOST, PROXY_PORT, "http");

        //Setting the proxy
        RequestConfig.Builder reqconfigconbuilder = RequestConfig.custom();
        reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHost);
        return reqconfigconbuilder.build();
    }

    public HttpPost createHttpPost() {
        RequestConfig config = null;
        if (configvariable.isProxyRequired()) {
            config = setProxyConfig();
        }
        httpPost = new HttpPost(getUrl());
        if (sendHeaders != null) {
            for (Map.Entry<String, String> e : sendHeaders.entrySet()) {
                httpPost.addHeader(configvariable.expandValue(e.getKey()), configvariable.expandValue(e.getValue()));
            }
        }
        httpPost.setConfig(config);
        return httpPost;
    }

    public HttpGet createHttpGet() {
        RequestConfig config = null;
        if (configvariable.isProxyRequired()) {
            config = setProxyConfig();
        }
        httpGet = new HttpGet(endpointUrl);
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        if (sendHeaders != null) {
            for (Map.Entry<String, String> e : sendHeaders.entrySet()) {
                httpGet.setHeader(configvariable.expandValue(e.getKey()), configvariable.expandValue(e.getValue()));
            }
        }
        //Setting the config to the request
        httpGet.setConfig(config);
        return httpGet;
    }

    public void setBodyParameters() {
        //Set the request post body
        StringEntity userEntity = null;
        if (requestBody != null) {
            try {
                userEntity = new StringEntity(requestBody);
            } catch (IOException e) {
                throw new TapException(TapExceptionType.IO_ERROR, "Failed to set body parameter");
            }
            httpPost.setEntity(userEntity);
        }

    }

    public HttpResponse getHTTPPostResponse() {
        //Send the request; It will immediately return the response in HttpResponse object if any
        try {
            httpresponse = httpClient.execute(httpPost);
            responseBody = EntityUtils.toString(httpresponse.getEntity());
            logger.info("Receiving:" + responseBody);
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to get response from post request");
        }
        return httpresponse;
    }

    public HttpResponse getHTTPGetResponse() {
        //Send the request; It will immediately return the response in HttpResponse object if any
        try {
            httpresponse = httpClient.execute(httpGet);
            responseBody = EntityUtils.toString(httpresponse.getEntity());
            logger.info("Receiving:" + responseBody);
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to get response from get request");
        }
        return httpresponse;
    }

    public int getResponseCode() {
        //verify the valid error code first
        int statusCode = 0;
        String responseBody = null;
        try {
            statusCode = httpresponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error("error: ", e);
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "API call failed with status [{}]", statusCode);
        }
        return statusCode;
    }

    public void addMultiPartAsFile(String keyName, File file) {
        if (multipartEntityBuilder == null) {
            multipartEntityBuilder = MultipartEntityBuilder.create();
            //Setting the mode
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        }
        //Adding a file
        multipartEntityBuilder.addBinaryBody(keyName, file);
    }

    public void addMultiPartAsText(String keyName, String text) {
        if (multipartEntityBuilder == null) {
            multipartEntityBuilder = MultipartEntityBuilder.create();
            //Setting the mode
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        }
        //Adding text
        multipartEntityBuilder.addTextBody(keyName, text);
    }

    public HttpEntity getHttpEntity() {
        //Building a single entity using the parts
        if (requestBody != null) {
            HttpEntity httpEntity = EntityBuilder.create()
                    .setText(requestBody)
                    .build();
            return httpEntity;
        } else if (multipartEntityBuilder != null) {
            return multipartEntityBuilder.build();
        }
        return null;
    }

    public HttpUriRequest createMultiPartPostRequest(String method, String url) {
        //Building the RequestBuilder request object
        RequestBuilder reqbuilder = RequestBuilder.create(method.toUpperCase()).setUri(url);
        if (configvariable.isProxyRequired()) {
            reqbuilder.setConfig(setProxyConfig());
        }
        if (sendHeaders != null) {
            for (Map.Entry<String, String> e : sendHeaders.entrySet()) {
                reqbuilder.addHeader(configvariable.expandValue(e.getKey()), configvariable.expandValue(e.getValue()));
            }
        }
        //Set the entity object to the RequestBuilder
        if (getHttpEntity() != null) {
            reqbuilder.setEntity(getHttpEntity());
        }

        //Building the request
        return reqbuilder.build();
    }

    public String executeRequestAndGetResponse(String method) {
        try {
            HttpUriRequest multipartRequest = createMultiPartPostRequest(method, getUrl());
            //Executing the request
            httpresponse = httpClient.execute(multipartRequest);
            responseBody = EntityUtils.toString(httpresponse.getEntity());
            logger.info("Receiving:" + responseBody);
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to send request and get response {}", e.getMessage());
        }
        return responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }


    public JsonNode getJsonNodeFromResponse() {
        JsonNode jsonNode = null;
        String jsonData = "";

        try {
            jsonData = responseBody;
            jsonNode = (new ObjectMapper()).readTree(jsonData);
            return jsonNode;
        } catch (Exception var4) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to convert response json data into json node [{}]", new Object[]{jsonData});
        }
    }

    public String getJsonPathStringValue(String path) {
        String val = "";
        try {
            val = JsonPath.read(responseBody, path).toString();
            logger.info("Value for node " + path + " is " + val);
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Please provide correct Json Path to get data [{}]", path);
        }
        return val;
    }

    public List<Object> getJsonPathListValue(String path) {
        List<Object> val = new ArrayList<Object>();
        try {
            val = JsonPath.read(responseBody, path);
            logger.info("Value for node " + path + " is " + val);
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Please provide correct Json Path to get data [{}]", path);
        }
        return val;
    }

    public void resetVariables() {
        multipartEntityBuilder = null;
//        sendHeaders = null;
        requestBody = null;
    }

}
