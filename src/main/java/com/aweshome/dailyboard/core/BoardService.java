package com.aweshome.dailyboard.core;


import java.util.Optional;

import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;

public interface BoardService {
	
	Board createBoard(Board board) throws ValidationException;

	Optional<Board> findBoard(Long id);
	
	Optional<Board> getFirstBoard();
	
	Long getNextBoardId(Board board);

}
