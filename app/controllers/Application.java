package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Comment;
import models.Post;
import models.ResourceNotFoundException;
import models.SimplePost;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
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
		List<Post> createds = new ArrayList<Post>();

		String[] msgs = params.get("msg");
		if (msgs != null) {
			for (String msg : msgs) {
				createds.add(SimplePost.createPost(msg));
			}
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
		Map<String, String> head = new HashMap<String, String>();

		Collection<Post> posts = SimplePost.allPost();
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
			mod = Long.toString(last);
		}

		head.put("amountOfPosts", Integer.toString(posts.size()));
		head.put("lastModification", mod);
		return ok(Json.toJson(head));
	}

	public static Result headPost(long id) {
		try {
			Post post = SimplePost.getPost(id);
			Map<String, String> head = new HashMap<String, String>();

			head.put("Content-Length", Integer.toString(post.getMsg().length()));
			head.put("lastModification",
					Long.toString(post.getLastModification().getTime()));
			return ok(Json.toJson(head));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result headComment(long idPost, long idComment) {
		try {
			Map<String, String> head = new HashMap<String, String>();

			Comment comment = SimplePost.getComment(idPost, idComment);
			head.put("Content-Length",
					Integer.toString(comment.getComment().length()));
			head.put("lastModification",
					Long.toString(comment.getLastModification().getTime()));
			return ok(Json.toJson(head));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}

	public static Result headComments(long id) {
		try {
			Post post = SimplePost.getPost(id);
			Map<String, String> head = new HashMap<String, String>();

			head.put("Content-Length", Integer.toString(post.getMsg().length()));
			head.put("lastModification",
					Long.toString(post.getLastModification().getTime()));
			return ok(Json.toJson(head));
		} catch (ResourceNotFoundException e) {
			return notFound(e.getMessage());
		}
	}
}