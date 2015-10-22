package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import nl.edegier.security.MockAuthProvider;

public class WebAuthVerticle extends AbstractVerticle {

	private static final int PORT = 9080;
	private static final String PATH = "app";
	private static final String WELCOME_PAGE = "index.html";

	@Override
	public void start() throws Exception {
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
		router.get("/api/hello-world").handler(context -> context.response().end("{\"content\" : \"Hello world!\" }"));
		router.mountSubRouter("/api", apiRouter);

		AuthProvider authProvider = new MockAuthProvider();
		AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);
		basicAuthHandler.addAuthority("admin");
		router.route().handler(basicAuthHandler).failureHandler(ErrorHandler.create());
		
		
		return router;
	}
}
