package com.aweshome.dailyboard.controller.resources;

import com.aweshome.dailyboard.controller.*;
import com.aweshome.dailyboard.model.Post;
import org.junit.Before;
import org.junit.Test;

import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.core.BoardService;
import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class BoardResourceTest {
	
	private BoardResource target = new BoardResource();
	private final String path = "localhost:8080";
	private final Long nextBoardId = 20L;
	private final BoardDTO boardDTO = TestUtils.getBoardDTO(1L, "main board", "most important post");

	@Before
	public void setUp() {
		this.resetBoardServiceMock();
		this.resetBoardDTOBuilderMock();
	}

	private BoardService resetBoardServiceMock() {
		BoardService boardService = mock(BoardService.class);
		when(boardService.getNextBoardId(any(Board.class))).thenReturn(2L);
		target.setBoardService(boardService);
		return boardService;
	}

	private Builder<Board, BoardDTO> resetBoardDTOBuilderMock() {
		Builder<Board, BoardDTO> boardDTOBuilder = mock(BoardDTOBuilder.class);
		when(boardDTOBuilder.buildEntityFrom(any(BoardDTO.class))).thenReturn(new Board());
		when(boardDTOBuilder.buildDTOFrom(any(Board.class))).thenReturn(new BoardDTO());

		Builder<Post, PostDTO> postDTOBuilder = mock(PostDTOBuilder.class);
		when(postDTOBuilder.buildEntityFrom(any(PostDTO.class))).thenReturn(new Post());

		BuilderFactory builderFactory = mock(BuilderFactory.class);
		when(builderFactory.<Board, BoardDTO>getBuilder(Board.class)).thenReturn(boardDTOBuilder);
		when(builderFactory.<Post, PostDTO>getBuilder(Post.class)).thenReturn(postDTOBuilder);
		target.setBuilderFactory(builderFactory);
		return boardDTOBuilder;
	}

	@Test
	public void createBoardSuccessfullResponse() throws ValidationException {
		Board boardCreated = TestUtils.getBoard(1L, "Main Board", "post content");
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.createBoard(any(Board.class))).thenReturn(boardCreated);
		Response response = target.createBoard(new BoardDTO());
		assertEquals(Status.CREATED, response.getStatusInfo());
		assertEquals("/Board/" + boardCreated.getId(), response.getLocation().getPath());
	}

	@Test
	public void createBoardErrorResponse() throws ValidationException {
		String errorMessage = "error message";
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.createBoard(any(Board.class))).thenThrow(new ValidationException(errorMessage));
		Response response = target.createBoard(new BoardDTO());
		assertEquals(Status.BAD_REQUEST, response.getStatusInfo());
		assertEquals(errorMessage, response.getEntity());
	}

	@Test
    public void createPostSuccessfulResponse() throws ValidationException {
        BoardService boardService = this.resetBoardServiceMock();
        Post postCreated = new Post("new post content");
        Long boardId = 5L;
        when(boardService.createPostForBoard(any(Post.class), anyLong())).thenReturn(postCreated);
	    Response response = target.createPost(boardId, new PostDTO());
        assertEquals(Status.CREATED, response.getStatusInfo());
        assertEquals("/Board/" + boardId + "/Post/" + postCreated.getId(), response.getLocation().getPath());

    }

    @Test
    public void createPostErrorResponse() throws ValidationException {
        String errorMessage = "error message";
        BoardService boardService = this.resetBoardServiceMock();
        when(boardService.createPostForBoard(any(Post.class), anyLong())).thenThrow(new ValidationException(errorMessage));
        Response response = target.createPost(9L, new PostDTO());
        assertEquals(Status.BAD_REQUEST, response.getStatusInfo());
        assertEquals(errorMessage, response.getEntity());
    }

	@Test
	public void findExistentBoard() {
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.findBoard(Mockito.anyLong())).thenReturn(Optional.ofNullable(new Board()));
		when(boardService.getNextBoardId(any(Board.class))).thenReturn(this.nextBoardId);
		Builder<Board, BoardDTO> boardDTOBuilder = this.resetBoardDTOBuilderMock();
		when(boardDTOBuilder.buildDTOFrom(any(Board.class))).thenReturn(this.boardDTO);

		Response resultResponse = target.getBoard(1L, this.mockUriInfo());
		assertResponseHasBoardAndLinkToNexBoard(resultResponse, this.boardDTO, this.nextBoardId);
	}

	@Test
	public void findNonExistentBoard() {
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.findBoard(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		Response result = target.getBoard(1L, this.mockUriInfo());
		assertEquals(Status.NOT_FOUND, result.getStatusInfo());
	}

	@Test
	public void getFirstBoardFindsBoard() {
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.getFirstBoard()).thenReturn(Optional.ofNullable(new Board()));
		when(boardService.getNextBoardId(any(Board.class))).thenReturn(this.nextBoardId);
		Builder<Board, BoardDTO> boardDTOBuilder = this.resetBoardDTOBuilderMock();
		when(boardDTOBuilder.buildDTOFrom(any(Board.class))).thenReturn(this.boardDTO);
		Response resultResponse = target.getFirstBoard(this.mockUriInfo());
		assertResponseHasBoardAndLinkToNexBoard(resultResponse, this.boardDTO, this.nextBoardId);
	}

	@Test
	public void getFirstBoardDoesntFindAnything() {
		BoardService boardService = this.resetBoardServiceMock();
		when(boardService.getFirstBoard()).thenReturn(Optional.ofNullable(null));
		Response result = target.getFirstBoard(this.mockUriInfo());
		assertEquals(Status.NOT_FOUND, result.getStatusInfo());
	}

	private UriInfo mockUriInfo() {
        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromPath(path));
        return uriInfo;
    }

	private void assertResponseHasBoardAndLinkToNexBoard(Response response, BoardDTO boardDTO, Long nextBoardId) {
		BoardDTO result = (BoardDTO) response.getEntity();
		assertEquals(boardDTO, result);
		assertEquals(path + "/Board/" + nextBoardId , response.getLink("next").getUri().toString());
	}
}
