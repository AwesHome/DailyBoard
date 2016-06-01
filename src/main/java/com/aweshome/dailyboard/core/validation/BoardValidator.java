package com.aweshome.dailyboard.core.validation;


import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.model.Board;

@Component
@Transactional
public class BoardValidator {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public ValidationReport validateBoardToBeCreated(Board board){
		ValidationReport report = this.validateBoardHasRequiredFields(board);
		if (!report.hasIssues()) {
			report.merge(this.validateBoardDoesntExistYet(board));
		}
		return report;
	}

	private ValidationReport validateBoardDoesntExistYet(Board board) {
		if (this.boardAlreadyExists(board)) {
			return new ValidationReport("There is already a board registered with the name: " + board.getName());
		}
		return new ValidationReport();
	}

	private boolean boardAlreadyExists(Board board) {
		Optional<Board> boardFound = searchForBoard(board);
		return boardFound.isPresent();
		
	}

	private Optional<Board> searchForBoard(Board board) {
		Board boardFound = (Board) sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.add(Restrictions.eq("name", board.getName()))
				.uniqueResult();
		return Optional.ofNullable(boardFound);
	}

	private ValidationReport validateBoardHasRequiredFields(Board board) {
		if (board == null) {
			return new ValidationReport("No board has been received");
		}
		return this.validateBoardName(board);
	}
	
	private ValidationReport validateBoardName(Board board)  {
		if (board.getName() == null) {
			return new ValidationReport("Board name has to be specified");
		}
		return validateThatBoardNameIsNotEmpty(board);
	}
	
	private ValidationReport validateThatBoardNameIsNotEmpty(Board board) {
		if (board.getName().equals("")) {
			return new ValidationReport("Board name can not be empty");
		}
		return new ValidationReport();
	}
	
}
