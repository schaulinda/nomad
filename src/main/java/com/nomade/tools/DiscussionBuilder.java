package com.nomade.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import com.nomade.domain.Comment;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.UserNomade;

public class DiscussionBuilder {
		private List<Discussion> discussions = new ArrayList<Discussion>();
		
		public DiscussionBuilder() {
		}

		/**
		 * @return the discussions
		 */
		public List<Discussion> getDiscussions() {
			return discussions;
		}
		public static DiscussionBuilder get(){
			return new DiscussionBuilder();
		}
		public DiscussionBuilder addDiscussion(SubTopic subTopic, Confidentiality confidentiality, String content, UserNomade nomade, String title){
			Discussion discussion = new Discussion();
			discussion.setSubTopic(subTopic);
			discussion.setComments(generateComments(3, nomade));
			discussion.setConfidentiality(confidentiality);
			discussion.setContent(content);
			discussion.setCreated(new Date());
			discussion.setNomade(nomade);
			discussion.setTitle(title);
			discussions.add(discussion);
			return this;
		}
		Set<Comment> generateComments(int number, UserNomade nomade){
			Set<Comment> comments = new HashSet<Comment>(number);
			for (int i = 0; i < number; i++) {
				Comment comment = new Comment();
				comment.setBusinessId(RandomStringUtils.randomAlphanumeric(7));
				comment.setCommentaire(RandomStringUtils.randomAlphabetic(30));
				comment.setCreated(new Date());
				comment.setNomade(nomade);
				comments.add(comment);
			}
			return comments;
		}
	}