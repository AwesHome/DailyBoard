package com.aweshome.dailyboard.core;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.TestSetUpUtils;
import com.aweshome.dailyboard.core.validation.BoardValidator;
import com.aweshome.dailyboard.core.validation.ValidationException;
import com.aweshome.dailyboard.core.validation.ValidationReport;
import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import jersey.repackaged.com.google.common.collect.Sets;

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
	public void getBoardTest() {
		Long expectedBoardId = 1L;
		Optional<Board> board = target.findBoard(expectedBoardId);
		assertBoardMatchesScenario(expectedBoardId, board);
		assertPostsMatchScenario(board);
	}
	
	@Test
	public void createBoardTest() {
		this.mockBoardValidatorToReturn();
		Board boardToBeCreated = TestSetUpUtils.getBoard(null, "Board being saved");
		try {
			Board createdBoard = target.createBoard(boardToBeCreated);
			assertCreatedBoardMaintainedAllFieldsAndHasId(boardToBeCreated, createdBoard);
			Optional<Board> boardObtainedBySearch = target.findBoard(createdBoard.getId());
			assertThatBoardObtainedBySearchIsTheCreatedBoard(boardObtainedBySearch, createdBoard);
		} catch (ValidationException e) {
			fail();
		}
	}
	
	@Test
	public void createBoardThrowsExceptionWhenThereIsOneValidationIssue() throws ValidationException {
		String issue = "Board name can't be empty";
		String expectedMessage = issue;
		assertThatCreateBoardThrowsExceptionWithExpectedMessageForIssues(expectedMessage, issue);
	}
	
	@Test
	public void createBoardThrowsExceptionWhenThereAreValidationIssues() throws ValidationException {
		String firstIssue = "Board name can't be empty";
		String secondIssue = "Board field with invalid data";
		String expectedMessage = firstIssue + ", " + secondIssue;
		assertThatCreateBoardThrowsExceptionWithExpectedMessageForIssues(expectedMessage, firstIssue, secondIssue);
	}
	
	private void assertThatCreateBoardThrowsExceptionWithExpectedMessageForIssues(String expectedMessage, String...issues) throws ValidationException {
		this.mockBoardValidatorToReturn(issues);
		thrown.expect(ValidationException.class);
		thrown.expectMessage(expectedMessage);
		target.createBoard(new Board());
	}
	
	private void mockBoardValidatorToReturn(String...issues) {
		ValidationReport validationReport = mockValidationReportToReturn(issues);
		mockBoardValidatorToReturnValidationReport(validationReport);
	}

	private ValidationReport mockValidationReportToReturn(String... issues) {
		ValidationReport validationReport= mock(ValidationReport.class);
		boolean hasIssues = issues.length > 0;
		when(validationReport.hasIssues()).thenReturn(hasIssues);
		when(validationReport.getIssues()).thenReturn(Sets.newHashSet(issues));
		return validationReport;
	}

	private void mockBoardValidatorToReturnValidationReport(ValidationReport validationReport) {
		BoardValidator boardValidator = mock(BoardValidator.class);
		when(boardValidator.validateBoardToBeCreated(any(Board.class))).thenReturn(validationReport);
		target.setBoardValidator(boardValidator);
	}
	

	private void assertBoardMatchesScenario(Long expectedBoardId, Optional<Board> board) {
		assertTrue(board.isPresent());
		assertEquals("Main Board", board.get().getName());
		assertEquals(expectedBoardId, board.get().getId());
	}

	private void assertPostsMatchScenario(Optional<Board> board) {
		Long expectedIdForFirstPost = 1L;
		Long expectedIdForSecondPost = 2L;
		List<Post> posts = board.get().getPosts();
		assertEquals(2, posts.size());
		assertEquals(expectedIdForFirstPost, posts.get(0).getId());
		assertEquals(expectedIdForSecondPost, posts.get(1).getId());
		assertEquals("first post content", posts.get(0).getContent());
		assertEquals("second post content", posts.get(1).getContent());
	}
	
	private void assertCreatedBoardMaintainedAllFieldsAndHasId(Board boardToBeCreated, Board createdBoard) {
		assertNotNull(createdBoard);
		assertNotNull(createdBoard.getId());
		assertEquals(boardToBeCreated.getName(), createdBoard.getName());
	}
	
	private void assertThatBoardObtainedBySearchIsTheCreatedBoard(Optional<Board> boardObtainedBySearch, Board createdBoard) {
		assertTrue(boardObtainedBySearch.isPresent());
		assertEquals(createdBoard, boardObtainedBySearch.get());
	}
}

