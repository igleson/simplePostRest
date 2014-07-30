package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@Entity
public class Post extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8719326480079151339L;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Comment> comments = new ArrayList<Comment>();

	@Id
	private long id;

	@NotNull
	private String msg;

	private int nextNumComment;

	public static final Finder<Long, Post> find = new Finder<Long, Post>(
			Long.class, Post.class);

	public Post(String msg) {
		this.msg = msg;
		this.nextNumComment = 1;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Comment> getComents() {
		return Collections.unmodifiableList(this.comments);
	}

	private void setComments(List<Comment> coments) {
		this.comments = coments;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
		comment.setNum(nextNumComment);
		nextNumComment++;
	}
}
