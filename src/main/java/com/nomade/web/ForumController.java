package com.nomade.web;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.nomade.domain.BeanSubTopicView;
import com.nomade.domain.BeanTopicManager;
import com.nomade.domain.Comment;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.security.SecurityUtil;
import com.nomade.service.DiscussionService;
import com.nomade.service.SubTopicService;
import com.nomade.service.TopicService;
import com.nomade.tools.CollectionUtil;

@Controller
@RequestMapping("/forum")
public class ForumController {
	private static final String FORUM_SUBTOPICS = "/forum/subtopics";

	private static final String FORUM_TOPICS = "/forum/topics";

	@Autowired
	private TopicService topicService;

	@Autowired
	private SubTopicService subTopicService;

	@Autowired
	private DiscussionService discussionService;

	@Autowired
	SecurityUtil securityUtil;

	@RequestMapping(method = RequestMethod.GET)
	public String showForumMainPage(Model uiModel) {
		if(!securityUtil.isUserLogged()){
			
		}
    		List<Topic> topics = topicService.findAllTopics();
    		List<BeanTopicManager> topicBeans = new ArrayList<BeanTopicManager>();
    		for (Topic topic : topics) {
    			List<SubTopic> subTopics = subTopicService.findByParentTopic(topic);
    			topic.setSubTopics(new HashSet<SubTopic>(subTopics));
    			BeanTopicManager beanTopicManager = new BeanTopicManager();
    			beanTopicManager.setTopic(topic);
    			beanTopicManager.setNbOfDiscussion(topicService
    					.countDiscussion(topic));
    			beanTopicManager.setNbOfMessages(topicService.countMessages(topic));
    			beanTopicManager.setLastMessageDate(topicService
    					.getLastMessageDate(topic));
    			topicBeans.add(beanTopicManager);
    		}
    		uiModel.addAttribute("topicBeans", topicBeans);
		return "public/forum/mainpage";
	}

	@RequestMapping(value = "/topics", method = RequestMethod.GET)
	public String showTopics(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,Model uiModel) {
		return "redirect:/forum/";
	}

	@RequestMapping(value = "/topics/{id}", method = RequestMethod.GET)
	public String showTopicView(@PathVariable("id") BigInteger topicId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			Model uiModel) {
		Topic topic = topicService.findTopic(topicId);

		if (topic == null) {
			return "redirect:" + FORUM_TOPICS;
		}
		List<SubTopic> subTopics =  null;
		if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
    		subTopics = subTopicService.findByParentTopic(topic,firstResult,sizeNo);
    		float nrOfPages = (float) subTopicService.countAllSubTopics() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		}else {
    		subTopics = subTopicService.findByParentTopic(topic);
		}
		List<BeanSubTopicView> subTopicToBeanSubTopic = subTopicService
				.convertSubTopicToBeanSubTopic(subTopics);

		BeanTopicManager beanTopicManager = new BeanTopicManager();
		beanTopicManager.setTopic(topic);
		beanTopicManager.setNbOfDiscussion(topicService.countDiscussion(topic));
		beanTopicManager.setNbOfMessages(topicService.countMessages(topic));
		beanTopicManager.setLastMessageDate(topicService
				.getLastMessageDate(topic));

