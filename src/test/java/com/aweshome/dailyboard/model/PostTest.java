package com.aweshome.dailyboard.model;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyboard.EqualsVerifier;
import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.controller.PostDTO;

import jersey.repackaged.com.google.common.collect.Lists;

public class PostTest {

	@Test
	public void equalsTest() {
		EqualsVerifier<Post> equalsTester = new EqualsVerifier<Post>();
		
		Post a = TestUtils.getPost(1L, "data");
		Post b = TestUtils.getPost(1L, "data");
		Post c = TestUtils.getPost(1L, "data");
		
		Post differentId = TestUtils.getPost(2L, "data");
		Post nullId = TestUtils.getPost(null, "data");
		Post differentContent = TestUtils.getPost(1L, "different data");
		Post nullContent = TestUtils.getPost(1L, null);
		PostDTO postDTO = new PostDTO("data");
		Post post = new Post("data");
		
		Assert.assertEquals(a, b);
		Assert.assertNotEquals(a, null);
		Assert.assertNotEquals(post, postDTO);
		Assert.assertNotEquals(a, differentId);
		Assert.assertNotEquals(a, nullId);
		Assert.assertNotEquals(a, differentContent);
		Assert.assertNotEquals(a, nullContent);
		
		equalsTester.assertEqualsProperties(a, b, c, Lists.newArrayList(differentContent, differentId, nullId, nullContent));
		
		
	}
	
}
