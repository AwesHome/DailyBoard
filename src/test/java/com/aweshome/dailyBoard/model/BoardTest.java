package com.aweshome.dailyBoard.model;

import org.junit.Assert;
import org.junit.Test;


public class BoardTest {
	
	@Test
	public void equalsTest() {
		Board a = this.getBoardWithoutId();
		Board b = this.getBoardWithoutId();
		Board c = this.getBoardWithoutId();
		
		this.equalsReflexibilityTest(a);
		this.equalsSymmetryTest(a, b);
		this.equalsTransitivityTest(a, b, c);
		
		a = this.getBoardWithId();
		b = this.getBoardWithId();
		c = this.getBoardWithId();
		
		this.equalsReflexibilityTest(a);
		this.equalsSymmetryTest(a, b);
		this.equalsTransitivityTest(a, b, c);
	}
	
	public void equalsReflexibilityTest(Board a) {
		Assert.assertEquals(a, a);
	}
	
	public void equalsSymmetryTest(Board a, Board b) {
		Assert.assertEquals(a, b);
		Assert.assertEquals(b, a);
	}
	
	public void equalsTransitivityTest(Board a, Board b, Board c) {
		Assert.assertEquals(a, b);
		Assert.assertEquals(b, c);
		Assert.assertEquals(a, c);
	}
	
	public Board getBoardWithoutId() {
		Board board = new Board();
		board.setName("relevant information");
		board.addPost(new Post("new information"));
		board.addPost(new Post("reminder"));
		return board;
	}
	
	public Board getBoardWithId() {
		Board board = new Board();
		board.setId(55L);
		board.setName("today");
		return board;
		
	}
	
}
