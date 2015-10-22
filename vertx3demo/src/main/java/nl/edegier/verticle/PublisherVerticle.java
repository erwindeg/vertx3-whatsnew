package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class PublisherVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		vertx.setPeriodic(1000, arg -> {
			vertx.eventBus().publish("event", new JsonObject().put("eventmessage", "hello"));
		});

		//startConsumer();

	}

	private void startConsumer() {
		vertx.eventBus().consumer("event", message -> {
			System.out.println(message.body());
		});
	}
}
