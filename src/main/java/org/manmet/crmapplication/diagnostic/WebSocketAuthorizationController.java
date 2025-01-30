package org.manmet.crmapplication.diagnostic;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketAuthorizationController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketAuthorizationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAuthorizationUpdate(List<ApiAuthorizationInfo> authInfoList) {
        // Send real-time updates to all subscribers on "/topic/authorisation"
        messagingTemplate.convertAndSend("/topic/authorisation", authInfoList);
    }
}
