package com.aweshome.dailyboard.core.validation;


import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.model.Board;

@Transactional
public class NewBoardValidator implements Validator<Board> {


	private SessionFactory sessionFactory;

	public NewBoardValidator(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	public ValidationReport validate(Board board){
		ValidationReport report = this.validateBoardHasRequiredFields(board);
		return report.hasIssues() ? report : this.validateBoardDoesntExistYet(board);
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
		if (name.trim().equals("")) {
			return new ValidationReport("Board name can not be empty");
		}
		return new ValidationReport();
	}

    private ValidationReport validateBoardDoesntExistYet(Board board) {
        boolean boardExists = this.sessionFactory.getCurrentSession()
                .createCriteria(Board.class)
                .add(Restrictions.eq("name", board.getName()))
                .uniqueResult() != null;
        return boardExists ? new ValidationReport("There is already a board registered with the name: " + board.getName()) : new ValidationReport();
    }
}
