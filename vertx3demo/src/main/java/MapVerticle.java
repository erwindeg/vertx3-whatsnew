import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

public class MapVerticle extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		SharedData sd = vertx.sharedData();

		sd.<String, String> getClusterWideMap("mymap", result -> {
			if (result.succeeded())
				startTimer(result.result());
		});
	}

	public void startTimer(AsyncMap<String, String> map) {
		vertx.setPeriodic(1000, arg -> {
			map.put("time", System.currentTimeMillis() + "", res -> {
			});
		});
	}
}
