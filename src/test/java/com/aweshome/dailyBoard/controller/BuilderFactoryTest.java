package com.aweshome.dailyBoard.controller;

import static org.junit.Assert.*;
import org.junit.Test;

import com.aweshome.dailyBoard.controller.BoardDTO;
import com.aweshome.dailyBoard.controller.BoardDTOBuilder;
import com.aweshome.dailyBoard.controller.Builder;
import com.aweshome.dailyBoard.controller.BuilderFactory;
import com.aweshome.dailyBoard.controller.PostDTO;
import com.aweshome.dailyBoard.controller.PostDTOBuilder;
import com.aweshome.dailyBoard.model.Board;
import com.aweshome.dailyBoard.model.Post;

public class BuilderFactoryTest {
	
	BuilderFactory target = new BuilderFactory();
	
	@Test
	public void getBuilder() {
		Builder<Board, BoardDTO> boardBuilder = target.getBuilder(Board.class);
		assertTrue(boardBuilder instanceof BoardDTOBuilder);
		BoardDTOBuilder boardDTOBuilder = (BoardDTOBuilder) boardBuilder;
		assertNotNull(boardDTOBuilder.getPostDTOBuilder());
		
		Builder<Post, PostDTO> postBuilder = target.getBuilder(Post.class);
		assertTrue(postBuilder instanceof PostDTOBuilder); 
		
		//should I just return null or throw an exception when there's no builder?
		//depends on what you want, if you return null everytime you call it you gotta check for null, if you trow an exception you will have to use try/catch everytime, unchecked exceptions are not really good for this. I personally would return an Optional<Builder<IO>>
	}

}
