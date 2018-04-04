var ws = new WebSocket("ws://192.168.1.53:8084/WebSocketChat/wschat");
ws.onopen = function () {

};
ws.onmessage = function (msg) {
    var message = msg.data;
    
    var msgDivided = message.split("userMessage: ",2);
    var messages = msgDivided[1].split(", listUser: ",2);
    var userMessage = messages[0];
    var userList = messages[1];
    
    document.getElementById("chatlog").textContent += userMessage + "\n";
    document.getElementById("userList").textContent = userList;
};
function postToServer() {
    var userMessage = document.getElementById("msg").value;
    var listUser = document.getElementById("userList").value;
    
    document.getElementById("msg").value = "";
    
    var SendMessage = "userMessage: "+ userMessage+", listUser: "+listUser;
    ws.send(SendMessage);
}

function closeConnect() {
    ws.close();
}