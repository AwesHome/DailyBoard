package com.aweshome.dailyboard.model;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyboard.EqualsVerifier;
import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.controller.BoardDTO;

import jersey.repackaged.com.google.common.collect.Lists;

import java.util.List;


public class BoardTest {
	
	@Test
	public void equalsTest() {
		EqualsVerifier<Board> equalsTester = new EqualsVerifier<Board>();
		
		Board a = TestUtils.getBoard(1L, "relevant information", "new information", "reminder");
		Board b = TestUtils.getBoard(1L, "relevant information", "new information", "reminder");
		Board c = TestUtils.getBoard(1L, "relevant information", "new information", "reminder");
		
		Board differentId = TestUtils.getBoard(55L, "relevant information", "new information", "reminder");
		Board nullId = TestUtils.getBoard(null, "relevant information", "new information", "reminder");
		Board differentName = TestUtils.getBoard(1L, "today", "new information", "reminder");
		Board nullName = TestUtils.getBoard(1L, null, "new information", "reminder");
		Board differentPosts = TestUtils.getBoard(1L, "relevant information", "new information");
		BoardDTO boardDTO = TestUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		
		Assert.assertNotEquals(a, null);
		Assert.assertEquals(a, b);
		Assert.assertNotEquals(a, differentId);
		Assert.assertNotEquals(a, nullId);
		Assert.assertNotEquals(a, differentName);
		Assert.assertNotEquals(a, nullName);
		Assert.assertNotEquals(a, differentPosts);
		Assert.assertNotEquals(a, boardDTO);
		
		equalsTester.assertEqualsProperties(a, b, c, Lists.newArrayList(differentId, nullId, differentName, nullName, differentPosts));
	}

	@Test
	public void onlyAddsPostIfNotRepeated() {
		Board board = new Board();
		board.addPost(new Post("content"));
		board.addPost(new Post("second"));
		board.addPost(new Post("content"));
		List<Post> posts = board.getPosts();
		Assert.assertEquals(2, posts.size());
		Assert.assertEquals("content", posts.get(0).getContent());
        Assert.assertEquals("second", posts.get(1).getContent());
	}
	
}
