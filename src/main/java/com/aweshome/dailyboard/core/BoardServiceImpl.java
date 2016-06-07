package com.aweshome.dailyboard.core;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.core.validation.BoardValidator;
import com.aweshome.dailyboard.core.validation.ValidationException;
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
	public Board createBoard(Board board) throws ValidationException {
		this.validateBoardToBeCreated(board);
		board.setLastModified(new Date());
		sessionFactory.getCurrentSession().save(board);
		return board;
		
	}

	@Override
	public Optional<Board> getFirstBoard() {
		Criteria criteria = this.getFirstBoardCriteria();
				return Optional.ofNullable((Board) criteria.uniqueResult());
	}
	
	@Override
	public Long getNextBoardId(Board board) {
		Long id = (Long) sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.addOrder(Order.asc("lastModified"))
				.addOrder(Order.asc("id"))
				.add(Restrictions.or(
						Restrictions.and(
								Restrictions.eq("lastModified", board.getLastModified()), 
								Restrictions.gt("id", board.getId())),
						Restrictions.gt("lastModified", board.getLastModified())
						))
				.setProjection(Projections.id())
				.setMaxResults(1)
				.uniqueResult();
		if (id == null) {
			id = (Long) this.getFirstBoardCriteria().setProjection(Projections.id()).uniqueResult();
		}
		return id;
	}
	
	private Criteria getFirstBoardCriteria() {
		return sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.addOrder(Order.asc("lastModified"))
				.addOrder(Order.asc("id"))
				.setMaxResults(1);
	}

	private void validateBoardToBeCreated(Board board) throws ValidationException {
		ValidationReport report = this.boardValidator.validateBoardToBeCreated(board);
		if (report.hasIssues()) {
			String message = this.buildMessageForException(report.getIssues());
			throw new ValidationException(message);
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
