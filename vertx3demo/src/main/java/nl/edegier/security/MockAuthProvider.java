package nl.edegier.security;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import nl.edegier.model.MyUser;

public class MockAuthProvider implements io.vertx.ext.auth.AuthProvider {
	
	private static final String USERNAME = "test";
	private static final String PASSWORD = "test";
	
	
	@Override
	public void authenticate(JsonObject jsonObject, Handler<AsyncResult<User>> handler) {
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");
		
		
		
			handler.handle(new AsyncResult<User>() {				
				@Override
				public User result() {
					if(succeeded()) {
						MyUser user = new MyUser(username);
						user.addRole("admin");
						return user;
					}
					
					return null;
				}

				@Override
				public Throwable cause() {
					// TODO Auto-generated method stub
					System.out.println("Method cause() called on AsyncResult in AuthProvider.");
					return null;
				}

				@Override
				public boolean succeeded() {
					if(USERNAME.equals(username) && PASSWORD.equals(password)) {
						return true;
					}
					
					return false;
				}

				@Override
				public boolean failed() {
					return !succeeded();
				}
			});
		
	}
}
