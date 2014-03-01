package com.nomade.web;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.service.DiscussionService;
import com.nomade.service.SubTopicService;
import com.nomade.service.TopicService;
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Autowired
	private TopicService topicService;
	@Autowired
	private SubTopicService subTopicService;
	@Autowired
	private DiscussionService discussionService;
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		registry.addConverter(new StringToTopicConverter());
		registry.addConverter(new StringToSubTopicConverter());
		registry.addConverter(new StringToDiscussionConverter());
	}

	class StringToTopicConverter implements Converter<String, Topic>{

		@Override
		public Topic convert(String topicId) {
			if(StringUtils.isBlank(topicId)){
				return null;
			}
			return topicService.findTopic(new BigInteger(topicId));
		}
		
	}
	
	class StringToSubTopicConverter implements Converter<String, SubTopic>{

		@Override
		public SubTopic convert(String subTopicId) {
			if(StringUtils.isBlank(subTopicId)){
				return null;
			}
			return subTopicService.findSubTopic(new BigInteger(subTopicId));
		}
		
	}

	class StringToDiscussionConverter implements Converter<String, Discussion>{

		@Override
		public Discussion convert(String discussionId) {
			if(StringUtils.isBlank(discussionId)){
				return null;
			}
			return discussionService.findDiscussion(new BigInteger(discussionId));
		}
		
	}
}
