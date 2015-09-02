var sd = vertx.sharedData();
sd.getClusterWideMap("mymap", function(map, res_err) {
	if (res_err == null) {
		map.get("time", function(val, resGet_err) {
			if (resGet_err == null) {
				console.log(time);
			} else {
				// Something went wrong!
			}
		});

	} else {
		console.log("cannot get map");
	}
});