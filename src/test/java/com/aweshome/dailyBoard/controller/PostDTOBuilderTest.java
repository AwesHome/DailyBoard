package com.aweshome.dailyBoard.controller;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyBoard.controller.Builder;
import com.aweshome.dailyBoard.controller.PostDTO;
import com.aweshome.dailyBoard.controller.PostDTOBuilder;
import com.aweshome.dailyBoard.model.Post;

public class PostDTOBuilderTest {

	private Builder<Post, PostDTO> target = new PostDTOBuilder();
	
	@Test
	public void buildDTO() {
		String content = "post content area";
		Post post = new Post(content);
		PostDTO postDTO = target.buildDTO(post);
		Assert.assertEquals(content, postDTO.getContent());
	}
	
	@Test
	public void buildEntity() {
		String content = "post content area";
		PostDTO postDTO = new PostDTO(content);
		Post post = target.buildEntity(postDTO);
		Assert.assertEquals(content, post.getContent());
	}
}
