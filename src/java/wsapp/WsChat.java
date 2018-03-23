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
            session.getBasicRemote().sendText(listaUsers);
            //asynchronous communication
            session.getBasicRemote().sendText("Hello User " + session.getId() + "!");
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
            JsonReader reader;
            reader = Json.createReader(new StringReader(msg));
            JsonObject jsonMessage = reader.readObject();

            String listaUsers = "";
            for (Session session : sessionList) {
                String userID = "User " + session.getId();
                listaUsers += userID + "\n";
                System.out.println(userID);
            }

            JsonProvider provider = JsonProvider.provider();
            JsonObject addMessage = provider.createObjectBuilder()
                    .add("listUser", listaUsers)
                    .add("userMessage", "ALgo algo")
                    .build();
            for (Session session : sessionList) {
                //System.out.println(session.getUs);

                //asynchronous communication
                String message = msg;
                if (message.startsWith("100,")) {
                    String[] msgDivided = message.split(",");
                    msg = "Se envia " + msgDivided[1];
                    message = "Te han pintado de " + msgDivided[1];
                    session.getBasicRemote().sendText(addMessage.toString());
                } else {
                    session.getBasicRemote().sendText(addMessage.toString());
                }
            }

        } catch (IOException e) {
        }
    }
}
