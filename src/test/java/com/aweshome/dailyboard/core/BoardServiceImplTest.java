package com.aweshome.dailyboard.core;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;

import com.aweshome.dailyboard.controller.PostDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.TestUtils;
import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "../spring-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(connection="dataSource", value="BasicScenario.xml")
public class BoardServiceImplTest {
	
	@Autowired
	private BoardServiceImpl target;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void persistsNewBoards() {
		Board[] boardsToBeCreated = {
				TestUtils.getBoard(null, "Board being saved"),
				TestUtils.getBoard(null, "Board with posts", "first post", "secondPost")
				};
		for (Board boardToBeCreated : boardsToBeCreated) {
			try {
				Board createdBoard = target.createBoard(boardToBeCreated);
                assertNotNull(createdBoard);
                assertNotNull(createdBoard.getId());
                assertEquals(boardToBeCreated.getName(), createdBoard.getName());
				Optional<Board> boardObtainedBySearch = target.findBoard(createdBoard.getId());
                assertTrue(boardObtainedBySearch.isPresent());
                assertEquals(createdBoard, boardObtainedBySearch.get());
			} catch (ValidationException e) {
				fail("Shouldn't have found validation issues");
			}
		}
	}
	
	@Test
	public void createBoardThrowsExceptionWhenThereIsOneValidationIssue() throws ValidationException {
		String expectedMessage = "Board name can not be empty";
		thrown.expect(ValidationException.class);
		thrown.expectMessage(expectedMessage);
		target.createBoard(TestUtils.getBoard(null, ""));
	}

    @Test
    public void createBoardThrowsExceptionWhenThereAreValidationIssues() throws ValidationException {
        String expectedMessage = "Board name can not be empty, Post content can not be empty";
        thrown.expect(ValidationException.class);
        thrown.expectMessage(expectedMessage);
        target.createBoard(TestUtils.getBoard(null, "", ""));
    }

    @Test
    public void createPostThrowsExceptionWhenThereIsOneValidationIssue() throws ValidationException {
        String expectedMessage = "Post content can not be empty";
        thrown.expect(ValidationException.class);
        thrown.expectMessage(expectedMessage);
        target.createPostForBoard(TestUtils.getPost(null, ""), 1L);
    }

	@Test
	public void createsNewPostInBoard() throws ValidationException {
		Post postToBeCreated = new Post("this post content");
		Long boardId = Long.valueOf(3);
		Post createdPost = target.createPostForBoard(postToBeCreated, boardId);
		
		assertNotNull(createdPost);
		assertNotNull(createdPost.getId());
		assertEquals(postToBeCreated.getContent(), createdPost.getContent());
		
		Optional<Board> board = target.findBoard(boardId);
		assertTrue(board.get().getPosts().contains(createdPost));
	}


    @Test
    public void findsSpecificBoard() {
        Long expectedBoardId = 1L;
        Optional<Board> board = target.findBoard(expectedBoardId);
        assertTrue(board.isPresent());
        assertEquals("Main Board", board.get().getName());
        assertEquals(expectedBoardId, board.get().getId());
        List<Post> posts = board.get().getPosts();
        assertEquals(2, posts.size());
        assertEquals(Long.valueOf(1L), posts.get(0).getId());
        assertEquals(Long.valueOf(2L), posts.get(1).getId());
        assertEquals("first post content", posts.get(0).getContent());
        assertEquals("second post content", posts.get(1).getContent());
    }

    @Test
	public void findsBoardWithEarliestModifiedDateAndLowestIdAsFirst() {
		Optional<Board> board = target.getFirstBoard();
		assertTrue(board.isPresent());
		assertEquals(Long.valueOf(2L), board.get().getId()); // earliest modified date and lowest id on BasicScenario.xml
	}
	
	@Test
	public void findsNextBoard() {
		Optional<Board> firstBoard = target.getFirstBoard();
		Long secondBoardId = target.getNextBoardId(firstBoard.get());
		assertEquals(Long.valueOf(3L), secondBoardId);
		
		Optional<Board> secondBoard = target.findBoard(secondBoardId);
		Long thirdBoardId = target.getNextBoardId(secondBoard.get());
		assertEquals(Long.valueOf(1L), thirdBoardId);
	}

	@Test
	public void getNextBoardReturnsFirstBoardWhenThereIsNoNext() {
		Optional<Board> lastBoard = target.findBoard(1L); // latest modified on BasicScenario.xml
		Long nextBoardId = target.getNextBoardId(lastBoard.get());
		assertEquals(Long.valueOf(2L), nextBoardId); // earliest modified and lowest id on BasicScenario.xml
	}

}


