package com.aweshome.dailyboard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="board")
public class Board {
	
	private Long id;
	private String name;
	private List<Post> posts = new ArrayList<Post>();
	private Date lastModified;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
	public List<Post> getPosts() {
		return new ArrayList<Post>(this.posts);
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public void addPost(Post post) {
		if(this.posts.size() > 0) {
			for(Post oldPost: this.posts) {
				if(oldPost.getContent().equals(post.getContent())) return;
			}
		}
		this.posts.add(post);
	}

	@Column(name="name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="last_modified")
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Board other = (Board) obj;
		if (this.getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}
		if (this.getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!this.getName().equals(other.getName())) {
			return false;
		}
		if (!this.getPosts().equals(other.getPosts())) {
			return false;
		}
		if (this.getLastModified() == null) {
			if (other.getLastModified() != null) {
				return false;
			}
		} else if (!this.getLastModified().equals(other.getLastModified())) {
			return false;
		} 
		return true;
	}
}
