package com.aweshome.dailyboard.controller;

import com.aweshome.dailyboard.model.Post;

public class PostDTOBuilder implements Builder<Post, PostDTO> {

	@Override
	public PostDTO buildDTO(Post input) {
		PostDTO postDTO = new PostDTO(input.getContent());
		return postDTO ;
	}

	@Override
	public Post buildEntity(PostDTO input) {
		Post post = new Post();
		post.setContent(input.getContent());
		return post ;
	}

}
