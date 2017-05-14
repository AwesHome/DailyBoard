package com.aweshome.dailyboard.controller.resources;


import java.net.URI;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.aweshome.dailyboard.controller.PostDTO;
import com.aweshome.dailyboard.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aweshome.dailyboard.core.BoardService;
import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.BuilderFactory;


@Component
@Path("/Board")
public class BoardResource {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BuilderFactory builderFactory;
	
	private String path = "/Board/";
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBoard(BoardDTO boardDTO) {
		Board boardBuilt = this.builderFactory.getBuilder(Board.class).buildEntityFrom(boardDTO);
		try {
			Board newBoard = this.boardService.createBoard(boardBuilt);
			return Response.created(URI.create(this.path + newBoard.getId())).build();
		} catch (ValidationException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPost(@PathParam("id") long boardId, PostDTO postDTO) {
		Post postBuilt = this.builderFactory.getBuilder(Post.class).buildEntityFrom(postDTO);
		try {
			Post newPost = this.boardService.createPostForBoard(postBuilt, boardId);
			return Response.created(URI.create(this.path + boardId + "/Post/" + newPost.getId())).build();
		} catch (ValidationException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBoard(@PathParam("id") long id, @Context UriInfo uriInfo) {
		Optional<Board> board = this.boardService.findBoard(id);
		return prepareResponseToReturnBoard(uriInfo, board);
	}
	
	@GET
	@Path("first")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFirstBoard(@Context UriInfo uriInfo) {
		Optional<Board> board = this.boardService.getFirstBoard();
		return prepareResponseToReturnBoard(uriInfo, board);
	}

	private Response prepareResponseToReturnBoard(UriInfo uriInfo, Optional<Board> board) {
		if (board.isPresent()){
			Long nextBoardId = this.boardService.getNextBoardId(board.get());
			BoardDTO boardDTO = (BoardDTO) this.builderFactory.getBuilder(Board.class).buildDTOFrom(board.get());
			URI nextBoardUri = uriInfo.getBaseUriBuilder().path(this.path + nextBoardId.toString()).build();
			return Response.ok(boardDTO).link(nextBoardUri, "next").build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	public void setBuilderFactory(BuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}
}
