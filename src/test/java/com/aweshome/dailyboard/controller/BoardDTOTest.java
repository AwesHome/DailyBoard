package com.aweshome.dailyboard.controller;


import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyboard.EqualsVerifier;
import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.model.Board;

import jersey.repackaged.com.google.common.collect.Lists;


public class BoardDTOTest {

	@Test
	public void equalsTest() {
		EqualsVerifier<BoardDTO> equalsTester = new EqualsVerifier<BoardDTO>();
		
		BoardDTO a = TestUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		BoardDTO b = TestUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		BoardDTO c = TestUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		
		BoardDTO differentId = TestUtils.getBoardDTO(55L, "relevant information", "new information", "reminder");
		BoardDTO nullId = TestUtils.getBoardDTO(null, "relevant information", "new information", "reminder");
		BoardDTO differentName = TestUtils.getBoardDTO(1L, "today", "new information", "reminder");
		BoardDTO nullName = TestUtils.getBoardDTO(1L, null, "new information", "reminder");
		BoardDTO differentPosts = TestUtils.getBoardDTO(1L, "relevant information", "new information");
		Board board = TestUtils.getBoard(1L, "relevant information", "new information", "reminder");
		
		Assert.assertNotEquals(a, null);
		Assert.assertEquals(a, b);
		Assert.assertNotEquals(a, differentId);
		Assert.assertNotEquals(a, nullId);
		Assert.assertNotEquals(a, differentName);
		Assert.assertNotEquals(a, nullName);
		Assert.assertNotEquals(a, differentPosts);
		Assert.assertNotEquals(a, board);
		
		equalsTester.assertEqualsProperties(a, b, c, Lists.newArrayList(differentId, nullId, differentName, nullName, differentPosts));
	}
}
