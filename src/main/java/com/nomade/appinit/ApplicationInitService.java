package com.nomade.appinit;

import java.io.IOException;

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
import com.nomade.service.UserService;

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
		
    public  void initData() throws IOException{
		System.out.print("init");
		mongoTemplate.getDb().dropDatabase();
		
		nomadeDod.init();
		/*parcoursDataOnDemand.init();
		voyageDataOnDemand.init();
		vehiculeDataOnDemand.init();
		pratiqueDataOnDemand.init();
		infoPratiqueDataOnDemand.init();*/
		
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
