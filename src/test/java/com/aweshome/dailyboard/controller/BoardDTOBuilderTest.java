package com.aweshome.dailyboard.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.* ;

import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;


public class BoardDTOBuilderTest {
	
	private BoardDTOBuilder target;
	
	@Before
	public void setUp() {
		PostDTOBuilder postDTOBuilder = mock(PostDTOBuilder.class);
		when(postDTOBuilder.buildDTOFrom(any(Post.class))).thenReturn(new PostDTO(""));
		when(postDTOBuilder.buildEntityFrom(any(PostDTO.class))).thenReturn(new Post("first"), new Post("second"));
		this.target = new BoardDTOBuilder(postDTOBuilder);
	}
	
	@Test
	public void buildDTO() {
		Board board = TestUtils.getBoard(1L, "test board", "post with content", "second post");
		BoardDTO boardDTO = target.buildDTOFrom(board);
		
		assertNotNull(boardDTO);
		assertEquals(board.getName(), boardDTO.getName());
		assertEquals(board.getId(), boardDTO.getId());
		assertNotNull(boardDTO.getPosts());
		assertEquals(board.getPosts().size(), boardDTO.getPosts().size());
	}
	
	@Test
	public void buildEntity() {
		BoardDTO boardDTO = TestUtils.getBoardDTO(1L, "test board", "post with content", "second post");
		Board board = target.buildEntityFrom(boardDTO);
		
		assertNotNull(board);
		assertEquals(boardDTO.getName(), board.getName());
		assertEquals(boardDTO.getId(), board.getId());
		assertNotNull(board.getPosts());
		assertEquals(boardDTO.getPosts().size(), board.getPosts().size());
	}
	
}
