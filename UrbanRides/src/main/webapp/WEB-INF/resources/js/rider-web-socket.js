var stompClient = null;
var connected = false;

function connectWebSocket() {
    if (connected) return;

    var socket = new SockJS('/UrbanRides/rider-notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        connected = true;

        stompClient.subscribe('/topic/rider-incoming-notifications', function (message) {
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
