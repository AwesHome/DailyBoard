package com.aweshome.dailyBoard.model;

import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyBoard.EqualsVerifier;
import com.aweshome.dailyBoard.TestSetUpUtils;
import com.aweshome.dailyBoard.controller.BoardDTO;

import jersey.repackaged.com.google.common.collect.Lists;


public class BoardTest {
	
	@Test
	public void equalsTest() {
		EqualsVerifier<Board> equalsTester = new EqualsVerifier<Board>();
		
		Board a = TestSetUpUtils.getBoard(1L, "relevant information", "new information", "reminder");
		Board b = TestSetUpUtils.getBoard(1L, "relevant information", "new information", "reminder");
		Board c = TestSetUpUtils.getBoard(1L, "relevant information", "new information", "reminder");
		
		Board differentId = TestSetUpUtils.getBoard(55L, "relevant information", "new information", "reminder");
		Board nullId = TestSetUpUtils.getBoard(null, "relevant information", "new information", "reminder");
		Board differentName = TestSetUpUtils.getBoard(1L, "today", "new information", "reminder");
		Board nullName = TestSetUpUtils.getBoard(1L, null, "new information", "reminder");
		Board differentPosts = TestSetUpUtils.getBoard(1L, "relevant information", "new information");
		BoardDTO boardDTO = TestSetUpUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		
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
	
	
}
