package com.aweshome.dailyboard.core.validation;

import com.aweshome.dailyboard.model.Board;
import com.aweshome.dailyboard.model.Post;
import org.hibernate.SessionFactory;

/**
 * Created by carolinamoya on 18/02/2017.
 */
public class NewPostValidator implements Validator<Post> {

    @Override
    public ValidationReport validate(Post post) {
        return post != null ? this.validatePostHasRequiredFields(post) : new ValidationReport("No post has been received");
    }

    private ValidationReport validatePostHasRequiredFields(Post post) {
        return post.getContent() != null ? this.validateContent(post.getContent()) : new ValidationReport("Post content has to be specified");
    }

    private ValidationReport validateContent(String content) {
        return content.trim().isEmpty() ? new ValidationReport("Post content can not be empty") : new ValidationReport();
    }
}
