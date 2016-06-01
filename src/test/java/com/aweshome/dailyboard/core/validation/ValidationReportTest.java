package com.aweshome.dailyboard.core.validation;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import jersey.repackaged.com.google.common.collect.Sets;

public class ValidationReportTest {
	
	@Test
	public void isKeepingTrackOfIssues() {
		ValidationReport report = new ValidationReport();
		this.assertStateOfValidationReport(report, new HashSet<String>());
		
		String issue = "First issue";
		report.addIssue(issue);
		this.assertStateOfValidationReport(report, Sets.newHashSet(issue));
	}

	@Test
	public void mergeIssuesOfOtherToThisValidationReport() {
		ValidationReport report = new ValidationReport();
		String issue = "First issue";
		report.addIssue(issue);
		
		ValidationReport otherReport = new ValidationReport();
		String otherIssue = "Other issue";
		otherReport.addIssue(otherIssue);
		
		ValidationReport mergeReport = report.merge(otherReport);
		assertSame(report, mergeReport);
		this.assertStateOfValidationReport(report, Sets.newHashSet(issue, otherIssue));
	}
	
	@Test
	public void ignoreEmptyOrNullIssuesToAdd() {
		ValidationReport report = new ValidationReport();
		report.addIssue("");
		report.addIssue(null);
		this.assertStateOfValidationReport(report, new HashSet<String>());
	}
	
	private void assertStateOfValidationReport(ValidationReport report, Set<String> expectedIssues) {
		boolean expectedToHaveIssues = expectedIssues.size() > 0;
		assertEquals(expectedToHaveIssues, report.hasIssues());
		Set<String> issuesFromReport = report.getIssues();
		assertEquals(expectedIssues.size(), issuesFromReport.size());
		assertEquals(expectedIssues, issuesFromReport);
	}

}
