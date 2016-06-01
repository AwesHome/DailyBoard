package com.aweshome.dailyboard.core.validation;

import java.util.HashSet;
import java.util.Set;

public class ValidationReport {
	
	private Set<String> issues = new HashSet<String>();
	
	public ValidationReport(String... issues) {
		for (String issue : issues) {
			this.addIssue(issue);
		}
	}
	
	public void addIssue(String issue) {
		if (issue != null && !issue.equals("")) {
			issues.add(issue);
		}
	}

	public Set<String> getIssues() {
		return new HashSet<String>(this.issues);
	}
	
	public ValidationReport merge(ValidationReport otherReport) {
		this.issues.addAll(otherReport.getIssues());
		return this;
	}

	public boolean hasIssues() {
		return !this.issues.isEmpty();
	}

}
