package com.aweshome.dailyboard.controller;

import static org.junit.Assert.*;
import org.junit.Test;

import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.BoardDTOBuilder;
import com.aweshome.dailyboard.controller.Builder;
import com.aweshome.dailyboard.controller.BuilderFactory;
import com.aweshome.dailyboard.controller.PostDTO;
import com.aweshome.dailyboard.controller.PostDTOBuilder;

public class BuilderFactoryTest {
	
	private BuilderFactory target = new BuilderFactory();
	
	@Test
	public void getBuilder() {
		Builder<Board, BoardDTO> boardBuilder = target.getBuilder(Board.class);
		assertTrue(boardBuilder instanceof BoardDTOBuilder);
		assertBoardBuilderHasPostBuilder((BoardDTOBuilder) boardBuilder);
	}
	
	@Test
	public void getBuilderForPost() {
		Builder<Post, PostDTO> postBuilder = target.getBuilder(Post.class);
		assertTrue(postBuilder instanceof PostDTOBuilder); 
	}
	
	private void assertBoardBuilderHasPostBuilder(BoardDTOBuilder boardDTOBuilder) {
		assertNotNull(boardDTOBuilder.getPostDTOBuilder());
	}
}
