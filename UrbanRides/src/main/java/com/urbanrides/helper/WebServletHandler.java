package com.urbanrides.helper;


import com.urbanrides.service.CabBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebServletHandler extends TextWebSocketHandler {

    @Autowired
    private CabBookingService  cabBookingService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String rideId = message.getPayload();
        cabBookingService.acceptRideRiderSide(Integer.parseInt(rideId));
    }

}