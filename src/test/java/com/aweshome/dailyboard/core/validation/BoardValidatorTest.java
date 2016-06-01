package com.aweshome.dailyboard.core.validation;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.TestSetUpUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import jersey.repackaged.com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "../../spring-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(connection="dataSource", value="../BasicScenario.xml")
public class BoardValidatorTest {
	
	@Autowired
	private BoardValidator target;
	
	@Test
	public void validateNullBoard(){
		ValidationReport report = target.validateBoardToBeCreated(null);	
		this.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("No board has been received"));
	}
	
	@Test
	public void validateBoardWithNullName() {
		ValidationReport report = target.validateBoardToBeCreated(TestSetUpUtils.getBoard(null, null));
		this.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Board name has to be specified"));
	}
	
	@Test
	public void validateBoardWithEmptyName(){
		ValidationReport report = target.validateBoardToBeCreated(TestSetUpUtils.getBoard(null, ""));
		this.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Board name can not be empty"));
	}
	
	@Test
	public void validateBoardWithExistentName() {
		String boardName = "Main Board";
		ValidationReport report = target.validateBoardToBeCreated(TestSetUpUtils.getBoard(null, boardName));
		this.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("There is already a board registered with the name: " + boardName));
	}
	
	@Test
	public void validateBoardWithNoIssues() {
		ValidationReport report = target.validateBoardToBeCreated(TestSetUpUtils.getBoard(null, "Board"));
		this.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet());
	}
	
	private void assertReportHasOnlyExpectedIssues(ValidationReport report, Set<String> expectedIssues) {
		boolean expectedToHaveIssues = expectedIssues.size() > 0;
		assertEquals(expectedToHaveIssues, report.hasIssues());
		assertEquals(expectedIssues.size(), report.getIssues().size());
		assertEquals(expectedIssues, report.getIssues());
	}
}
