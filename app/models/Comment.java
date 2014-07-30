package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@Entity
public class Comment extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Finder<Long, Comment> find = new Finder<Long, Comment>(
			Long.class, Comment.class);

	@Id
	private long id;

	@NotNull
	private String comment;

	@NotNull
	private int num;

	public Comment(String msg) {
		this.comment = msg;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
