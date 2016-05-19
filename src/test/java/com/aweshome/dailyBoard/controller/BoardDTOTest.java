package com.aweshome.dailyBoard.controller;


import org.junit.Assert;
import org.junit.Test;

import com.aweshome.dailyBoard.EqualsVerifier;
import com.aweshome.dailyBoard.TestSetUpUtils;
import com.aweshome.dailyBoard.model.Board;

import jersey.repackaged.com.google.common.collect.Lists;


public class BoardDTOTest {

	@Test
	public void equalsTest() {
		EqualsVerifier<BoardDTO> equalsTester = new EqualsVerifier<BoardDTO>();
		
		BoardDTO a = TestSetUpUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		BoardDTO b = TestSetUpUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		BoardDTO c = TestSetUpUtils.getBoardDTO(1L, "relevant information", "new information", "reminder");
		
		BoardDTO differentId = TestSetUpUtils.getBoardDTO(55L, "relevant information", "new information", "reminder");
		BoardDTO nullId = TestSetUpUtils.getBoardDTO(null, "relevant information", "new information", "reminder");
		BoardDTO differentName = TestSetUpUtils.getBoardDTO(1L, "today", "new information", "reminder");
		BoardDTO nullName = TestSetUpUtils.getBoardDTO(1L, null, "new information", "reminder");
		BoardDTO differentPosts = TestSetUpUtils.getBoardDTO(1L, "relevant information", "new information");
		Board board = TestSetUpUtils.getBoard(1L, "relevant information", "new information", "reminder");
		
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
