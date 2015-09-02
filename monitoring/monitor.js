var eb = vertx.eventBus();
eb.consumer("event", function (message) {
  console.log("Received message: " + message.body().eventmessage);
});