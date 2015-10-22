package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import nl.edegier.service.ProductService;

public class WebVerticle extends AbstractVerticle {

	private static final int PORT = 9080;
	private static final String PATH = "app";
	private static final String WELCOME_PAGE = "index.html";
	ProductService productService;

	@Override
	public void start() throws Exception {
		productService = new ProductService();
		vertx.createHttpServer(new HttpServerOptions().setPort(PORT)).requestHandler(req -> setupRouter().accept(req))
				.listen();

		startSendingMessages();
	}

	private void startSendingMessages() {
		vertx.setPeriodic(1000, arg -> {
			vertx.eventBus().publish("event", new JsonObject().put("eventmessage", "hello"));
		});
	}

	private Router setupRouter() {
		Router router = Router.router(vertx);
		
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		
		//Static routing
		router.get("/").handler(StaticHandler.create().setWebRoot(PATH).setIndexPage(WELCOME_PAGE));
		router.get("/" + PATH + "/*").handler(StaticHandler.create().setWebRoot(PATH));

		//API routing
		Router apiRouter = Router.router(vertx);
		apiRouter.get("/product").handler(productService::getProducts);
		router.mountSubRouter("/api", apiRouter);

		//Event Bus bridge
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
		sockJSHandler.bridge(new BridgeOptions().addOutboundPermitted(new PermittedOptions().setMatch(new JsonObject())));
		router.route("/eventbus/*").handler(sockJSHandler);

		return router;
	}
}
