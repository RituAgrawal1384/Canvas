package com.automation.platform.api;

import com.automation.platform.config.Configvariable;
import com.automation.platform.constants.Constants;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.automation.platform.filehandling.JsonReader;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    private WebSocket webSocket;
    private static WebSocketFactory factory = new WebSocketFactory();
    private static String OS = System.getProperty("os.name").toLowerCase();


    @Autowired
    private WebSocketListener webSocketListener;

    @Autowired
    private JsonReader jsonReader;

    @Autowired
    private Configvariable configvariable;

    private final int MAX_ATTEMPT = 20;
    private String webSocketURL;

    public String getWebSocketURL() {
        return webSocketURL;
    }

    public void setWebSocketURL(String webSocketURL) {
        this.webSocketURL = webSocketURL;
    }


    public void connectToWebSocket(String socketUrl) {
        try {
            logger.info("Creating Socket for : " + socketUrl);
            wssProxySetter(); // only works for Win
            webSocket = factory.createSocket(socketUrl);
            webSocket.addListener(webSocketListener);
            webSocket.connect();
        } catch (IOException | WebSocketException e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to connect with websocket");
        }
    }

    public String sendRequestAndCheckForResponse(String jsonFileName) {
        if (webSocket == null || !webSocket.isOpen()) {
            webSocketListener.resetMessage();
            connectToWebSocket(getWebSocketURL());
        } else {
            webSocketListener.resetMessage();
        }
        String requestBody = jsonReader.getJsonString(jsonFileName);
        requestBody = configvariable.expandValue(requestBody);
        logger.info("Sending: " + requestBody);
        webSocket.sendText(requestBody);

        //wait for the response to come
        int attempt = 0;
        while (StringUtils.isEmpty(webSocketListener.getLastMessage()) && attempt < MAX_ATTEMPT) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new TapException(TapExceptionType.PROCESSING_FAILED, "Thread.sleep failed");
            }
            attempt++;
        }
        if (!StringUtils.isEmpty(webSocketListener.getLastMessage())) {
            return webSocketListener.getLastMessage();
        }
        return null;
    }

    public String sendMalinatorRequestAndGetForResponse(String url) {
        if (webSocket == null || !webSocket.isOpen()) {
            webSocketListener.resetMessage();
            connectToWebSocket(url);
        } else {
            webSocketListener.resetMessage();
        }
        //wait for the response to come
        int attempt = 0;
        while (StringUtils.isEmpty(webSocketListener.getEmailMessage()) && attempt < MAX_ATTEMPT) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new TapException(TapExceptionType.PROCESSING_FAILED, "Thread.sleep failed");
            }
            attempt++;
        }
        if (!StringUtils.isEmpty(webSocketListener.getEmailMessage())) {
            return webSocketListener.getEmailMessage();
        }
        return null;
    }

    public void disconnectToWebSocket() {
        try {
            logger.info("Disconnecting from websSocket...");
            if (webSocket != null) {
                webSocket.disconnect();
                webSocket = null;
            }
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to disconnect with webSocket");
        }
    }

    //  set string values for "proxy" and "port" from config
    public void wssProxySetter() {
        if (Constants.PROXY_URL != null && (configvariable.isProxyRequired())) {
            factory.getProxySettings().setServer(Constants.PROXY_URL).setPort(Integer.parseInt(Constants.PROXY_PORT));
        } else
            logger.info("OS is NOT Windows system...");
    }

}
