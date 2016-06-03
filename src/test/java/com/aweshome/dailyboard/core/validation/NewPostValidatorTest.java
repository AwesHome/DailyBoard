package com.aweshome.dailyboard.core.validation;

import com.aweshome.dailyboard.TestUtils;
import jersey.repackaged.com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by carolinamoya on 18/02/2017.
 */
public class NewPostValidatorTest {

    NewPostValidator target = new NewPostValidator();

    @Test
    public void returnsIssueForNullPost(){
        ValidationReport report = target.validate(null);
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("No post has been received"));
    }

    @Test
    public void returnsIssueForPostWithNullContent(){
        ValidationReport report = target.validate(TestUtils.getPost(null, null));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Post content has to be specified"));
    }

    @Test
    public void returnsIssueForPostWithEmptyContent(){
        ValidationReport report = target.validate(TestUtils.getPost(null, ""));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Post content can not be empty"));
    }

    @Test
    public void returnsIssueForPostWithBlankContent(){
        ValidationReport report = target.validate(TestUtils.getPost(null, "  "));
        TestUtils.assertReportHasOnlyExpectedIssues(report, Sets.newHashSet("Post content can not be empty"));
    }

    @Test
    public void returnsClearReportForValidPost() {
        ValidationReport report = target.validate(TestUtils.getPost(null, "post content"));
        Assert.assertFalse(report.hasIssues());
    }
}
