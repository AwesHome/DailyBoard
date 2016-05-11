package com.aweshome.dailyBoard.core;


import com.aweshome.dailyBoard.model.Board;

public interface BoardService {

	Board getBoard(Long id);

	Board createBoard(Board board);

}
