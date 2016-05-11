package com.aweshome.dailyBoard.controller;

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
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (posts == null) {
			if (other.posts != null) {
				return false;
			}
		} else if (!posts.equals(other.posts)) {
			return false;
		}
		return true;
	}
	
	

}
