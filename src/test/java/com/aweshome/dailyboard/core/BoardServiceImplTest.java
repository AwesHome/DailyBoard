package com.aweshome.dailyboard.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.TestSetUpUtils;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "../spring-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(connection="dataSource", value="BoardServiceImplTest-scenario.xml")
public class BoardServiceImplTest {
	
	@Autowired
	private BoardServiceImpl target;
	
	@Test
	public void getBoardTest() {
		Board board = target.getBoard(1L);
		
		assertNotNull(board);
		List<Post> posts = board.getPosts();
		assertNotNull(posts.get(0).getContent());
		assertNotEquals("", posts.get(0).getContent());
	}
	
	@Test
	public void createBoardTest() {
		Board board = TestSetUpUtils.getBoard(null, "Board being saved");
		
		Board savedBoard = target.createBoard(board);
		
		assertNotNull(savedBoard);
		assertNotNull(savedBoard.getId());
		assertEquals(board.getName(), savedBoard.getName());
		
		Board obtainedBoard = target.getBoard(savedBoard.getId());
		assertNotNull(obtainedBoard);
		assertEquals(savedBoard.getId(), obtainedBoard.getId());
		assertEquals(savedBoard.getName(), obtainedBoard.getName());
	}

}
