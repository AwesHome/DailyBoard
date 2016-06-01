package com.aweshome.dailyboard.controller.resources;


import java.net.URI;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aweshome.dailyboard.core.BoardService;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.Builder;
import com.aweshome.dailyboard.controller.BuilderFactory;


@Component
@Path("/Board")
public class BoardResource {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BuilderFactory builderFactory;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBoard(@PathParam("id") long id) {
		Optional<Board> board = this.boardService.findBoard(id);
		return this.buildGetBoardResponse(board);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBoard(BoardDTO boardDTO) {
		Board boardBuilt = buildBoard(boardDTO);
		return buildCreateBoardResponse(boardBuilt);
	}

	private Response buildCreateBoardResponse(Board boardBuilt) {
		try {
			return createdResponse(boardBuilt);
		} catch (Exception e) {
			return badRequestResponse(e);
		}
	}

	private Response buildGetBoardResponse(Optional<Board> board) {
		if (board.isPresent()){
			return okResponse(board.get());
		}
			return notFoundResponse();
	}

	private Response okResponse(Board board) {
		BoardDTO boardDTO = this.buildBoardDTO(board);
		return Response.ok(boardDTO).build();
	}

	private Response notFoundResponse() {
		return Response.status(Status.NOT_FOUND).build();
	}

	private Response badRequestResponse(Exception e) {
		return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
	}

	private Response createdResponse(Board boardBuilt) throws Exception {
		Board newBoard = this.boardService.createBoard(boardBuilt);
		return Response.created(URI.create("/Board/" + newBoard.getId())).build();
	}
	
	private BoardDTO buildBoardDTO(Board board) {
		Builder<Board, BoardDTO> builder = this.builderFactory.getBuilder(Board.class);
		BoardDTO boardDTO = builder.buildDTOFrom(board);
		return boardDTO;
	}
	
	private Board buildBoard(BoardDTO boardDTO) {
		Builder<Board, BoardDTO> builder = this.builderFactory.getBuilder(Board.class);
		Board boardBuilt = builder.buildEntityFrom(boardDTO);
		return boardBuilt;
	}
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	public void setBuilderFactory(BuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}
	
}
