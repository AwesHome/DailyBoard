package com.aweshome.dailyboard.core;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aweshome.dailyboard.model.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "../spring-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(connection="dataSource", value="BasicScenario.xml")
public class PostServiceImplTest {

	@Autowired
	private PostServiceImpl target;
	
	@Test
	public void findPostTest() {
		Optional<Post> post = target.findPost(1L);
		assertTrue(post.isPresent());
		Post postFound = post.get();
		assertEquals(Long.valueOf(1L), postFound.getId());
		assertEquals("first post content", postFound.getContent());
	}
	
}