		uiModel.addAttribute("topicBean", beanTopicManager);
		uiModel.addAttribute("subTopicToBeans", subTopicToBeanSubTopic);
		uiModel.addAttribute("subtopicModel", new SubTopic());
		populateModel(uiModel);
		return "public/forum/topicView";
	}

	@RequestMapping(value = "/topics", method = RequestMethod.POST)
	public String createTopic(@Valid Topic topic, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		topic.setCreated(new Date());
		topic.setNomade(securityUtil.getUserNomade());
		topic = topicService.updateTopic(topic);
		return "redirect:/topics/"
				+ encodeUrlPathSegment(topic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/topics", method = RequestMethod.PUT)
	public String updateTopic(@Valid Topic topic, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (topicService.findTopic(topic.getId()) == null) {
			throw new RuntimeException(
					"You are trying to update an invalid/unexisting topic");
		}
		topic = topicService.updateTopic(topic);
		return "redirect:/topics/"
				+ encodeUrlPathSegment(topic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/topics/{id}", method = RequestMethod.DELETE)
	public String deleteTopic(@PathVariable("id") BigInteger topicId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Topic topic = topicService.findTopic(topicId);
		if (topic == null) {
			throw new RuntimeException(
					"Sorry we couldn't found the topics you are looking for");
		}
		/*
		 * if(topic.getNomade().equals(securityUtil.getUserNomade()) ||
		 * isAllowed(securityUtil.getUserNomade())){
		 * topicService.deleteTopic(topic); }
		 */
		return "redirect:/topics";
	}

	/* subtopics */
	@RequestMapping(value = "/subtopics")
	public String showSubTopics(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		return "redirect:" + FORUM_TOPICS;
	}

	@RequestMapping(value = "/subtopics/{id}")
	public String showSubTopicView(@PathVariable("id") BigInteger subTopicId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		if (subTopic == null) {
			return "redirect:" + FORUM_SUBTOPICS;
		}
		if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
    		List<Discussion> discussions = discussionService.findBySubTopic(subTopic,firstResult,sizeNo);    	
    		float nrOfPages = (float) subTopicService.countAllSubTopics() / sizeNo;
    		uiModel.addAttribute("discussions", discussions);
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		}else {
			List<Discussion> discussions = discussionService.findBySubTopic(subTopic);    	
    		uiModel.addAttribute("discussions", discussions);
		}
		int numberOfDiscussions = discussionService.countDiscussion(subTopic);
		int numberOfMessages = discussionService.countMessages(subTopic);
		Date lastMessageDate = discussionService.getLastMessageDate(subTopic);
		uiModel.addAttribute("subTopic", subTopic);
		uiModel.addAttribute("numberOfDiscussion", numberOfDiscussions);
		uiModel.addAttribute("numberOfMessages", numberOfMessages);
		uiModel.addAttribute("lastMessageDate", lastMessageDate);
		uiModel.addAttribute("discussionModel", new Discussion());
		uiModel.addAttribute("subTopicModel", new SubTopic());
		populateModel(uiModel);
		return "public/forum/subTopicView";
	}

	@RequestMapping(value = "/topics/{parentTopicId}/subtopics/{parentSubTopicId}/add", method = RequestMethod.POST)
	public String createSubTopic(@Valid SubTopic subTopic,
			@PathVariable("parentSubTopicId") BigInteger parentSubTopicId,
			@PathVariable("parentTopicId") BigInteger parentTopic,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (parentSubTopicId != null) {
			SubTopic findSubTopic = subTopicService
					.findSubTopic(parentSubTopicId);
			subTopic.setParentSubTopic(findSubTopic);
		}
		if (parentTopic != null) {
			Topic topic = topicService.findTopic(parentTopic);
			subTopic.setParentTopic(topic);
		}
		subTopic.setNomade(securityUtil.getUserNomade());
		subTopic.setCreated(new Date());
		subTopic = subTopicService.updateSubTopic(subTopic);
		return "redirect:/forum/subtopics/"
				+ encodeUrlPathSegment(subTopic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/subtopics", method = RequestMethod.PUT)
	public String updateSubTopic(@Valid SubTopic subTopic,
			@RequestParam("parentSubTopicId") BigInteger parentSubTopicId,
			@RequestParam("parentTopicId") BigInteger parentTopic,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (parentSubTopicId != null) {
			SubTopic findSubTopic = subTopicService
					.findSubTopic(parentSubTopicId);
			subTopic.setParentSubTopic(findSubTopic);
		}
		if (parentTopic != null) {
			Topic topic = topicService.findTopic(parentTopic);
			subTopic.setParentTopic(topic);
		}
		subTopic = subTopicService.updateSubTopic(subTopic);
		return "redirect:/subtopics/"
				+ encodeUrlPathSegment(subTopic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/subtopics/{id}", method = RequestMethod.DELETE)
	public String deleteSubTopic(@PathVariable("id") BigInteger subTopicId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		if (subTopic == null) {
			throw new RuntimeException(
					"You are trying to update an invalid/unexisting topic");
		}
		subTopicService.deleteSubTopic(subTopic);
		return "redirect:" + FORUM_SUBTOPICS;
	}

	/* Discussions */
	@RequestMapping(value = "/discussions", method = RequestMethod.GET)
	public String showDiscussions(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,Model uiModel) {
		return "redirect:" + FORUM_SUBTOPICS;
	}

	/**
	 * show discussion, with pagination on discussions comments.
	 * @param discussionId
	 * @param page
	 * @param size
	 * @param sortFieldName
	 * @param sortOrder
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "/discussions/{id}", method = RequestMethod.GET)
	public String showDiscussion(@PathVariable("id") BigInteger discussionId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		if (discussion == null) {
			return "redirect:/forum/discussions";
		}
		uiModel.addAttribute("discussion", discussion);
		uiModel.addAttribute("numberOfMessages", discussion.getComments()
				.size());
		Comment lastComment = sortCommentByDateAndGetLastComment(new ArrayList<Comment>(
				discussion.getComments()));
		Date lastMessageDate = null;
		if (lastComment != null) {
			lastMessageDate = lastComment.getCreated();
		}
		uiModel.addAttribute("lastMessageDate", lastMessageDate);
		uiModel.addAttribute("commentModel", new Comment());
		populateModel(uiModel);
		return "public/forum/discussionView";
	}

	@RequestMapping(value = "/subtopics/{subTopicId}/discussions", method = RequestMethod.POST)
	public String addDiscussion(@Valid Discussion discussion,
			@PathVariable("subTopicId") BigInteger subTopicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		discussion.setSubTopic(subTopic);
		discussion.setNomade(securityUtil.getUserNomade());
		discussion.setCreated(new Date());
		discussion.setComments(new HashSet<Comment>());
		// actually, the #updateDiscussion method, save the object and return
		// the saved object.
		discussion = discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussion.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/subtopics/{subTopicId}/discussions", method = RequestMethod.PUT)
	public String updateDiscussion(@Valid Discussion discussion,
			@PathVariable("subTopicId") BigInteger subTopicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		Discussion discussion2 = discussionService.findDiscussion(discussion
				.getId());
		if (discussion2 == null) {
			throw new RuntimeException(
					"You are trying to update an invalid/unexisting discussion");
		}
		discussion.setSubTopic(subTopic);
		// actually, the #updateDiscussion method, save the object and return
		// the saved object.
		discussion = discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussion.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/discussions/{id}", method = RequestMethod.DELETE)
	public String deleteDiscussion(@PathVariable("id") BigInteger id,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(id);
		if (discussion == null) {
			throw new RuntimeException(
					"You are trying to delete an invalid/unexisting discussion");
		}
		discussionService.deleteDiscussion(discussion);
		return "redirect:/forum/discussions";
	}

	/* Comments */
	@RequestMapping(value = "/discussions/{discussionId}/comments", method = RequestMethod.POST)
	public String addComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@Valid Comment comment, Model uiModel,
			HttpServletRequest httpServletRequest) {
		comment.setBusinessId(discussionId
				+ RandomStringUtils.randomAlphanumeric(4));
		Discussion discussion = discussionService.findDiscussion(discussionId);
		discussion.getComments().add(comment);
		discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussionId.toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/discussions/{discussionId}/comments", method = RequestMethod.PUT)
	public String updateComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@Valid Comment comment, Model uiModel,
			HttpServletRequest httpServletRequest) {
		comment.setBusinessId(discussionId
				+ RandomStringUtils.randomAlphanumeric(4));
		Discussion discussion = discussionService.findDiscussion(discussionId);
		for (Comment c : discussion.getComments()) {
			if (c.getBusinessId().equals(comment.getBusinessId())) {
				c.setCommentaire(comment.getCommentaire());
				break;
			}
		}
		discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussionId.toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/discussions/{discussionId}/comments/{businessId}", method = RequestMethod.DELETE)
	public String deleteComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@PathVariable("businessId") String businessId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		for (Comment c : discussion.getComments()) {
			if (c.getBusinessId().equals(businessId)) {
				discussion.getComments().remove(c);
				break;
			}
		}
		discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussionId.toString(),
						httpServletRequest);
	}

	/**
	 * @param pathSegment
	 * @param httpServletRequest
	 * @return
	 */
	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

	/**
	 * @param comments
	 * @return
	 */
	public static Comment sortCommentByDateAndGetLastComment(
			List<Comment> comments) {
		Collections.sort(comments, new Comparator<Comment>() {
			@Override
			public int compare(Comment comment1, Comment comment2) {
				return comment1.getCreated().compareTo(comment2.getCreated());
			}
		});
		return CollectionUtil.getLastElement(comments);
	}

	public void populateModel(Model uiModel) {
		uiModel.addAttribute("confidentialities",
				Arrays.asList(Confidentiality.values()));
	}
}
