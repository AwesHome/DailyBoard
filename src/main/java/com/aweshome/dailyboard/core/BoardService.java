package com.aweshome.dailyboard.core;

import java.util.Optional;

import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;

public interface BoardService {

	Board createBoard(Board board) throws ValidationException;

	Post createPostForBoard(Post post, Long boardId) throws ValidationException;

	Optional<Board> findBoard(Long id);

	Optional<Board> getFirstBoard();

	Long getNextBoardId(Board board);

}
