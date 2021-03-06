package com.aweshome.dailyboard.controller;

import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;

public class BoardDTOBuilder implements Builder<Board, BoardDTO> {
	
	private Builder<Post, PostDTO> postDTOBuilder;
	
	public BoardDTOBuilder(Builder<Post, PostDTO> postDTOBuilder) {
		this.postDTOBuilder = postDTOBuilder;
	}

	@Override
	public BoardDTO buildDTOFrom(Board input) {
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(input.getId());
		boardDTO.setName(input.getName());
		for (Post post : input.getPosts()) {
			PostDTO postDTO = this.postDTOBuilder.buildDTOFrom(post);
			boardDTO.addPost(postDTO);
		}
		return boardDTO ;
	}

	@Override
	public Board buildEntityFrom(BoardDTO input) {
		Board board = new Board();
		board.setId(input.getId());
		board.setName(input.getName());
		for (PostDTO postDTO : input.getPosts()) {
			Post post = this.postDTOBuilder.buildEntityFrom(postDTO);
			board.addPost(post);
		}
		return board ;
	}
	
	public Builder<Post, PostDTO> getPostDTOBuilder() {
		return this.postDTOBuilder;
	}
}
