package com.nomade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;

public class InfoPratiqueServiceImpl implements InfoPratiqueService {

	@Autowired
	MongoTemplate mongoTemplate;
	protected static final int NUMBER_PER_PAGE = 1;

	public List<InfoPratique> findByLocation(double[] loc1, double[] loc2) {

		/*
		 * Box box = new Box(new Point(loc1[0], loc1[1]), new Point(loc2[0],
		 * loc2[1]));
		 * 
		 * List<InfoPratique> find = mongoTemplate.find(new
		 * Query(Criteria.where("geoLocation").within(box)),
		 * InfoPratique.class); System.out.print(find); return find;
		 */
		return infoPratiqueRepository.findAll();

	}

	public Page<InfoPratique> findByNomade(UserNomade nomade, int page) {

		PageRequest pageRequest = new PageRequest(page, NUMBER_PER_PAGE,
				new Sort(new Order(Direction.DESC, "comments.created"),
						new Order(Direction.DESC, "created")));

		return infoPratiqueRepository.findByNomade(nomade, pageRequest);
	}

	public boolean hasVoted(UserNomade nomade,InfoPratique infoPratique) {
		
		boolean found = false;
		List<UserNomade> listVotants = infoPratique.getListVotants();
		for(UserNomade v:listVotants){
			
			if (nomade.getId().equals(v.getId())){
				found = true;
				break;
			}
		}
		
		return found;

	}
}
