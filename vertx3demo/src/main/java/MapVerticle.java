import io.vertx.core.AbstractVerticle;
import io.vertx.core.shareddata.AsyncMap;

public class MapVerticle extends AbstractVerticle {
	@Override
	public void start() throws Exception {
		vertx.sharedData().<String, String> getClusterWideMap("mymap",
				result -> {
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
