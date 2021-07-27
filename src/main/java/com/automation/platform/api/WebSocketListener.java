package com.automation.platform.api;

import com.automation.platform.config.Configvariable;
import com.automation.platform.filehandling.JsonReader;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebSocketListener extends WebSocketAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

    private String lastMessage;
    private String emailMessage;

    @Autowired
    private JsonReader jsonReader;

    @Autowired
    private Configvariable configvariable;

    @Override
    public void onTextMessage(WebSocket webSocket, String message) {
        Map<String, Object> webSocketMessage = jsonReader.convertJsonStringToMap(message);
        if (webSocketMessage.containsKey("subject") && webSocketMessage.get("subject").toString().equalsIgnoreCase(configvariable.expandValue("${EMAIL_SUBJECT}"))) {
            emailMessage = message;
        }
        lastMessage = message;
    }

    @Override
    public void onStateChanged(WebSocket webSocket, WebSocketState webSocketState) {
        logger.info("Websocket new state: " + webSocketState);
    }

    public void resetMessage() {
        this.lastMessage = null;
        this.emailMessage = null;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

}
