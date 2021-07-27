package com.automation.platform.api;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;

public class GraphQlApi {
    private static final Logger logger = LoggerFactory.getLogger(GraphQlApi.class);

    private static final OkHttpClient client = new OkHttpClient();

    private static Configvariable configvariable = new Configvariable();

    /**
     * Parses the given input stream graphql to the string suitable for the request
     * payload.
     *
     * @param inputStream - A {@link InputStream} of Graphql File
     * @param variables   - The variables in the form of {@link ObjectNode}
     * @return A string suitable for the request payload.
     */
    public static String parseGraphql(InputStream inputStream, ObjectNode variables, String queryType) {
        String graphqlFileContent = convertInputStreamToString(inputStream);
        return convertToGraphqlString(graphqlFileContent, variables, queryType);
    }

    /**
     * Parses the given graphql file object to the string suitable for the request
     * payload.
     *
     * @param file      - A {@link File} object
     * @param variables - The variables in the form of {@link ObjectNode}
     * @return A string suitable for the request payload.
     */
    public static String parseGraphql(File file, ObjectNode variables, String queryType) {
        String graphqlFileContent = null;
        try {
            graphqlFileContent = convertInputStreamToString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Unable to find file [{}]", file.getAbsoluteFile());
        }
        return convertToGraphqlString(graphqlFileContent, variables, queryType);
    }

    private static String convertToGraphqlString(String graphql, ObjectNode variables, String queryType) {
        ObjectMapper oMapper = new ObjectMapper();
        ObjectNode oNode = oMapper.createObjectNode();
        oNode.put("query", graphql);
        oNode.set("variables", variables);
        try {
            return oMapper.writeValueAsString(oNode);
        } catch (JsonProcessingException e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to process file [{}]", e.getMessage());
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Unable to convert file to string");
        }
        return sb.toString();
    }


    public static Response sendPostRequest(String graphqlPayload, String uri, Map<String, String> headers) {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = addHeaders(headers).url(uri).post(body).build();
        Response res = null;
        try {
            res = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static JsonNode getJsonNodeFromResponse(Response response) {
        JsonNode jsonNode = null;
        String jsonData = "";
        try {
            jsonData = response.body().string();
            jsonNode = new ObjectMapper().readTree(jsonData);
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to convert response json data into json node [{}]", jsonData);
        }
        return jsonNode;
    }

    public static Request.Builder addHeaders(Map<String, String> sendHeaders) {
        Request.Builder builder = new Request.Builder();
        if (sendHeaders != null) {
            for (String key : sendHeaders.keySet()) {
                builder.addHeader(key, sendHeaders.get(key));
            }
        }
        if (configvariable.isCookieRequired()) {
            setApiHeadersFromVmArguments(builder);
        }
        return builder;
    }

    public static void setApiHeadersFromVmArguments(Request.Builder builder) {
        String webCookieName = configvariable.getStringVar("bluegreen.cookie.keys");
        String webCookieValue = configvariable.getStringVar("bluegreen.cookie.values");
        if (webCookieValue.isEmpty()) {
            logger.info("Please supply the cookie value for the Key" + webCookieName);
            throw new TapException(TapExceptionType.IO_ERROR, "Please supply the cookie value for the Key [{}]", webCookieName);
        }
        StringTokenizer nameToken = new StringTokenizer(webCookieName, ";");
        StringTokenizer valueToken = new StringTokenizer(webCookieValue, ";");
        while (nameToken.hasMoreTokens()) {
            String name = nameToken.nextToken();
            String value = valueToken.nextToken();
            builder.addHeader(name, value);
        }

    }

    public static Response sendPostRequest(String graphqlPayload, String uri) {
        return sendPostRequest(graphqlPayload, uri, null);
    }

}
