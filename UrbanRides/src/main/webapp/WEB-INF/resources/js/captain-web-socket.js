var stompClient = null;
var connected = false;

function connectWebSocket() {
    if (connected) return;

    var socket = new SockJS('/UrbanRides/captain-notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        connected = true;
        stompClient.subscribe('/topic/captain-incoming-notifications', function (message) {
            console.log(message);
            var messageBody = JSON.parse(message.body);
            var notificationMessage = messageBody.message || "No Message";
            showSuccesstMsg(notificationMessage);
        });
    });
}

function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
        connected = false;
    }
}

if (!connected) {
    connectWebSocket();
}
