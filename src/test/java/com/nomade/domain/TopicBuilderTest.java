/**
 * 
 */
package com.nomade.domain;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nomade.tools.TopicBuilder;

/**
 * @author bwa
 *
 */
@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:META-INF/spring/applicationContext*.xml"})
@Transactional
public class TopicBuilderTest {

//	@Test
	public void testTopicBuilder(){
		UserNomade nomade = new UserNomade();
		TopicBuilder topicBuilder = TopicBuilder.get().addTopic("Africa Travels", "travales to africa", Confidentiality.Publique, nomade)
			.addTopic("Europa", "Travels accross europe", Confidentiality.Publique,nomade )
			.addTopic("Asia", "Travels Across Asia", Confidentiality.Publique, nomade);
		List<Topic> topics = topicBuilder.getTopics();
		System.out.println(topics.size());
	}
}
