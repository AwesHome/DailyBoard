package com.aweshome.dailyboard.core;


import java.util.Optional;

import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;

public interface BoardService {

	Optional<Board> findBoard(Long id);

	Board createBoard(Board board) throws ValidationException;

}
