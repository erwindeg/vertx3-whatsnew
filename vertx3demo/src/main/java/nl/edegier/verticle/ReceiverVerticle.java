package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Created by Erwin on 18/10/2016.
 */
public class ReceiverVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("event",message -> System.out.println(message.body()));
    }

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new ReceiverVerticle());
            } else {
                System.out.println("Failed: " + res.cause());
            }
        });
    }
}
