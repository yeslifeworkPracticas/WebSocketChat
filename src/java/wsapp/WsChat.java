//This sample is how to use websocket of Tomcat7.0.52.
//web.xml is not required. Because you can use Servlet3.0 Annotation.
package wsapp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.json.*;
import javax.json.spi.JsonProvider;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wschat")
public class WsChat {

    //notice:not thread-safe
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    private String userName = "";

    @OnOpen
    public void onOpen(Session session) {
        try {
            sessionList.add(session);

            String listaUsers = "";
            for (Session user : sessionList) {
                String userID = "User " + user.getId();
                listaUsers += userID + "\n";
                System.out.println(userID);
            }
            //asynchronous communication
            userName = session.getId();
            String userMessage = "Hello User " + session.getId() + "!";
            String texto = "userMessage: " + userMessage + ", listUser: " + listaUsers;
            session.getBasicRemote().sendText(texto);
        } catch (IOException e) {
            e.printStackTrace();
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
                String userID = "User " + session.getId();
                listaUsers += userID + "\n";
                System.out.println(userID);
            }

            for (Session session : sessionList) {
                //System.out.println(session.getUs);

                //asynchronous communication
                String message = msg;
                if (message.startsWith("userMessage: 100,")) {
                    String[] msgDivided = message.split(",", 2);
                    String[] color = msgDivided[1].split(", listUser:", 2);
                    String senderOfMsg = "Se envia " + color[0];
                    String userMessage = userName + ": " + "Te han pintado de " + color[0];
                    message = "userMessage: " + userMessage + ", listUser: " + listaUsers;
                    session.getBasicRemote().sendText(message);
                } else {
                    if (userName.equals(session.getId())) {
                        message = "userMessage: " + "Enviaste un mensaje" + ", listUser: " + listaUsers;
                        session.getBasicRemote().sendText(message);
                    } else {
                        String[] msgDivided = message.split("userMessage: ", 2);
                        String[] messages = msgDivided[1].split(", listUser:", 2);
                        String userMessage = userName + ": " + messages[0];
                        message = "userMessage: " + userMessage + ", listUser: " + listaUsers;
                        session.getBasicRemote().sendText(message);
                    }
                }
            }

        } catch (IOException e) {
        }
    }
}
