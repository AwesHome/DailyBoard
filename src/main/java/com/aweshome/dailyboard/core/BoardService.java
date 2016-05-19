package com.aweshome.dailyboard.core;


import com.aweshome.dailyboard.model.Board;

public interface BoardService {

	Board getBoard(Long id);

	Board createBoard(Board board);

}
