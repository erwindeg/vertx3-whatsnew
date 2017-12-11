package nl.edegier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoVerticle extends AbstractVerticle {

	MongoClient mongo;

	@Override
	public void start() throws Exception {
		this.mongo = MongoClient.createShared(vertx, new JsonObject());

		vertx.eventBus().consumer("message.insert",this::insertMessage);
		vertx.eventBus().consumer("message.remove",this::removeMessage);
		vertx.eventBus().consumer("message.find",this::getMessages);
	}
	
	
	private void insertMessage(Message<String> message){
		this.mongo.insert("message", new JsonObject().put("message", 
				message.body()), result -> {
			if (result.succeeded()) {
				message.reply("save success");
			} else {
				message.fail(1, "save failed");
			}
		});
	}
	
	private void removeMessage(Message<String> message){
		this.mongo.remove("message", new JsonObject().put("message", message.body()), result -> {
			if (result.succeeded()) {
				message.reply(result.result());
			} else {
				message.fail(1, "save failed");
			}
		});
	}
	
	private void getMessages(Message<String> message){
		this.mongo.find("message", new JsonObject().put("message", message.body()), result -> {
			if (result.succeeded()) {
				message.reply(new JsonArray(result.result()));
			} else {
				message.fail(1, "find failed");
			}
		});
	}
}
