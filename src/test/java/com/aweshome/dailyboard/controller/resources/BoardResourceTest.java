package com.aweshome.dailyboard.controller.resources;

import org.junit.Before;
import org.junit.Test;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class BoardResourceTest {
	
	private BoardResource target = new BoardResource();
	private BoardService boardService;
	private UriInfo uriInfo;
	private String path = "localhost:8080";
	private Long nextBoardId = 1L;
	private BoardDTO boardDTO = TestSetUpUtils.getBoardDTO(1L, "main board", "most important post");
	
	@Before
	public void setUp() {
		Builder<Board, BoardDTO> boardDTOBuilder = mock(BoardDTOBuilder.class);
		when(boardDTOBuilder.buildEntityFrom(any(BoardDTO.class))).thenReturn(new Board());
		when(boardDTOBuilder.buildDTOFrom(any(Board.class))).thenReturn(boardDTO);
		
		BuilderFactory builderFactory = mock(BuilderFactory.class);
		when(builderFactory.<Board, BoardDTO>getBuilder(Board.class)).thenReturn(boardDTOBuilder);
		
		boardService = mock(BoardService.class);
		when(boardService.getNextBoardId(any(Board.class))).thenReturn(nextBoardId);

		uriInfo = mock(UriInfo.class);
		when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromPath(path));

		target.setBuilderFactory(builderFactory);
		target.setBoardService(boardService);
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
	
	@Test
	public void findExistentBoard() {
		when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(new Board()));
		Response resultResponse = target.getBoard(1L, uriInfo);
		assertFindBoardResponseIsBuiltCorrectly(resultResponse);
	}

	@Test
	public void findNonExistentBoard() {
		when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(null));
		Response result = target.getBoard(1L, uriInfo);
		assertEquals(Status.NOT_FOUND, result.getStatusInfo());
	}
	
	@Test
	public void getFirstBoardFindsBoard() {
		when(boardService.getFirstBoard()).thenReturn(Optional.ofNullable(new Board()));
		Response resultResponse = target.getFirstBoard(uriInfo);
		assertFindBoardResponseIsBuiltCorrectly(resultResponse);
	}

	@Test
	public void getFirstBoardDoesntFindAnything() {
		when(boardService.getFirstBoard()).thenReturn(Optional.ofNullable(null));
		Response result = target.getFirstBoard(uriInfo);
		assertEquals(Status.NOT_FOUND, result.getStatusInfo());
	}


	private void assertFindBoardResponseIsBuiltCorrectly(Response response) {
		BoardDTO result = (BoardDTO) response.getEntity();
		assertEquals(boardDTO, result);
		assertEquals(path + "/Board/" + nextBoardId , response.getLink("next").getUri().toString());
	}
}
