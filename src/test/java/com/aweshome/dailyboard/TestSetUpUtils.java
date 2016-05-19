package com.aweshome.dailyboard;

import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.PostDTO;

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
