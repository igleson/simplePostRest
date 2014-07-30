package controllers;

import java.util.Map;

import models.ResourceNotFoundException;
import models.SimplePost;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.defaultpages.error;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result allPosts() {
		return ok(Json.toJson(SimplePost.allPost()));
	}

	public static Result getPost(long id) {
		try {
			return ok(Json.toJson(SimplePost.getPost(id)));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
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

	public static Result editPost(long id) {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();
		String newMsg = params.get("msg")[0];

		try {
			SimplePost.setMsg(id, newMsg);
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
		return ok();
	}

	public static Result deletePost(long id) {
		try {
			SimplePost.deletePost(id);
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
		return ok();
	}

	public static Result getComment(long idPost, long idComment) {
		try {
			return ok(Json.toJson(SimplePost.getComment(idPost, idComment)));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result comments(long id) {
		try {
			return ok(Json.toJson(SimplePost.comentsFrom(id)));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result addComent(long id) {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();

		String[] comms = params.get("comment");
		if (comms != null) {
			try {
				for (String msg : comms) {
					SimplePost.coment(id, msg);
				}
			} catch (ResourceNotFoundException e) {
				return notFound(e.getMessage());
			}
		}
		return ok();
	}

	public static Result editComment(long idPost, long idComment) {
		System.out.println("EDIT COMMENT");
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();

		String newComm = params.get("comment")[0];
		try {
			SimplePost.editComment(idPost, idComment, newComm);
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
		return ok();
	}

	public static Result deleteComment(long idPost, long idComment) {
		System.out.println("DELETE COMMENT " + idPost + " " + idComment);
		try {
			SimplePost.deleteComment(idPost, idComment);
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
		return ok();
	}
}
