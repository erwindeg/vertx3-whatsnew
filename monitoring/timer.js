var eb = vertx.eventBus();
vertx.setPeriodic(1000, function (id) {
	eb.publish("event", {"eventmessage":"hello"});
});