package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import models.Comment;
import models.Post;
import models.ResourceNotFoundException;
import models.SimplePost;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

	private static final int PARAM_KEY = 0;
	private static final int PARAM_VALUE = 1;
	private static final String KEY_VALUE_SEPARATOR = "=";
	private static final String PARAM_SEPARATOR = ";";
	private static final String ACCEPT_SEPARATOR = ",";
	private static final String PRIORITY_KEY = "q=";
	private static final String JSON = "json";
	private static final String HTML = "html";

	public static Result app() {
		return ok(views.html.app.render());
	}
	
	public static Result app2() {
		return ok(views.html.app2.render());
	}

	public static Result index() {
		if (choosenAcceptType().toLowerCase().contains(HTML)) {
			return ok(views.html.index.render());
		} else {
			return ok("HOW TO:\n"
					+ "GET		/post								return all posts\n"
					+ "POST		/post								add a new post, use param msg to set the message from the post\n"
					+ "HEAD		/post								sumarize data about all the posts\n\n"
					+ "GET		/post/:id							return a specific post\n"
					+ "PUT		/post/:id							set a specific post, use msg to set the message from the post\n"
					+ "DELETE		/post/:id							delete a specefic post\n"
					+ "HEAD		/post/:id							sumarize the data about a post\n\n"
					+ "GET		/post/:idPost/comment				get all the comments from a specif post\n"
					+ "POST		/post/:idPost/comment				add a new comment to a post, use param comment to set the content of the comment\n"
					+ "HEAD		/post/:idPost/comment				sumazire data about all comment from a post\n\n"
					+ "GET		/post/:idPost/comment/:idComment	get a specific comment from a specific post\n"
					+ "PUT		/post/:idPost/comment/:idComment	set a specific comment from a post, use param comment to set the content\n"
					+ "DELETE		/post/:idPost/comment/:idComment	delete a comment from a post\n"
					+ "HEAD		/post/:idPost/comment/:idComment	sumarize the data about a comment from a post\n");
		}
	}

	public static Result posts(int initial, int _final) {
		System.out.println(initial);
		System.out.println(_final);
		if (choosenAcceptType().toLowerCase().contains(HTML)) {
			return ok(views.html.post.render(SimplePost.posts(initial, _final)));
		} else if (choosenAcceptType().toLowerCase().contains(JSON)) {
			return ok(allPostsJson(initial, _final));
		} else {
			return ok(allPostsJson(initial, _final).toString());
		}
	}

	private static String choosenAcceptType() {
		String[] accepts = request().headers().get(ACCEPT);

		List<String> accs = fixedAccepts(accepts);

		String choosenType = "";
		float biggerPreference = 0;
		for (String acc : accs) {
			if (acc.contains(PRIORITY_KEY)) {
				String[] params = acc.split(PARAM_SEPARATOR);
				for (String param : params) {
					if (param.startsWith(PRIORITY_KEY)) {
						float preference = Float.parseFloat(param
								.split(KEY_VALUE_SEPARATOR)[PARAM_VALUE]);
						if (preference > biggerPreference) {
							biggerPreference = preference;
							choosenType = params[PARAM_KEY];
						}
					}
				}
			} else if (biggerPreference < Float.MAX_VALUE) {
				biggerPreference = Float.MAX_VALUE;
				choosenType = acc;
			}
		}
		return choosenType;
	}

	private static List<String> fixedAccepts(String[] accepts) {
		List<String> accs = new ArrayList<String>();

		for (String accept : accepts) {
			for (String acc : accept.split(ACCEPT_SEPARATOR)) {
				accs.add(acc);
			}
		}
		return accs;
	}

	public static Result getPost(long id) {
		try {
			if (choosenAcceptType().toLowerCase().contains(HTML)) {
				List<Post> posts = new ArrayList<Post>();
				posts.add(SimplePost.getPost(id));
				return ok(views.html.post.render(posts));
			} else if (choosenAcceptType().toLowerCase().contains(JSON)) {
				return ok(Json.toJson(SimplePost.getPost(id)));
			} else {
				return ok(Json.toJson(SimplePost.getPost(id)).toString());
			}
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	private static JsonNode allPostsJson(int initial, int _final) {
		return Json.toJson(SimplePost.posts(initial, _final));
	}

	public static Result createPost() {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();
		List<Post> createds = new ArrayList<Post>();

		String[] msgs = params.get("msg");
		if (msgs != null) {
			for (String msg : msgs) {
				createds.add(SimplePost.createPost(msg));
			}
		} else {
			return badRequest("Must exist at least a param named msg");
		}
		return ok(Json.toJson(createds));
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
			if (choosenAcceptType().toLowerCase().contains(HTML)) {
				List<Comment> comments = new ArrayList<Comment>();
				comments.add(SimplePost.getComment(idPost, idComment));
				return ok(views.html.comments.render(comments));
			} else if (choosenAcceptType().toLowerCase().contains(JSON)) {
				return ok(Json.toJson(SimplePost.getComment(idPost, idComment)));
			} else {
				return ok(Json.toJson(SimplePost.getComment(idPost, idComment))
						.toString());
			}
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result comments(long id) {
		try {
			if (choosenAcceptType().toLowerCase().contains(HTML)) {
				return ok(views.html.comments
						.render(SimplePost.comentsFrom(id)));
			} else if (choosenAcceptType().toLowerCase().contains(JSON)) {
				return ok(Json.toJson(SimplePost.comentsFrom(id)));
			} else {
				return ok(Json.toJson(SimplePost.comentsFrom(id)).toString());
			}
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result addComent(long id) {
		Map<String, String[]> params = Controller.request().body()
				.asFormUrlEncoded();

		String[] comms = params.get("comment");
		List<Comment> createds = new ArrayList<Comment>();
		if (comms != null) {
			try {
				for (String msg : comms) {
					createds.add(SimplePost.coment(id, msg));
				}
			} catch (ResourceNotFoundException e) {
				return notFound(e.getMessage());
			}
		}
		return ok();
	}

	public static Result editComment(long idPost, long idComment) {
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
		try {
			SimplePost.deleteComment(idPost, idComment);
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
		return ok();
	}

	public static Result actionNotFount(String uri) {
		return notFound("There is no resource to " + uri);
	}

	public static Result headPosts() {

		Collection<Post> posts = SimplePost.posts(1, 10);
		String mod = "";
		if (posts.isEmpty()) {
			mod = "never";
		} else {
			long last = 0;
			for (Post post : posts) {
				if (post.getLastModification().getTime() > last) {
					last = post.getLastModification().getTime();
				}
			}
			mod = formatDate(last);
		}

		Controller.response().setHeader("Content-Length",
				Integer.toString(allPostsJson(1, 10).toString().getBytes().length));
		Controller.response().setHeader("Last-Modified", mod);
		return ok();
	}

	public static Result headPost(long id) {
		try {
			Post post = SimplePost.getPost(id);
			Controller.response()
					.setHeader(
							"Content-Length",
							Integer.toString(Json.toJson(post).toString()
									.getBytes().length));
			Controller.response().setHeader("Last-Modified",
					formatDate(post.getLastModification().getTime()));
			return ok();
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result headComment(long idPost, long idComment) {
		try {
			Comment comment = SimplePost.getComment(idPost, idComment);
			Controller.response()
					.setHeader(
							"Content-Length",
							Integer.toString(Json.toJson(comment).toString()
									.getBytes().length));
			Controller.response().setHeader("Last-Modified",
					formatDate(comment.getLastModification().getTime()));

			return ok();
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result headComments(long id) {
		try {
			List<Comment> comments = SimplePost.getPost(id).getComents();

			Controller.response().setHeader(
					"Content-Length",
					Integer.toString(Json.toJson(comments).toString()
							.getBytes().length));

			String mod = "";
			if (comments.isEmpty()) {
				mod = "never";
			} else {
				long last = 0;
				for (Comment comment : comments) {
					if (comment.getLastModification().getTime() > last) {
						last = comment.getLastModification().getTime();
					}
				}
				mod = formatDate(last);
			}

			Controller.response().setHeader("Last-Modified", mod);

			return ok();
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	private static String formatDate(long timestamp) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(timestamp);
	}
}