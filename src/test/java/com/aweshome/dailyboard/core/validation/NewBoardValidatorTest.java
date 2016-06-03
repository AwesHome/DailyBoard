package com.aweshome.dailyboard.core.validation;

import static org.junit.Assert.*;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.TestUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import jersey.repackaged.com.google.common.collect.Sets;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "../../spring-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(connection="dataSource", value="../BasicScenario.xml")
public class NewBoardValidatorTest {
	
	@Autowired
	private SessionFactory sessionFactory;

	private NewBoardValidator target;

	@Before
	public void setUp() {
		this.target = new NewBoardValidator(sessionFactory);
	}

	@Test
	public void returnsIssueForNullBoard(){
		ValidationReport report = target.validate(null);
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("No board has been received"));
	}
	
	@Test
	public void returnsIssueForBoardWithNullName() {
		ValidationReport report = target.validate(TestUtils.getBoard(null, null));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Board name has to be specified"));
	}
	
	@Test
	public void returnsIssueForBoardWithEmptyName(){
		ValidationReport report = target.validate(TestUtils.getBoard(null, ""));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Board name can not be empty"));
	}

	@Test
	public void returnsIssueForBoardWithBlankName(){
		ValidationReport report = target.validate(TestUtils.getBoard(null, "   "));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Board name can not be empty"));
	}

	@Test
	public void returnsIssueForBoardWithExistentName() {
		String boardName = "Main Board"; //Exists in BasicScenario.xml
		ValidationReport report = target.validate(TestUtils.getBoard(null, boardName));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("There is already a board registered with the name: " + boardName));
	}
	
	@Test
	public void returnClearReportForBoardWithNoIssues() {
		ValidationReport report = target.validate(TestUtils.getBoard(null, "Board"));
		TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet());
	}
}
