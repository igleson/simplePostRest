package models;

import java.util.Collection;

public class SimplePost {

	public static Post createPost(String msg) {
		Post post = new Post(msg);
		post.save();
		return post;
	}

	public static Collection<Post> allPost() {
		return Post.find.all();
	}

	public static Collection<Comment> comentsFrom(long idPost) {
		return getPost(idPost).getComents();
	}

	public static void coment(long idPost, String comment) {
		Post post = getPost(idPost);
		post.addComment(new Comment(comment));
		post.save();
	}

	public static Post getPost(long id) {
		Post post = Post.find.byId(id);
		if (post == null) {
			throw new ResourceNotFoundException(
					"There is no post with this ID ou maybe this post was deleted");
		}
		return post;
	}

	public static Comment getComment(long idPost, long idComment) {
		Collection<Comment> comments = Post.find.byId(idPost).getComents();
		for (Comment comment : comments) {
			if (comment.getNum() == idComment) {
				return comment;
			}
		}
		throw new ResourceNotFoundException(
				"There is no comment with this ID ou maybe this post was deleted");
	}

	public static void setMsg(long id, String newMsg) {
		getPost(id).setMsg(newMsg);
	}

	public static void editComment(long idPost, long idComment, String newComm) {
		getComment(idPost, idComment).setComment(newComm);
	}

	public static void deletePost(long id) {
		getPost(id).delete();
	}

	public static void deleteComment(long idPost, long idComment) {
		getComment(idPost, idComment).delete();
	}
}
