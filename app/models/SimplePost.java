package models;

import java.util.Collection;

public class SimplePost {

	public static void createPost(String msg) {
		Post post = new Post(msg);
		post.save();
	}

	public static Collection<Post> allPost() {
		return Post.find.all();
	}

	public static Collection<Comment> comentsFrom(long idPost) {
		return Post.find.byId(idPost).getComents();
	}

	public static void coment(long idPost, String comment){
		Post.find.byId(idPost).addComment(new Comment(comment));
	}
}
