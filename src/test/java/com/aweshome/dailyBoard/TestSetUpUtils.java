package com.aweshome.dailyBoard;

import com.aweshome.dailyBoard.controller.BoardDTO;
import com.aweshome.dailyBoard.controller.PostDTO;
import com.aweshome.dailyBoard.model.Board;
import com.aweshome.dailyBoard.model.Post;

public class TestSetUpUtils {

	public static Board getBoard(Long id, String name, String...postsContents) {
		Board board = new Board();
		board.setId(id);
		board.setName(name);
		for (String postContent : postsContents) {
			board.addPost(new Post(postContent));
		}
		return board;
	}
	
	public static BoardDTO getBoardDTO(Long id, String name, String...postsContents) {
		BoardDTO board = new BoardDTO();
		board.setId(id);
		board.setName(name);
		for (String postContent : postsContents) {
			board.addPost(new PostDTO(postContent));
		}
		return board;
	}
	
	public static Post getPost(Long id, String content) {
		Post post = new Post(content);
		post.setId(id);
		return post;
	}
}
