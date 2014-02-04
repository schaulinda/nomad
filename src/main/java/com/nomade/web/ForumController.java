package com.nomade.web;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nomade.domain.BeanForumManager;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.service.DiscussionService;
import com.nomade.service.SubTopicService;
import com.nomade.service.TopicService;

@Controller
@RequestMapping("/forum")
public class ForumController {
	private static final String HELLO_WORLD_FORUM_MESSAGED = "Hello World Forum";

	@Autowired
	private TopicService topicService;
	
	@Autowired
	private SubTopicService subTopicService;
	
	@Autowired
	private DiscussionService  discussionService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showForumMainPage(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		List<Topic> topics = topicService.findAllTopics();
		List<SubTopic> subTopics = subTopicService.findAllSubTopics();
		BeanForumManager beanForumManager = new BeanForumManager();
		beanForumManager.setTopics(new HashSet<Topic>(topics));
		beanForumManager.setSubTopics(new HashSet<SubTopic>(subTopics));
		uiModel.addAttribute("message", HELLO_WORLD_FORUM_MESSAGED);
		uiModel.addAttribute("beanForumManager", beanForumManager);
		return "public/forum/mainpage";
	}
}
