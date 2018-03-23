var db = null;
var ws;
var cont;

function abrirBD() {
    db = openDatabase("BD", "1.0", "HTML5 SQLITE Example", 200000);

    db.transaction(function (tx) {
        tx.executeSql("SELECT * FROM sqlite_master WHERE name ='conexion' and type='table'", [], function (tx, result) {
            if (result.rows.length > 0) {
                alert('cliente ya conectado');
                conexion();
            } else {
                alert('cliente no conectado');
                conexion();
                crearTabla();
                crearRegistro();
            }
        }, function (tx, error) {
            //alert('la bbdd no existe');
            return;
        });
    });
}

function crearTabla() {
    alert('tabla');

    db.transaction(function (tx) {
        //fecha conexion, fecha desconexion
        tx.executeSql("create table conexion(valor TEXT)", [], function (result) {});
    });
    //alert('tabla creada');
}

function crearRegistro() {
    alert('registro');

    db.transaction(function (tx) {
        tx.executeSql("insert into conexion values(?) ", ['conectado']);
    });
    //alert('Registro insertado');
}


function conexion() {
    alert('conectado');
    ws = new WebSocket("ws://localhost:8084/WebSocketChat/wschat");
    alert(ws);
    ws.onopen = function () {
    };
    ws.onmessage = function (message) {
        document.getElementById("chatlog").textContent += message.data + "\n";
        document.body.style.backgroundColor = message.data;
    };
    return ws;
}

function postToServer() {
    alert('postSERVER' + ws);

    ws.send(document.getElementById("msg").value);
    document.getElementById("msg").value = "";
}
function closeConnect() {
    ws.close();
}
