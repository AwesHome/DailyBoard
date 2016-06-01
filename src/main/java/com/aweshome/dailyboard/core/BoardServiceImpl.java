package com.aweshome.dailyboard.core;

import java.util.Collection;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.core.validation.BoardValidator;
import com.aweshome.dailyboard.core.validation.ValidationReport;
import com.aweshome.dailyboard.model.Board;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private BoardValidator boardValidator;
	
	@Override
	public Optional<Board> findBoard(Long id) {
		return Optional.ofNullable(
				(Board) sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.add(Restrictions.idEq(id))
				.uniqueResult());
	}

	@Override
	public Board createBoard(Board board) throws Exception {
		this.validateBoardToBeCreated(board);
		sessionFactory.getCurrentSession().save(board);
		return board;
		
	}

	private void validateBoardToBeCreated(Board board) throws Exception {
		ValidationReport report = this.boardValidator.validateBoardToBeCreated(board);
		if (report.hasIssues()) {
			String message = this.buildMessageForException(report.getIssues());
			throw new Exception(message);
		}
	}

	private String buildMessageForException(Collection<String> issues) {
		String message = String.join(", ", issues);
		return message;
	}

	public void setBoardValidator(BoardValidator boardValidator) {
		this.boardValidator = boardValidator;
		
	}
}
