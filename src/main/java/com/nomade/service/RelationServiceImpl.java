package com.nomade.service;

import java.util.List;

import com.nomade.domain.FriendState;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;

public class RelationServiceImpl implements RelationService {

	public boolean friendschip(UserNomade nomade, UserNomade nomade1) {

		List<Relation> findByNomade2AndNomade = relationRepository
				.findByNomade2AndNomade(nomade, nomade1);

		if (findByNomade2AndNomade != null && findByNomade2AndNomade.size() > 0) {

			return true;

		}

		List<Relation> findByNomadeAndNomade2 = relationRepository
				.findByNomadeAndNomade2(nomade, nomade1);

		if (findByNomadeAndNomade2 != null && findByNomadeAndNomade2.size() > 0) {

			return true;

		}

		return false;

	}

	public List<Relation> findByNomadeOrNomade2(UserNomade nomade,
			UserNomade nomade1) {

		return relationRepository.findByNomadeOrNomade2(nomade, nomade1);
	}

	public List<Relation> findMyFriends(UserNomade nomade) {

		return relationRepository
				.findByFriendStateAndNomadeOrFriendStateAndNomade2(
						FriendState.AMI, nomade, FriendState.AMI, nomade);
	}

	public List<Relation> findMyFriendUnique(UserNomade nomade,
			UserNomade nomade1) {

		List<Relation> list = relationRepository.findByNomadeAndNomade2(nomade,
				nomade1);
		if (list != null && list.size() > 0) {
			System.out.print("list: " + list);
			return list;
		}
		List<Relation> list1 = relationRepository.findByNomadeAndNomade2(
				nomade1, nomade);
		if (list1 != null && list1.size() > 0) {
			System.out.print("list1: " + list1);
			return list1;
		}
		return null;
	}

	public List<Relation> findReceivedDemand(UserNomade nomade) {

		return relationRepository.findByNomade2AndFriendState(nomade,
				FriendState.EN_ATTENTE);
	}

	public List<Relation> findByNomadeAndNomade2(UserNomade nomade,
			UserNomade nomade2) {

		return relationRepository.findByNomadeAndNomade2(nomade, nomade2);
	}

}
