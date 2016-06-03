package com.aweshome.dailyboard.controller;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyboard.model.Post;
import com.aweshome.dailyboard.controller.Builder;
import com.aweshome.dailyboard.controller.PostDTO;
import com.aweshome.dailyboard.controller.PostDTOBuilder;

public class PostDTOBuilderTest {

	private Builder<Post, PostDTO> target = new PostDTOBuilder();
	
	@Test()
	public void buildDTO() {
		String content = "post content area";
		Post post = new Post(content);
		PostDTO postDTO = target.buildDTOFrom(post);
		Assert.assertEquals(content, postDTO.getContent());
	}
	
	@Test
	public void buildEntity() {
		String content = "post content area";
		PostDTO postDTO = new PostDTO(content);
		Post post = target.buildEntityFrom(postDTO);
		Assert.assertEquals(content, post.getContent());
	}
}
