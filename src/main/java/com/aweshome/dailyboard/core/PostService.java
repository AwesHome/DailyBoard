package com.aweshome.dailyboard.core;

import java.util.Optional;

import com.aweshome.dailyboard.model.Post;

public interface PostService {

	Optional<Post> findPost(Long id);
}
