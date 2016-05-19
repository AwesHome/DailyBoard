package com.aweshome.dailyboard.core;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.model.Board;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Board getBoard(Long id) {
		
		Board board = (Board) sessionFactory.getCurrentSession()
				.createCriteria(Board.class)
				.add(Restrictions.idEq(id))
				.uniqueResult();
		return board;
	}

	@Override
	public Board createBoard(Board board) {
		sessionFactory.getCurrentSession().save(board);
		return board;
		
	}

}
