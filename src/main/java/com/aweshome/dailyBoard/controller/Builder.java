package com.aweshome.dailyBoard.controller;

public interface Builder<E, O> {
	
	public O buildDTO(E input);
	
	public E buildEntity(O input);

}
