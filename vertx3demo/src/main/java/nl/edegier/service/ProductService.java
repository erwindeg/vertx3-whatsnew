package nl.edegier.service;

import java.util.Arrays;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ProductService {

	List<JsonObject> products = Arrays.asList(new JsonObject[]{new JsonObject().put("name","Catfood").put("price", "4.50")});
	
	
	public void getProducts(RoutingContext routingContext) {
		routingContext.response().end(new JsonArray(products).toString());
	}

	
}
