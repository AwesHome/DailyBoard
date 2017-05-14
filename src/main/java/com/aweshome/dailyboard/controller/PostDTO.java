package com.aweshome.dailyboard.controller;

public class PostDTO {
	
	private String content;

	public PostDTO(String content) {
		 this.content = content;
	}

	public PostDTO(){}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PostDTO other = (PostDTO) obj;
		if (this.getContent() == null) {
			if (other.getContent() != null) {
				return false;
			}
		} else if (!this.getContent().equals(other.getContent())) {
			return false;
		}
		return true;
	}

}
