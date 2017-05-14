package com.aweshome.dailyboard.controller;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class BoardDTO {

	private Long id;
	private String name;
	private List<PostDTO> posts = new ArrayList<PostDTO>();

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<PostDTO> getPosts() {
		return new ArrayList<PostDTO>(this.posts);
	}

	public void addPost(PostDTO postDTO) {
		this.posts.add(postDTO);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BoardDTO other = (BoardDTO) obj;
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
		return true;
	}
	
	

}
