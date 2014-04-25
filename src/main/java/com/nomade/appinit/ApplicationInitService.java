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
import org.springframework.util.Assert;

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

	public void initData() throws IOException {
		System.out.print("init");
		mongoTemplate.getDb().dropDatabase();
		nomadeDod.init();
		initForum(userService.findAllUserNomades().iterator().next());
		/*
		 * initTopics();
		 * initSubTopics(topicService.findAllTopics().iterator().next
		 * (),userService
		 * .findAllUserNomades().iterator().next());parcoursDataOnDemand.init();
		 * mongoTemplate.getDb().dropDatabase(); nomadeDod.init();
		 * voyageDataOnDemand.init(); vehiculeDataOnDemand.init();
		 * pratiqueDataOnDemand.init(); infoPratiqueDataOnDemand.init();
		 */

	}

	protected void initForum(UserNomade nomade) {
		Assert.notNull(nomade, "The nomad user should not be null here !");
		// init topic preparatifs
		Topic topicPreparatif = TopicBuilder
				.get()
				.addTopic("Les préparatifs", "Les préparatifs",
						Confidentiality.Publique, nomade, "lang_preparations")
				.getFirstTopic();
		topicService.saveTopic(topicPreparatif);
		List<SubTopic> subTopics = SubTopicBuilder
				.get()
				.addPublicNonFrozenSubTopic("Formalités administratives",
						nomade, topicPreparatif,
						"lang_administratives_formalities")
				.addPublicNonFrozenSubTopic("La santé en voyage", nomade,
						topicPreparatif, "lang_healthcare_in_travel")
				.addPublicNonFrozenSubTopic("Financer son voyage", nomade,
						topicPreparatif, "lang_finance_his_travel")
				.getSubTopics();
		subTopics = saveAndClearSubTopicsList(subTopics);
		SubTopic subTopic = SubTopicBuilder
				.get()
				.addPublicNonFrozenSubTopic("Assurances", nomade,
						topicPreparatif, "lang_insurances").getFirstSubTopic();
		subTopicService.saveSubTopic(subTopic);
		subTopics = SubTopicBuilder
				.get()
				.addSubPublicNonFrozenSubTopic("Assurance personnelle", nomade,
						topicPreparatif, subTopic, "lang_personal_insurance")
				.addSubPublicNonFrozenSubTopic("Assurance du véhicule", nomade,
						topicPreparatif, subTopic, "lang_vehicle_insurance")
				.addSubPublicNonFrozenSubTopic("Assurance pour le matériel",
						nomade, topicPreparatif, subTopic,
						"lang_equipment_insurance").getSubTopics();
		subTopics = saveAndClearSubTopicsList(subTopics);

		List<SubTopic> subTopics2 = SubTopicBuilder
				.get()
				.addPublicNonFrozenSubTopic("Ecrire son carnet de voyage",
						nomade, topicPreparatif,
						"lang_write_your_travel_notebook")
				.addPublicNonFrozenSubTopic("Comment stocker ses photos",
						nomade, topicPreparatif,
						"lang_how_to_store_your_photos").getSubTopics();
		subTopics2 = saveAndClearSubTopicsList(subTopics2);

		SubTopic subTopicLogement = SubTopicBuilder
				.get()
				.addPublicNonFrozenSubTopic("Logement", nomade,
						topicPreparatif, "lang_housing").getFirstSubTopic();
		subTopicService.saveSubTopic(subTopicLogement);
		List<SubTopic> subSubTopicLogement = SubTopicBuilder
				.get()
				.addSubPublicNonFrozenSubTopic("Que faire de votre logement",
						nomade, topicPreparatif, subTopicLogement,
						"lang_what_to_do_with_your_housing")
				.addSubPublicNonFrozenSubTopic(
						"Offres de location de logement", nomade,
						topicPreparatif, subTopicLogement,
						"lang_housing_rental_offers")
				.addSubPublicNonFrozenSubTopic(
						"Demandes de location de logement", nomade,
						topicPreparatif, subTopicLogement,
						"lang_housing_rental_demands")
				.addSubPublicNonFrozenSubTopic("Echange de logement", nomade,
						topicPreparatif, subTopicLogement, "lang_housing_swap")
				.getSubTopics();
		subSubTopicLogement = saveAndClearSubTopicsList(subSubTopicLogement);

		SubTopic subTopicCommentVoyager = SubTopicBuilder
				.get()
				.addPublicNonFrozenSubTopic("Comment voyager", nomade,
						topicPreparatif, "lang_how_to_travel")
				.getFirstSubTopic();
		subTopicService.saveSubTopic(subTopicCommentVoyager);
		List<SubTopic> subSubTopicsCommentVoyager = SubTopicBuilder
				.get()
				.addSubPublicNonFrozenSubTopic(
						"Choisir son  véhicule pour voyager", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_choose_your_vehicle_for_travel")
				.addSubPublicNonFrozenSubTopic("Véhicules à vendre", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_vehicle_for_sale_vehicle")
				.addSubPublicNonFrozenSubTopic("Recherche d'achat de véhicule",
						nomade, topicPreparatif, subTopicCommentVoyager,
						"lang_looking_for_buy_vehicle")
				.addSubPublicNonFrozenSubTopic("Voyager en voiture", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_car")
				.addSubPublicNonFrozenSubTopic("Voyager en 4x4", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_4x4")
				.addSubPublicNonFrozenSubTopic("Voyager en camion", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_truck")
				.addSubPublicNonFrozenSubTopic("Voyager en camping car",
						nomade, topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_camping_car")
				.addSubPublicNonFrozenSubTopic("Voyager en moto", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_motorcycle")
				.addSubPublicNonFrozenSubTopic("Voyager en vélo", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_with_a_bike")
				.addSubPublicNonFrozenSubTopic("Voyager différemment", nomade,
						topicPreparatif, subTopicCommentVoyager,
						"lang_travel_differently").getSubTopics();
		subSubTopicsCommentVoyager = saveAndClearSubTopicsList(subSubTopicsCommentVoyager);
	}

	protected List<SubTopic> saveAndClearSubTopicsList(List<SubTopic> subTopics) {
		saveSubTopics(subTopics);
		subTopics.clear();
		return subTopics;
	}

	protected void saveSubTopics(List<SubTopic> subTopics) {
		Assert.notNull(subTopics, "Subtopics should not be null here!");
		for (SubTopic subTopic : subTopics) {
			subTopicService.saveSubTopic(subTopic);
		}
	}

	protected void initTopics() {
		UserNomade nomade = userService.findAllUserNomades().iterator().next();
		TopicBuilder topicBuilder = TopicBuilder
				.get()
				.addTopic("Africa Travels", "travales to africa",
						Confidentiality.Publique, nomade)
				.addTopic("Europa", "Travels accross europe",
						Confidentiality.Publique, nomade)
				.addTopic("Asia", "Travels Across Asia",
						Confidentiality.FriendsOnly, nomade)
				.addTopic("Antartica", "Visit agross antartica",
						Confidentiality.NomadesOnly, nomade);
		List<Topic> topics = topicBuilder.getTopics();
		for (Topic topic : topics) {
			topicService.saveTopic(topic);
		}
	}

	protected void initSubTopics(Topic topic, UserNomade nomade) {
		SubTopicBuilder subTopicBuilder = SubTopicBuilder
				.get()
				.addSubTopic("Afrique du Nord",
						"Forum relatif a l'Afrique du nord",
						Confidentiality.Publique, nomade, topic, false)
				.addSubTopic(
						"Afrique du " + RandomStringUtils.randomAlphanumeric(4),
						"Forum relatif a l'Afrique du "
								+ RandomStringUtils.randomAlphanumeric(4),
						Confidentiality.Publique, nomade, topic, false)
				.addSubTopic(
						"Afrique du " + RandomStringUtils.randomAlphanumeric(4),
						"Forum relatif a l'Afrique du "
								+ RandomStringUtils.randomAlphanumeric(4),
						Confidentiality.Publique, nomade, topic, false)
				.addSubTopic(
						"Afrique du " + RandomStringUtils.randomAlphanumeric(4),
						"Forum relatif a l'Afrique du "
								+ RandomStringUtils.randomAlphanumeric(4),
						Confidentiality.Publique, nomade, topic, false)
				.addSubTopic("Afrique de L'Ouest",
						"Forum relatif a l'Afrique l'ouest",
						Confidentiality.Publique, nomade, topic, true);
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

	protected HashSet<Discussion> initDiscussion(SubTopic subTopic,
			UserNomade userNomade) {
		List<Discussion> discussions = DiscussionBuilder
				.get()
				.addDiscussion(subTopic, Confidentiality.Publique,
						RandomStringUtils.randomAlphanumeric(120), userNomade,
						RandomStringUtils.randomAlphabetic(7), false)
				.addDiscussion(subTopic, Confidentiality.Publique,
						RandomStringUtils.randomAlphanumeric(120), userNomade,
						RandomStringUtils.randomAlphabetic(7), false)
				.addDiscussion(subTopic, Confidentiality.Publique,
						RandomStringUtils.randomAlphanumeric(120), userNomade,
						RandomStringUtils.randomAlphabetic(7), false)
				.addDiscussion(subTopic, Confidentiality.Publique,
						RandomStringUtils.randomAlphanumeric(120), userNomade,
						RandomStringUtils.randomAlphabetic(7), true)
				.addDiscussion(subTopic, Confidentiality.NomadesOnly,
						RandomStringUtils.randomAlphanumeric(120), userNomade,
						RandomStringUtils.randomAlphabetic(7), false)
				.getDiscussions();
		return new HashSet<Discussion>(discussions);
		/*parcoursDataOnDemand.init();
		voyageDataOnDemand.init();
		vehiculeDataOnDemand.init();
		pratiqueDataOnDemand.init();
		infoPratiqueDataOnDemand.init();*/
	}
	/*
	 * public void initApplication() {
	 * 
	 * Set<RoleName> roleNames = new HashSet<RoleName>();
	 * roleNames.add(RoleName.ROLE_SIMPLE_USER); UserNomade nomade = new
	 * UserNomade("toto", "titi", false, roleNames);
	 * userService.saveUserNomade(nomade);
	 * 
	 * UserNomade nomade1 = new UserNomade("mama", "123", true, roleNames);
	 * userService.saveUserNomade(nomade1);
	 * 
	 * UserNomade nomade2 = new UserNomade("hermine", "yougo", false,
	 * roleNames); nomade2.setDisableLogin(true);
	 * userService.saveUserNomade(nomade2); }
	 */

}
