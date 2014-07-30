package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@Entity
public class Comment extends Model {

	public static final Finder<Long, Comment> find = new Finder<Long, Comment>(
			Long.class, Comment.class);

	@Id
	private long id;

	@NotNull
	private String coment;

	public Comment(String msg) {
		this.coment = msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
