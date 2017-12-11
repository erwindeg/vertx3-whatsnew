package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class PublisherVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		vertx.setPeriodic(1000, arg -> {
			vertx.eventBus().publish("event", 
					new JsonObject().put("eventmessage", "hello world"));
		});
	}

	public static void main(String[] args) {
		Vertx.clusteredVertx(new VertxOptions(), res -> {
			if (res.succeeded()) {
				Vertx vertx = res.result();
				vertx.deployVerticle(new PublisherVerticle());
			} else {
				System.out.println("Failed: " + res.cause());
			}
		});
	}
}

