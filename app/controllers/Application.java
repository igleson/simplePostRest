package controllers;

import java.util.Map;

import models.SimplePost;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result allPosts() {
		return ok(Json.toJson(SimplePost.allPost()));
	}

	public static Result createPost() {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();

		String[] msgs = params.get("msg");

		if (msgs != null) {
			for (String msg : msgs) {
				SimplePost.createPost(msg);
			}
		}
		return ok();
	}

	public static Result coments(long id) {
		return ok(Json.toJson(SimplePost.comentsFrom(id)));
	}

	public static Result addComent(long id) {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();

		String[] msgs = params.get("comment");
		if (msgs != null) {
			for (String msg : msgs) {
				SimplePost.coment(id, msg);
			}
		}
		return ok();
	}
}
