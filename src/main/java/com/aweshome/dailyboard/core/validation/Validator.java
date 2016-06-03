package com.aweshome.dailyboard.core.validation;

public interface Validator<O> {

	public ValidationReport validate(O object);
}
