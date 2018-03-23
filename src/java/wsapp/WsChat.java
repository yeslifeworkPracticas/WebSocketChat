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
        //try {
        sessionList.add(session);
        //asynchronous communication
        //session.getBasicRemote().sendText("#FF0000");
        //} catch (IOException e) {
        //}
    }

    @OnClose
    public void onClose(Session session) {
        sessionList.remove(session);
    }

    @OnMessage
    public void onMessage(String msg) {
        try {
            //String message = InterpretarMensaje(msg);
            for (Session session : sessionList) {
                session.getBasicRemote().sendText(InterpretarMensaje(session,msg));

            }
        } catch (IOException e) {
        }
    }

    private String InterpretarMensaje(Session msg,String o) {
        String mensaje = null;
       mensaje = o + msg.getId();
        /*
        if (msg.contains(",")) {
            String[] dividir = msg.split(",");
            msg = dividir[0];
            if (msg.equals("100")) {
                mensaje = "#FF0000";
            }
        } else {
            mensaje = msg;
        }*/
        return mensaje;
    }
}
