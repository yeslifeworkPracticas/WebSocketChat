var ws = new WebSocket("ws://192.168.1.53:8084/WebSocketChat/wschat");
ws.onopen = function () {

};
ws.onmessage = function (msg) {
    var message = JSON.parse(msg.data);
    
    document.getElementById("userList").textContent = message.listUser;
    document.getElementById("chatlog").textContent += message.userMessage + "\n";
};
function postToServer() {
    var listUser = document.getElementById("userList").value;
    var userMessage = document.getElementById("msg").value;
    
    document.getElementById("userList").textContent = ""
    document.getElementById("msg").textContent = "";
    
    var SendMessage = {
        listUser: listUser,
        userMessage: userMessage
    };
    
    ws.send(JSON.stringify(SendMessage));
}

function closeConnect() {
    ws.close();
}