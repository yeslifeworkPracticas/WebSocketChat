//This sample is how to use websocket of Tomcat7.0.52.
//web.xml is not required. Because you can use Servlet3.0 Annotation.
package wsapp;

import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wschat")
public class WsChat {

    //notice:not thread-safe
    private static ArrayList<Session> sessionList = new ArrayList<Session>();

    @OnOpen
    public void onOpen(Session session) {
        try {
            sessionList.add(session);
            for (Session user : sessionList) {
                System.out.println("User "+user.getId());
            }
            //asynchronous communication
            session.getBasicRemote().sendText("Hello User "+session.getId()+"!");
        } catch (IOException e) {
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionList.remove(session);
    }

    @OnMessage
    public void onMessage(String msg) {
        try {
            String listaUsers = "";
            for (Session session : sessionList) {
                listaUsers += "User "+session.getId() + "\n";
                System.out.println("User "+session.getId());
            }
            
            for (Session session : sessionList) {
                //System.out.println(session.getUs);
                
                //asynchronous communication
                String message = msg;
                if (message.startsWith("100,")) {
                    String[] msgDivided = message.split(",");
                    msg = "Se envia " + msgDivided[1];
                    message = "Te han pintado de "+msgDivided[1];
                    session.getBasicRemote().sendText(message +"\n"+listaUsers);
                } else {
                    session.getBasicRemote().sendText(message +"\n"+listaUsers);
                }
            }
        } catch (IOException e) {
        }
    }
}
