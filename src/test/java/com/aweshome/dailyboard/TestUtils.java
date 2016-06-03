package com.aweshome.dailyboard;

import com.aweshome.dailyboard.core.validation.ValidationReport;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.aweshome.dailyboard.controller.BoardDTO;
import com.aweshome.dailyboard.controller.PostDTO;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestUtils {

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

	public static void assertReportHasOnlyExpectedIssues(ValidationReport report, Set<String> expectedIssues) {
		boolean expectedToHaveIssues = expectedIssues.size() > 0;
		assertEquals(expectedToHaveIssues, report.hasIssues());
		assertEquals(expectedIssues.size(), report.getIssues().size());
		assertEquals(expectedIssues, report.getIssues());
	}
}
