package com.aweshome.dailyboard.controller.resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.aweshome.dailyboard.TestSetUpUtils;
import com.aweshome.dailyboard.core.BoardService;
import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.BoardDTOBuilder;
import com.aweshome.dailyboard.controller.Builder;
import com.aweshome.dailyboard.controller.BuilderFactory;
import com.aweshome.dailyboard.controller.resources.BoardResource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class BoardResourceTest {
	
	private BoardResource target = new BoardResource();
	private Builder<Board, BoardDTO> boardDTOBuilder;
	private BoardService boardService;
	
	@Before
	public void setUp() {
		boardDTOBuilder = mock(BoardDTOBuilder.class);
		BuilderFactory builderFactory = mock(BuilderFactory.class);
		boardService = mock(BoardService.class);
		when(builderFactory.<Board, BoardDTO>getBuilder(Board.class)).thenReturn(boardDTOBuilder);
		when(boardDTOBuilder.buildEntityFrom(any(BoardDTO.class))).thenReturn(new Board());

		target.setBuilderFactory(builderFactory);
		target.setBoardService(boardService);
	}
	
	@Test
	public void findBoard() {
		Board boardReturnedBySearch = TestSetUpUtils.getBoard(1L, "main board", "most important post");
		when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(boardReturnedBySearch));
		BoardDTO boardBuilt = TestSetUpUtils.getBoardDTO(1L, "main board", "most important post");
		when(boardDTOBuilder.buildDTOFrom(any(Board.class))).thenReturn(boardBuilt);
		
		Response resultResponse = target.getBoard(1L);
		assertBoardIsSentToBuilder(boardReturnedBySearch);
		assertBoardIsReturnedInResponse(boardBuilt, resultResponse);
	}

	@Test
	public void findNonExistentBoard() {
		when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(null));
		Response result = target.getBoard(1L);
		assertEquals(Status.NOT_FOUND, result.getStatusInfo());
	}
	
	@Test
	public void createBoard() throws ValidationException {
		BoardDTO boardDTOToBeCreated = TestSetUpUtils.getBoardDTO(null, "Main Board", "post content");
		Board boardBuilt = TestSetUpUtils.getBoard(null, "Main Board", "post content");
		Board boardCreated = TestSetUpUtils.getBoard(1L, "Main Board", "post content");
		when(boardDTOBuilder.buildEntityFrom(any(BoardDTO.class))).thenReturn(boardBuilt);
		when(boardService.createBoard(any(Board.class))).thenReturn(boardCreated);
		target.createBoard(boardDTOToBeCreated);
		
		assertBoardDTOIsSentToBuilder(boardDTOToBeCreated);
		assertBoardIsSentToBeCreated(boardBuilt);
	}
	
	@Test
	public void createBoardSuccessfullResponse() throws ValidationException {
		Board boardCreated = TestSetUpUtils.getBoard(1L, "Main Board", "post content");
		when(boardService.createBoard(any(Board.class))).thenReturn(boardCreated);
		Response response = target.createBoard(new BoardDTO());
		
		assertEquals(Status.CREATED, response.getStatusInfo());
		assertEquals("/Board/" + boardCreated.getId(), response.getLocation().getPath());
	}

	@Test
	public void createBoardErrorResponse() throws ValidationException {
		String errorMessage = "error message";
		when(boardService.createBoard(any(Board.class))).thenThrow(new ValidationException(errorMessage));
		Response response = target.createBoard(new BoardDTO());
		assertEquals(Status.BAD_REQUEST, response.getStatusInfo());
		assertEquals(errorMessage, response.getEntity());
	}
	
	private void assertBoardIsSentToBuilder(Board board) {
		ArgumentCaptor<Board> boardSentToBuilder = ArgumentCaptor.forClass(Board.class);
		verify(boardDTOBuilder).buildDTOFrom(boardSentToBuilder.capture());
		assertEquals(board, boardSentToBuilder.getValue());
	}
	
	private void assertBoardDTOIsSentToBuilder(BoardDTO boardDTO) {
		ArgumentCaptor<BoardDTO> boardDTOSentToBuilder = ArgumentCaptor.forClass(BoardDTO.class);
		verify(boardDTOBuilder).buildEntityFrom(boardDTOSentToBuilder.capture());
		assertEquals(boardDTO, boardDTOSentToBuilder.getValue());
	}

	private void assertBoardIsReturnedInResponse(BoardDTO board, Response response) {
		BoardDTO result = (BoardDTO) response.getEntity();
		assertEquals(board, result);
	}
	
	private void assertBoardIsSentToBeCreated(Board board) throws ValidationException {
		ArgumentCaptor<Board> boardSentToBeCreated = ArgumentCaptor.forClass(Board.class);
		verify(boardService).createBoard(boardSentToBeCreated.capture());
		assertEquals(board, boardSentToBeCreated.getValue());
	}
}
