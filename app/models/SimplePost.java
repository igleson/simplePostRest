package models;

import java.util.Collection;
import java.util.List;

public class SimplePost {

	public static Post createPost(String msg) {
		Post post = new Post(msg);
		post.save();
		return post;
	}

	public static List<Post> allPost() {
		return Post.find.all();
	}

	public static List<Comment> comentsFrom(long idPost) {
		return getPost(idPost).getComents();
	}

	public static Comment coment(long idPost, String comment) {
		Post post = getPost(idPost);
		Comment comm = new Comment(comment);
		comm.preUpdate();
		post.addComment(comm);
		post.save();
		return comm;
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
		Comment comm = getComment(idPost, idComment);
		comm.preUpdate();
		comm.setComment(newComm);
	}

	public static void deletePost(long id) {
		getPost(id).delete();
	}

	public static void deleteComment(long idPost, long idComment) {
		getComment(idPost, idComment).delete();
	}
}
