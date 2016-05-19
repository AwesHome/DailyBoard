package com.aweshome.dailyboard.controller.resources;


import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public BoardDTO getBoard(@PathParam("id") long id) {
		Board board = this.boardService.getBoard(id);
		Builder<Board, BoardDTO> builder = this.builderFactory.getBuilder(Board.class);
		return builder.buildDTO(board);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBoard(BoardDTO boardDTO) {
		Builder<Board, BoardDTO> builder = this.builderFactory.getBuilder(Board.class);
		Board board = builder.buildEntity(boardDTO);
		this.boardService.createBoard(board);
		return Response.created(URI.create("/Board/" + board.getId())).build();
	}
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	public void setBuilderFactory(BuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}
	
}
