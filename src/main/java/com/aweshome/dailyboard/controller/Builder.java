package com.aweshome.dailyboard.controller;

public interface Builder<E, O> {
	
	public O buildDTOFrom(E input);
	
	public E buildEntityFrom(O input);

}
