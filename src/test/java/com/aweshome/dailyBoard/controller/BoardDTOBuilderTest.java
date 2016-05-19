package com.aweshome.dailyBoard.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.* ;

import com.aweshome.dailyBoard.TestSetUpUtils;
import com.aweshome.dailyBoard.controller.BoardDTO;
import com.aweshome.dailyBoard.controller.BoardDTOBuilder;
import com.aweshome.dailyBoard.controller.PostDTOBuilder;
import com.aweshome.dailyBoard.model.Board;
import com.aweshome.dailyBoard.model.Post;


public class BoardDTOBuilderTest {
	
	private BoardDTOBuilder target;
	
	@Before
	public void setUp() {
		PostDTOBuilder postDTOBuilder = mock(PostDTOBuilder.class);
		when(postDTOBuilder.buildDTO(any(Post.class))).thenReturn(new PostDTO());
		when(postDTOBuilder.buildEntity(any(PostDTO.class))).thenReturn(new Post());
		this.target = new BoardDTOBuilder(postDTOBuilder);
	}
	
	@Test
	public void buildDTO() {
		Board board = TestSetUpUtils.getBoard(1L, "test board", "post with content", "second post");
		
		BoardDTO boardDTO = target.buildDTO(board);
		
		assertNotNull(boardDTO);
		assertEquals(board.getName(), boardDTO.getName());
		assertEquals(board.getId(), boardDTO.getId());
		assertNotNull(boardDTO.getPosts());
		assertEquals(board.getPosts().size(), boardDTO.getPosts().size());
	}
	
	@Test
	public void buildEntity() {
		BoardDTO boardDTO = TestSetUpUtils.getBoardDTO(1L, "test board", "post with content", "second post");
		
		Board board = target.buildEntity(boardDTO);
		
		assertNotNull(board);
		assertEquals(boardDTO.getName(), board.getName());
		assertEquals(boardDTO.getId(), board.getId());
		assertNotNull(board.getPosts());
		assertEquals(boardDTO.getPosts().size(), board.getPosts().size());
	}
	
}
