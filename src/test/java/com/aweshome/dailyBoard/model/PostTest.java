package com.aweshome.dailyBoard.model;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyBoard.EqualsVerifier;
import com.aweshome.dailyBoard.TestSetUpUtils;
import com.aweshome.dailyBoard.controller.PostDTO;

import jersey.repackaged.com.google.common.collect.Lists;

public class PostTest {

	@Test
	public void equalsTest() {
		EqualsVerifier<Post> equalsTester = new EqualsVerifier<Post>();
		
		Post a = TestSetUpUtils.getPost(1L, "data");
		Post b = TestSetUpUtils.getPost(1L, "data");
		Post c = TestSetUpUtils.getPost(1L, "data");
		
		Post differentId = TestSetUpUtils.getPost(2L, "data");
		Post nullId = TestSetUpUtils.getPost(null, "data");
		Post differentContent = TestSetUpUtils.getPost(1L, "different data");
		Post nullContent = TestSetUpUtils.getPost(1L, null);
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
