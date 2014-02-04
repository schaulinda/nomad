package com.nomade.domain;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Topic.class, transactional = false)
public class TopicIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
