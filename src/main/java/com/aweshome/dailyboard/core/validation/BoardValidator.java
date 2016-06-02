package com.aweshome.dailyboard.core.validation;


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
		return sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.add(Restrictions.eq("name", board.getName()))
				.uniqueResult() != null;
	}

	private ValidationReport validateBoardHasRequiredFields(Board board) {
		if (board == null) {
			return new ValidationReport("No board has been received");
		}
		return this.validateBoardName(board.getName());
	}
	
	private ValidationReport validateBoardName(String name)  {
		if (name == null) {
			return new ValidationReport("Board name has to be specified");
		}
		return validateThatBoardNameIsNotEmpty(name);
	}
	
	private ValidationReport validateThatBoardNameIsNotEmpty(String name) {
		if (name.equals("")) {
			return new ValidationReport("Board name can not be empty");
		}
		return new ValidationReport();
	}
	
}
