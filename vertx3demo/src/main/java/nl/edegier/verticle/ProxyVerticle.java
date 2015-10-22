package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;

public class ProxyVerticle extends AbstractVerticle {

	private Logger vertxLogger = LoggerFactory.getLogger(ProxyVerticle.class.getName());

	@Override
	public void start() throws Exception {
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("address", "mongo"));
		vertx.deployVerticle(new MongoServiceVerticle(), options, res -> {
			saveMessage();
		});

	}

	private void saveMessage() {
		MongoService mongo = MongoService.createEventBusProxy(vertx, "mongo");

		mongo.save("message", new JsonObject().put("content", "helloworld"), result -> {
			vertxLogger.info(result.result());
		});
	}
}
