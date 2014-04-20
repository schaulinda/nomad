package com.nomade.web;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
	private static final String PAGE_SIZE = "5";

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
		List<Topic> topics = new ArrayList<Topic>();
		topics = findTopicsDependingIfUserIsLoggedOrNot();
		List<BeanTopicManager> topicBeans = new ArrayList<BeanTopicManager>();
		for (Topic topic : topics) {
			List<SubTopic> subTopics = new ArrayList<SubTopic>();
			if(securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()){
				subTopics =  subTopicService.findByParentTopic(topic);
			}else if(securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()){
				subTopics =  subTopicService.findByParentTopicAndFrozen(topic, Boolean.FALSE);
			}else{
				subTopics = subTopicService.findByParentTopicAndConfidentialityAndFrozen(topic, Confidentiality.Publique, Boolean.FALSE);
			}
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

	private List<Topic> findTopicsDependingIfUserIsLoggedOrNot() {
		List<Topic> topics;
		if (!securityUtil.isUserLogged()) {
			topics = topicService
					.findByConfidentiality(Confidentiality.Publique);
		} else {
			topics = topicService.findAllTopics();
		}
		return topics;
	}

	@RequestMapping(value = "/topics", method = RequestMethod.GET)
	public String showTopics(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			Model uiModel) {
		return "redirect:/forum/";
	}

	/**
	 * <p>
	 * <ul>
	 * <li>if pagination is set and user is logged in, no confidentiality on
	 * subtopics are applied</li>
	 * <li>if pagination is not setted and user is logged in</li>
	 * <li>if pagination is set and user is not logged in, we find only publics
	 * ones</li>
	 * <li>if pagination is not setted and user is not logged in, we find only
	 * publics subtopics</li>
	 * </ul>
	 * </p>
	 * 
	 * @param topicId
	 * @param page
	 * @param size
	 * @param sortFieldName
	 * @param sortOrder
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "/topics/{id}", method = RequestMethod.GET)
	public String showTopicView(
			@PathVariable("id") BigInteger topicId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "imageId", required  = false) String imageId,
			Model uiModel) {
		Topic topic = topicService.findTopic(topicId);
		if (topic == null) {
			return "redirect:" + FORUM_TOPICS;
		}
		List<SubTopic> subTopics = null;
		boolean paginationIsSet = checkIfPaginationIsSet(page, size);
		// declare local params
		int sizeNo = 0;
		int firstResult = 0;
		float nrOfPages = 0;
		// init local 3params if pagination is set
		if (paginationIsSet) {
			sizeNo = size == null ? 10 : size.intValue();
			firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			nrOfPages = (float) subTopicService.countAllSubTopics() / sizeNo;
		}

		if (paginationIsSet && securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()) {
			subTopics = subTopicService.findByParentTopic(topic, firstResult,sizeNo);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		}if (paginationIsSet && securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()) {
			subTopics = subTopicService.findByParentTopicAndFrozen(topic, Boolean.FALSE, firstResult, sizeNo);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else if (paginationIsSet && !securityUtil.isUserLogged()) {
			subTopics = subTopicService.findByParentTopicAndConfidentialityAndFrozen(topic, Confidentiality.Publique, Boolean.FALSE, firstResult, sizeNo);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else if (!paginationIsSet && securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()) {
			subTopics = subTopicService.findByParentTopic(topic);
		}else if (!paginationIsSet && securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()) {
			subTopics = subTopicService.findByParentTopicAndFrozen(topic, Boolean.FALSE);
		} else if (!paginationIsSet && !securityUtil.isUserLogged()) {
			subTopics = subTopicService.findByParentTopicAndConfidentialityAndFrozen(topic, Confidentiality.Publique, Boolean.FALSE);
		}
		List<BeanSubTopicView> subTopicToBeanSubTopic = subTopicService
				.convertSubTopicToBeanSubTopic(subTopics);

		List<Topic> topics = new ArrayList<Topic>();
		topics = findTopicsDependingIfUserIsLoggedOrNot();
		BeanTopicManager beanTopicManager = new BeanTopicManager();
		beanTopicManager.setTopic(topic);
		beanTopicManager.setNbOfDiscussion(topicService.countDiscussion(topic));
		beanTopicManager.setNbOfMessages(topicService.countMessages(topic));
		beanTopicManager.setLastMessageDate(topicService
				.getLastMessageDate(topic));

		uiModel.addAttribute("topicBean", beanTopicManager);
		uiModel.addAttribute("subTopicToBeans", subTopicToBeanSubTopic);
		uiModel.addAttribute("subtopicModel", new SubTopic());
		uiModel.addAttribute("entities", topics);
		uiModel.addAttribute("imageId", imageId);
		populateModel(uiModel);
		return "public/forum/topicView";
	}

	@RequestMapping(value = "/topics", method = RequestMethod.POST)
	public String createTopic(@Valid Topic topic, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		topic.setCreated(new Date());
		topic.setNomade(securityUtil.getUserNomade());
		topic = topicService.updateTopic(topic);
		return "redirect:/forum/topics/"
				+ encodeUrlPathSegment(topic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/topics/{topicId}/form", method = RequestMethod.GET)
	public String showUpdateTopicForm(
			@PathVariable("topicId") BigInteger topicId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Topic topic = topicService.findTopic(topicId);
		uiModel.addAttribute("topic", topic);
		populateModel(uiModel);
		return "public/topics/update";
	}

	@RequestMapping(value = "/topics/update", method = RequestMethod.POST)
	public String updateTopic(@Valid Topic topic, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Topic findTopic = topicService.findTopic(topic.getId());
		if (findTopic == null) {
			throw new RuntimeException(
					"You are trying to update an invalid/unexisting topic");
		}
		findTopic.setConfidentiality(topic.getConfidentiality());
		findTopic.setContent(topic.getContent());
		findTopic.setTitle(topic.getTitle());
		findTopic.setFrozen(topic.getFrozen());
		topic = topicService.updateTopic(findTopic);
		findTopic = null;
		return "redirect:/forum/topics/"
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
		 * }
		 */
		topicService.deleteTopic(topic);
		return "redirect:/forum/topics";
	}

	/*
	@RequestMapping(value = "/topics/{topicId}/", method = RequestMethod.GET)
	public String showTopic(@PathVariable("topicId") BigInteger topicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Topic topic = topicService.findTopic(topicId);
		uiModel.addAttribute("topic", topic);
		populateModel(uiModel);
		return "public/topics/update";
	} */

	/* subtopics */
	@RequestMapping(value = "/subtopics")
	public String showSubTopics(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		return "redirect:" + FORUM_TOPICS;
	}

	@RequestMapping(value = "/subtopics/{id}")
	public String showSubTopicView(
			@PathVariable("id") BigInteger subTopicId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "imageId", required  = false) String imageId,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		if (subTopic == null) {
			return "redirect:" + FORUM_SUBTOPICS;
		}
		// declare local params
		int sizeNo = 0;
		int firstResult = 0;
		float nrOfPages = 0;
		boolean paginationIsSet = checkIfPaginationIsSet(page, size);
		if (paginationIsSet) {
			sizeNo = size == null ? 10 : size.intValue();
			firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			nrOfPages = (float) subTopicService.countAllSubTopics() / sizeNo;
		}
		List<SubTopic> subTopics = new ArrayList<SubTopic>();
		if (paginationIsSet && securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()) {
			List<Discussion> discussions = discussionService.findBySubTopic(subTopic, firstResult, sizeNo);
			uiModel.addAttribute("discussions", discussions);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		}else if (paginationIsSet && securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()) {
			List<Discussion> discussions = discussionService.findBySubTopicAndFrozen(subTopic, Boolean.FALSE, firstResult, sizeNo);
			uiModel.addAttribute("discussions", discussions);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else if (paginationIsSet && !securityUtil.isUserLogged()) {
			List<Discussion> discussions = discussionService.findBySubTopicAndConfidentialityAndFrozen(subTopic, Confidentiality.Publique, Boolean.FALSE, firstResult, sizeNo);
			uiModel.addAttribute("discussions", discussions);
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else if (!paginationIsSet && securityUtil.isUserLogged() && securityUtil.hasAccessToFrozenData()) {
			List<Discussion> discussions = discussionService.findBySubTopic(subTopic);
			uiModel.addAttribute("discussions", discussions);
		} else if (!paginationIsSet && securityUtil.isUserLogged() && !securityUtil.hasAccessToFrozenData()) {
			List<Discussion> discussions = discussionService.findBySubTopicAndFrozen(subTopic, Boolean.FALSE);
			uiModel.addAttribute("discussions", discussions);
		} else if (!paginationIsSet && !securityUtil.isUserLogged()) {
			List<Discussion> discussions = discussionService.findBySubTopicAndConfidentialityAndFrozen(subTopic, Confidentiality.Publique, Boolean.FALSE);
			uiModel.addAttribute("discussions", discussions);
		}
		if(securityUtil.isUserLogged()){
			subTopics = subTopicService.findByParentTopic(subTopic.getParentTopic());
		}else {

			subTopics = subTopicService.findByParentTopicAndConfidentiality(subTopic.getParentTopic(), Confidentiality.Publique);
		}
		int numberOfDiscussions = discussionService.countDiscussion(subTopic);
		int numberOfMessages = discussionService.countMessages(subTopic);
		Date lastMessageDate = discussionService.getLastMessageDate(subTopic);
		uiModel.addAttribute("subTopic", subTopic);
		uiModel.addAttribute("numberOfDiscussion", numberOfDiscussions);
		uiModel.addAttribute("numberOfMessages", numberOfMessages);
		uiModel.addAttribute("lastMessageDate", lastMessageDate);
		Discussion discussion = new Discussion();
		discussion.setPhotoId(imageId);
		uiModel.addAttribute("discussionModel", discussion);
		uiModel.addAttribute("subTopicModel", new SubTopic());
		uiModel.addAttribute("entities", subTopics);
		uiModel.addAttribute("imageId", imageId);
		populateModel(uiModel);
		return "public/forum/subTopicView";
	}

	private boolean checkIfPaginationIsSet(Integer page, Integer size) {
		if(page != null || size != null){
			return true;
		}
		return false;
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
		if(subTopic.getConfidentiality() == null){//mean it was an unauthenticated user, that don't have access to the confidentiality combobox
			subTopic.setConfidentiality(Confidentiality.Publique);
		}
		subTopic.setNomade(securityUtil.getUserNomade());
		subTopic.setCreated(new Date());
		subTopic = subTopicService.updateSubTopic(subTopic);
		return "redirect:/forum/subtopics/"
				+ encodeUrlPathSegment(subTopic.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/subtopics/update", method = RequestMethod.POST)
	public String updateSubTopic(@Valid SubTopic subTopic,BindingResult bindingResult, Model uiModel,HttpServletRequest httpServletRequest) {
		SubTopic subTopic2 = subTopicService.findSubTopic(subTopic.getId());
		subTopic2.setTitle(subTopic.getTitle());
		subTopic2.setContent(subTopic.getContent());
		subTopic2.setParentTopic(subTopic.getParentTopic());
		subTopic2.setConfidentiality(subTopic.getConfidentiality());
		subTopic2.setFrozen(subTopic.getFrozen());
		subTopicService.updateSubTopic(subTopic2);
		subTopic = null;
		return "redirect:/forum/subtopics/"
				+ encodeUrlPathSegment(subTopic2.getId().toString(),
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

	@RequestMapping(value = "/subtopics/{subTopicId}/form", method = RequestMethod.GET)
	public String showUpdateSubTopicForm(
			@PathVariable("subTopicId") BigInteger subTopicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		uiModel.addAttribute("subTopic", subTopic);
		List<Topic> allTopics = new ArrayList<Topic>();
		if(securityUtil.isUserLogged()){
			allTopics= topicService.findAllTopics();
		}else {
			allTopics  = topicService.findByConfidentiality(Confidentiality.Publique);
		}
		Iterator<Topic> iterator = allTopics.iterator();
		while (iterator.hasNext()) {
			Topic topic = (Topic) iterator.next();
			if(topic.getId().equals(subTopic.getParentTopic().getId())){
				iterator.remove();
			}
		}
		allTopics.add(0, subTopic.getParentTopic());
		uiModel.addAttribute("topics", allTopics);
		populateModel(uiModel);
		return "public/subtopics/update";
	}

	@RequestMapping(value = "/subtopics/{subTopicId}/freeze", method = RequestMethod.GET)
	public String freezeSubTopic(
			@PathVariable("subTopicId") BigInteger subTopicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		if(subTopic == null){
			return "redirect:/forum";
		}
		subTopic = subTopicService.freezeSubTopic(subTopic);
		return "redirect:/forum/subtopics/"+subTopic.getId()+"?size=" + PAGE_SIZE;
	}

	@RequestMapping(value = "/subtopics/{subTopicId}/unfreeze", method = RequestMethod.GET)
	public String unFreezeSubTopic(
			@PathVariable("subTopicId") BigInteger subTopicId, Model uiModel,
			HttpServletRequest httpServletRequest) {
		SubTopic subTopic = subTopicService.findSubTopic(subTopicId);
		if(subTopic == null){
			return "redirect:/forum";
		}
		subTopic = subTopicService.unFreezeSubTopic(subTopic);
		return "redirect:/forum/subtopics/"+subTopic.getId()+"?size=" + PAGE_SIZE;
	}

	/* Discussions */
	@RequestMapping(value = "/discussions", method = RequestMethod.GET)
	public String showDiscussions(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			Model uiModel) {
		return "redirect:" + FORUM_SUBTOPICS;
	}

	/**
	 * show discussion, with pagination on discussions comments.
	 * 
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
	public String showDiscussionView(
			@PathVariable("id") BigInteger discussionId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "imageId", required  = false) String imageId,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model uiModel) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		if (discussion == null) {
			return "redirect:/forum/discussions";
		}
		List<Discussion> discussions = new ArrayList<Discussion>();
		if(securityUtil.isUserLogged()){
			discussions = discussionService.findBySubTopic(discussion.getSubTopic());
		}else {
			discussions = discussionService.findBySubTopicAndConfidentiality(discussion.getSubTopic(), Confidentiality.Publique);
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
		Comment comment = new Comment();
		comment.setPhotoId(imageId);
		uiModel.addAttribute("commentModel", comment);
		uiModel.addAttribute("entities", discussions);
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
		if(discussion.getConfidentiality() == null ){//mean it was an unauthenticated user, that don't have access to the confidentiality combobox
			discussion.setConfidentiality(Confidentiality.Publique);
		}
		HashSet<Comment> comments = new HashSet<Comment>();
		Comment comment = new Comment();
		comment.setBusinessId(RandomStringUtils.randomAlphanumeric(7));
		comment.setCommentaire(discussion.getContent());
		comment.setCreated(new Date());
		comment.setFrozen(discussion.getFrozen());
		comment.setNomade(discussion.getNomade());
		comments.add(comment);
		discussion.setComments(comments);
		// actually, the #updateDiscussion method, save the object and return
		// the saved object.
		discussion = discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussion.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/discussions/{discussionId}/form", method = RequestMethod.GET)
	public String showUpdateDiscussionForm(
			@PathVariable("discussionId") BigInteger discussionId,
			@RequestParam(value = "imageId", required  = false) String imageId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		discussion.setPhotoId(imageId);;
		uiModel.addAttribute("discussion", discussion);
		List<SubTopic> subTopics = subTopicService.findByParentTopic(discussion.getSubTopic().getParentTopic());
		uiModel.addAttribute("subTopics", subTopics);
		populateModel(uiModel);
		return "public/discussions/update";
	}
	@RequestMapping(value = "/discussions/{discussionId}/freeze", method = RequestMethod.GET)
	public String freezeDiscussion(
			@PathVariable("discussionId") BigInteger discussionId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		discussion = discussionService.freezeDiscussion(discussion);
		return "redirect:/forum/discussions/"+discussion.getId()+"?size=" + PAGE_SIZE;
	}

	@RequestMapping(value = "/discussions/{discussionId}/unfreeze", method = RequestMethod.GET)
	public String unFreezeDiscussion(
			@PathVariable("discussionId") BigInteger discussionId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		discussion = discussionService.freezeDiscussion(discussion);
		return "redirect:/forum/discussions/"+discussion.getId()+"?size=" + PAGE_SIZE;
	}

	@RequestMapping(value = "/discussions/update", method = RequestMethod.POST)
	public String updateDiscussion(@Valid Discussion discussion, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Discussion discussion2 = discussionService.findDiscussion(discussion
				.getId());
		if (discussion2 == null) {
			throw new RuntimeException(
					"You are trying to update an invalid/unexisting discussion");
		}
		// actually, the #updateDiscussion method, save the object and return
		// the saved object.
		discussion2.setTitle(discussion.getTitle());
		discussion2.setContent(discussion.getContent());
		discussion2.setSubTopic(discussion.getSubTopic());
		discussion2.setFrozen(discussion.getFrozen());
		discussionService.updateDiscussion(discussion2);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussion2.getId().toString(),
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
		comment.setCreated(new Date());
		comment.setNomade(securityUtil.getUserNomade());
		Discussion discussion = discussionService.findDiscussion(discussionId);
		discussion.getComments().add(comment);
		discussionService.updateDiscussion(discussion);
		return "redirect:/forum/discussions/"
				+ encodeUrlPathSegment(discussionId.toString(),
						httpServletRequest);
	}


	@RequestMapping(value = "/discussions/{discussionId}/comments/{businessId}/form", method = RequestMethod.GET)
	public String showUpdateCommentForm(
			@PathVariable("discussionId") BigInteger discussionId,
			@PathVariable("businessId") String commentBusinessId,
			@RequestParam(value = "imageId", required  = false) String imageId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		Comment comment = null;
		for (Comment c : discussion.getComments()) {
			if (c.getBusinessId().equals(commentBusinessId)) {
				comment = c;
			}
		}
		comment.setPhotoId(imageId);
		uiModel.addAttribute("discussion", discussion);
		uiModel.addAttribute("comment", comment);
		populateModel(uiModel);
		return "public/comments/update";
	}
	@RequestMapping(value = "/discussions/{discussionId}/comments/{businessId}/freeze", method = RequestMethod.GET)
	public String freezeComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@PathVariable("businessId") String commentBusinessId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		Set<Comment> comments = discussion.getComments();
		for (Comment c : comments) {
			if (c.getBusinessId().equals(commentBusinessId)) {
				c.setFrozen(Boolean.TRUE);
				discussionService.updateDiscussion(discussion);
			}
		}
		return "redirect:/forum/discussions/"+discussion.getId()+"?size=" + PAGE_SIZE;
	}

	@RequestMapping(value = "/discussions/{discussionId}/comments/{businessId}/unfreeze", method = RequestMethod.GET)
	public String unFreezeComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@PathVariable("businessId") String commentBusinessId,
			Model uiModel, HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		Set<Comment> comments = discussion.getComments();
		for (Comment c : comments) {
			if (c.getBusinessId().equals(commentBusinessId)) {
				c.setFrozen(Boolean.FALSE);
				discussionService.updateDiscussion(discussion);
			}
		}
		return "redirect:/forum/discussions/"+discussion.getId()+"?size=" + PAGE_SIZE;
	}
	
	@RequestMapping(value = "/discussions/{discussionId}/comments/update", method = RequestMethod.POST)
	public String updateComment(
			@PathVariable("discussionId") BigInteger discussionId,
			@Valid Comment comment, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Discussion discussion = discussionService.findDiscussion(discussionId);
		Set<Comment> comments = discussion.getComments();
		for (Comment c : comments) {
			if (c.getBusinessId().equals(comment.getBusinessId())) {
				c.setCommentaire(comment.getCommentaire());
				c.setFrozen(comment.getFrozen());
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

	/**
	 * This method take a {@link Model uiModel} and populate it with <br />
	 * <ul>
	 * 	<li>{@link Confidentiality confidentiality}</li>
	 * </ul>
	 * @param uiModel
	 */
	public void populateModel(Model uiModel) {
		addDateTimeFormatPatterns(uiModel,"");
		uiModel.addAttribute("confidentialities", Arrays.asList(
				Confidentiality.Publique, Confidentiality.NomadesOnly));
	}
	void addDateTimeFormatPatterns(Model uiModel,String dateTimePattern) {
		if(StringUtils.isBlank((dateTimePattern))){
			dateTimePattern = "dd-MM-yyyy HH:mm";
		}
        uiModel.addAttribute("dateTimePattern", dateTimePattern);
    }
}
