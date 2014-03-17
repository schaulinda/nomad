package com.nomade.appinit;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomade.dataTest.DangerPratiqueDataOnDemand;
import com.nomade.dataTest.EtapeVehiculeDataOnDemand;
import com.nomade.dataTest.EtapeVoyageDataOnDemand;
import com.nomade.dataTest.InfoPratiqueDataOnDemand;
import com.nomade.dataTest.ParcoursDataOnDemand;
import com.nomade.dataTest.UserNomadeDataOnDemand;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Discussion;
import com.nomade.domain.SubTopic;
import com.nomade.domain.Topic;
import com.nomade.domain.UserNomade;
import com.nomade.service.DiscussionService;
import com.nomade.service.SubTopicService;
import com.nomade.service.TopicService;
import com.nomade.service.UserService;
import com.nomade.tools.DiscussionBuilder;
import com.nomade.tools.SubTopicBuilder;
import com.nomade.tools.TopicBuilder;

@Service
@Transactional
public class ApplicationInitService {

	@Autowired
	UserService userService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	UserNomadeDataOnDemand nomadeDod;
	@Autowired
	ParcoursDataOnDemand parcoursDataOnDemand;
	@Autowired
	EtapeVoyageDataOnDemand voyageDataOnDemand;
	@Autowired
	EtapeVehiculeDataOnDemand vehiculeDataOnDemand;
	@Autowired
	DangerPratiqueDataOnDemand pratiqueDataOnDemand;
	@Autowired
	InfoPratiqueDataOnDemand infoPratiqueDataOnDemand;
	@Autowired
	TopicService topicService;
	@Autowired
	SubTopicService subTopicService;
	@Autowired
	DiscussionService discussionService;
	
    public  void initData() throws IOException{
		System.out.print("init");
		
		mongoTemplate.getDb().dropDatabase();		
		nomadeDod.init();
		initTopics();
		initSubTopics(topicService.findAllTopics().iterator().next(),userService.findAllUserNomades().iterator().next());
		/*parcoursDataOnDemand.init();
		voyageDataOnDemand.init();
		vehiculeDataOnDemand.init();
		pratiqueDataOnDemand.init();
		infoPratiqueDataOnDemand.init();*/
		
	}
	
	protected void initTopics() {
		UserNomade nomade = userService.findAllUserNomades().iterator().next();
		TopicBuilder topicBuilder = TopicBuilder.get().addTopic("Africa Travels", "travales to africa", Confidentiality.Publique, nomade)
			.addTopic("Europa", "Travels accross europe", Confidentiality.Publique,nomade )
			.addTopic("Asia", "Travels Across Asia", Confidentiality.FriendsOnly, nomade)
			.addTopic("Antartica", "Visit agross antartica", Confidentiality.NomadesOnly, nomade);
		List<Topic> topics = topicBuilder.getTopics();
		for (Topic topic : topics) {
			topicService.saveTopic(topic);
		}
	}
	protected void initSubTopics(Topic topic,UserNomade nomade){
		SubTopicBuilder subTopicBuilder = SubTopicBuilder.get().addSubTopic("Afrique du Nord", "Forum relatif a l'Afrique du nord", Confidentiality.Publique, nomade, topic,false)
			.addSubTopic("Afrique du "+RandomStringUtils.randomAlphanumeric(4), "Forum relatif a l'Afrique du "+RandomStringUtils.randomAlphanumeric(4) , Confidentiality.Publique, nomade, topic,false)
			.addSubTopic("Afrique du "+RandomStringUtils.randomAlphanumeric(4), "Forum relatif a l'Afrique du "+RandomStringUtils.randomAlphanumeric(4), Confidentiality.Publique, nomade, topic,false)
			.addSubTopic("Afrique du "+RandomStringUtils.randomAlphanumeric(4), "Forum relatif a l'Afrique du "+RandomStringUtils.randomAlphanumeric(4), Confidentiality.Publique, nomade, topic,false)
			.addSubTopic("Afrique de L'Ouest", "Forum relatif a l'Afrique l'ouest", Confidentiality.Publique, nomade, topic,true);
		List<SubTopic> subTopics = subTopicBuilder.getSubTopics();
		for (SubTopic subTopic : subTopics) {
			subTopicService.saveSubTopic(subTopic);
		}
		List<SubTopic> savedSubTopics = subTopicService.findAllSubTopics();
		Set<Discussion> discussions = new HashSet<Discussion>();
		for (SubTopic subTopic : savedSubTopics) {
			Set<Discussion> initDiscussion = initDiscussion(subTopic, nomade);
			discussions.addAll(initDiscussion);
		}
		for (Discussion discussion : discussions) {
			discussionService.saveDiscussion(discussion);
		}
	}
	protected HashSet<Discussion> initDiscussion(SubTopic subTopic,UserNomade userNomade) {
		List<Discussion> discussions = DiscussionBuilder.get().addDiscussion(subTopic,Confidentiality.Publique, RandomStringUtils.randomAlphanumeric(120), userNomade, RandomStringUtils.randomAlphabetic(7),false)
			.addDiscussion(subTopic,Confidentiality.Publique, RandomStringUtils.randomAlphanumeric(120), userNomade, RandomStringUtils.randomAlphabetic(7),false)
			.addDiscussion(subTopic,Confidentiality.Publique, RandomStringUtils.randomAlphanumeric(120), userNomade, RandomStringUtils.randomAlphabetic(7),false)
			.addDiscussion(subTopic,Confidentiality.Publique, RandomStringUtils.randomAlphanumeric(120), userNomade, RandomStringUtils.randomAlphabetic(7),true)
			.addDiscussion(subTopic,Confidentiality.NomadesOnly, RandomStringUtils.randomAlphanumeric(120), userNomade, RandomStringUtils.randomAlphabetic(7),false).getDiscussions();
		return new HashSet<Discussion>(discussions);
	}
	/*public void initApplication() {
		
		Set<RoleName> roleNames = new HashSet<RoleName>();
		roleNames.add(RoleName.ROLE_SIMPLE_USER);
		UserNomade nomade = new UserNomade("toto", "titi", false, roleNames);
		userService.saveUserNomade(nomade);
		
		UserNomade nomade1 = new UserNomade("mama", "123", true, roleNames);
		userService.saveUserNomade(nomade1);
		
		UserNomade nomade2 = new UserNomade("hermine", "yougo", false, roleNames);
		nomade2.setDisableLogin(true);
		userService.saveUserNomade(nomade2);
	}*/

}
