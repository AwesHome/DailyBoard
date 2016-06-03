package com.aweshome.dailyboard.core;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aweshome.dailyboard.model.Post;

@Component
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Optional<Post> findPost(Long id) {
		return Optional.ofNullable((Post)
				sessionFactory.getCurrentSession()
				.createCriteria(Post.class)
				.add(Restrictions.idEq(id))
				.uniqueResult());
	}
	
}
