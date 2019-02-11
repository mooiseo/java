package kr.co.samsung.com.sockjs.ws;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EchoHandler extends TextWebSocketHandler {
	

    private List<WebSocketSession> connectedUsers;
    TextMessage message = null;
	WebSocketSession session = null;
    
    
    
    
    public EchoHandler() {
        connectedUsers = new ArrayList<WebSocketSession>();
    }
    
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("1");
		System.out.println(session.getId());
		connectedUsers.add(session);
		
		System.out.println(connectedUsers.toString());
		this.session=session;
		
	}

	

	

	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	
		
		session.sendMessage(new TextMessage(message.getPayload()));
		for (WebSocketSession webSocketSession : connectedUsers) {
			webSocketSession.sendMessage(new TextMessage(message.getPayload()));
		}
		
		
		
	}
	
	public  void sendMessage(String msg) throws Exception {
		handleTextMessage(session,new TextMessage(msg));
	}
	
	

	@Override
	public void afterConnectionClosed(
			WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("3");
		System.out.println(session.getId());
	}

}
