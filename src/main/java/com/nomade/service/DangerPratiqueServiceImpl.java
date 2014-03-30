package com.nomade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nomade.domain.DangerPratique;
import com.nomade.domain.UserNomade;
import com.nomade.repository.DangerPratiqueRepository;

public class DangerPratiqueServiceImpl implements DangerPratiqueService {

	@Autowired
	MongoTemplate mongoTemplate;
	protected static final int NUMBER_PER_PAGE = 1;

	public List<DangerPratique> findByLocation(double[] loc1, double[] loc2) {

		/*
		 * Box box = new Box(new Point(loc1[0], loc1[1]), new Point(loc2[0],
		 * loc2[1]));
		 * 
		 * List<DangerPratique> find = mongoTemplate.find(new
		 * Query(Criteria.where("geoLocation").within(box)),
		 * DangerPratique.class); System.out.print(find); return find;
		 */
		return dangerPratiqueRepository.findAll();

	}

	public Page<DangerPratique> findByNomade(UserNomade nomade, int page) {

		PageRequest pageRequest = new PageRequest(page, NUMBER_PER_PAGE,
				new Sort(new Order(Direction.DESC, "comments.created"),
						new Order(Direction.DESC, "created")));

		return dangerPratiqueRepository.findByNomade(nomade, pageRequest);
	}
	
	public List<DangerPratique> findByNomadeOrderByCreated(UserNomade nomade){
		
		return dangerPratiqueRepository.findByNomadeOrderByCreatedDesc(nomade);
	}

	public boolean hasVoted(UserNomade nomade, DangerPratique dangerPratique) {

		List<UserNomade> listVotants = dangerPratique.getListVotants();
		for (UserNomade v : listVotants) {

			if (nomade.getId().equals(v.getId())) {
				return true;
			}
		}

		return false;

	}

}
