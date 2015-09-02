
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class MyVerticle extends AbstractVerticle {
	
	@Override
	public void start() throws Exception {
		vertx.setPeriodic(1000,  arg -> {
			vertx.eventBus().publish("event", new JsonObject().put("eventmessage", "hello"));
		});
	}
}
