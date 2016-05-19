package com.aweshome.dailyBoard.controller.resources;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.aweshome.dailyBoard.TestSetUpUtils;
import com.aweshome.dailyBoard.controller.BoardDTO;
import com.aweshome.dailyBoard.controller.BoardDTOBuilder;
import com.aweshome.dailyBoard.controller.Builder;
import com.aweshome.dailyBoard.controller.BuilderFactory;
import com.aweshome.dailyBoard.controller.resources.BoardResource;
import com.aweshome.dailyBoard.core.BoardService;
import com.aweshome.dailyBoard.model.Board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

public class BoardResourceTest {
	
	BoardResource target = new BoardResource();
	
	@Test
	public void getBoard() {
		Board boardReturnedBySearch = TestSetUpUtils.getBoard(1L, "main board", "most important post");
		BoardDTO expectedResult = TestSetUpUtils.getBoardDTO(1L, "main board", "most important post");
		
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
		BoardDTO boardDTOToBeCreated = TestSetUpUtils.getBoardDTO(null, "Main Board", "post content");
		Board boardBuilt = TestSetUpUtils.getBoard(1L, "Main Board", "post content");
		
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
