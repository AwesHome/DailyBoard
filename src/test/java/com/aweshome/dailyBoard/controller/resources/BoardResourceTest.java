package com.aweshome.dailyBoard.controller.resources;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.aweshome.dailyBoard.controller.BoardDTO;
import com.aweshome.dailyBoard.controller.BoardDTOBuilder;
import com.aweshome.dailyBoard.controller.Builder;
import com.aweshome.dailyBoard.controller.BuilderFactory;
import com.aweshome.dailyBoard.controller.PostDTO;
import com.aweshome.dailyBoard.controller.resources.BoardResource;
import com.aweshome.dailyBoard.core.BoardService;
import com.aweshome.dailyBoard.model.Board;
import com.aweshome.dailyBoard.model.Post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

public class BoardResourceTest {
	
	BoardResource target = new BoardResource();
	
	@Test
	public void getBoard() {
		Board boardReturnedBySearch = new Board();
		boardReturnedBySearch.setId(1L);
		boardReturnedBySearch.setName("main board");
		boardReturnedBySearch.addPost(new Post("most important post"));
		
		BoardDTO expectedResult = new BoardDTO();
		expectedResult.setId(1L);
		expectedResult.setName("main board DTO");
		PostDTO postDTO = new PostDTO();
		postDTO.setContent("most important post DTO");
		expectedResult.addPost(postDTO);
		
		Builder<Board, BoardDTO> builder = mock(BoardDTOBuilder.class);
		BuilderFactory builderFactory = mock(BuilderFactory.class);
		BoardService boardService = mock(BoardService.class);
		
		when(builderFactory.<Board, BoardDTO>getBuilder(Board.class)).thenReturn(builder);
		when(builder.buildDTO(any(Board.class))).thenReturn(expectedResult);
		when(boardService.getBoard(1L)).thenReturn(boardReturnedBySearch);
		
		target.setBuilderFactory(builderFactory);
		target.setBoardService(boardService);
		BoardDTO result = target.getBoard(1L);
		
		verify(boardService, times(1)).getBoard(1L);
		ArgumentCaptor<Board> boardSentToBuilder = ArgumentCaptor.forClass(Board.class);
		verify(builder).buildDTO(boardSentToBuilder.capture());
		assertNotNull(boardSentToBuilder.getValue());
		assertEquals(boardReturnedBySearch, boardSentToBuilder.getValue());
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void createBoard() {
		String name = "Main Board DTO";
		BoardDTO boardDTOToBeCreated = new BoardDTO();
		boardDTOToBeCreated.setName(name);
		PostDTO postDTO = new PostDTO();
		postDTO.setContent("post content DTO");
		boardDTOToBeCreated.addPost(postDTO);
		
		Board boardBuilt = new Board();
		boardBuilt.setId(1L);
		boardBuilt.setName("Main board");
		boardBuilt.addPost(new Post("post content"));
		
		Builder<Board, BoardDTO> boardDTOBuilder = mock(BoardDTOBuilder.class);
		BuilderFactory builderFactory = mock(BuilderFactory.class);
		BoardService boardService = mock(BoardService.class);
		
		when(builderFactory.<Board, BoardDTO>getBuilder(Board.class)).thenReturn(boardDTOBuilder);
		when(boardService.createBoard(Mockito.any(Board.class))).thenReturn(boardBuilt);
		when(boardDTOBuilder.buildEntity(any(BoardDTO.class))).thenReturn(boardBuilt);
		
		target.setBoardService(boardService);
		target.setBuilderFactory(builderFactory);
		
		Response response = target.createBoard(boardDTOToBeCreated);
		
		ArgumentCaptor<BoardDTO> boardDTOSentToBuilder = ArgumentCaptor.forClass(BoardDTO.class);
		verify(boardDTOBuilder).buildEntity(boardDTOSentToBuilder.capture());
		assertEquals(boardDTOToBeCreated, boardDTOSentToBuilder.getValue());
		
		ArgumentCaptor<Board> boardSentToBeCreated = ArgumentCaptor.forClass(Board.class);
		verify(boardService).createBoard(boardSentToBeCreated.capture());
		assertEquals(boardBuilt, boardSentToBeCreated.getValue());
		
		assertEquals(201, response.getStatus());
		assertEquals("/Board/" + boardBuilt.getId(), response.getLocation().getPath());
	}
}
