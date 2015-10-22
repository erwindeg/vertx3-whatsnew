package nl.edegier.model;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

public class MyUser extends AbstractUser {
	private String username;

	private List<String> roles = new ArrayList<String>();
	
	public void addRole(String role){
		this.roles.add(role);
	}

	public MyUser(String username) {
		this.setUsername(username);
	}

	

	@Override
	public JsonObject principal() {
		System.out.println("principal called");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthProvider(AuthProvider arg0) {
		System.out.println("setAuthProvider called");
		// TODO Auto-generated method stub

	}

	@Override
	protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> handler) {
		handler.handle(new AsyncResult<Boolean>() {
			@Override
			public Boolean result() {
				if (roles.contains(permission)) {
					return true;
				}
				return false;
			}

			@Override
			public Throwable cause() {
				return new Error("403");
			}

			@Override
			public boolean succeeded() {
				return roles != null;
			}

			@Override
			public boolean failed() {
				return !succeeded();
			}
		});
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
