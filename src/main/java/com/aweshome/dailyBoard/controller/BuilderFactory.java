package com.aweshome.dailyBoard.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.aweshome.dailyBoard.model.Board;
import com.aweshome.dailyBoard.model.Post;

@Component
public class BuilderFactory {

	
	private Map<Class<?>, Builder<?, ?>> buildersMap = new HashMap<>();

	public BuilderFactory() {
		this.buildersMap.put(Board.class, new BoardDTOBuilder(new PostDTOBuilder()));
		this.buildersMap.put(Post.class, new PostDTOBuilder());
	}

	@SuppressWarnings("unchecked")
	public <I, O> Builder<I, O> getBuilder(Class<I>theClass){
		return (Builder<I, O>) this.buildersMap.get(theClass);
	}
	
}