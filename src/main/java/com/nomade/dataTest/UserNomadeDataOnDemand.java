package com.nomade.dataTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.nomade.domain.Account;
import com.nomade.domain.Profil;
import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.domain.Vehicule;
import com.nomade.service.UserService;

@Component
@Configurable
public class UserNomadeDataOnDemand {
	
	@Autowired
	AccountDataOnDemand accountDataOnDemand;
	@Autowired
	ProfilDataOnDemand dataOnDemand;
	@Autowired
	VehiculeDataOnDemand vehiculeDataOnDemand;
	
	private static Date accExp = DateUtils.addYears(new Date(), 50);
	
	 public void setDisableLogin(UserNomade obj, int index) {
	        Boolean disableLogin = false;
	        obj.setDisableLogin(disableLogin);
	    }
	    
	    public void setPassword(UserNomade obj, int index) {
	        String password = "nomad" + index;
	        obj.setPassword(obj.encodePassword(password));
	    }
	     
	    public void setAccountLocked(UserNomade obj, int index) {
	        Boolean accountLocked = false;
	        obj.setAccountLocked(accountLocked);
	    }
	    
	    public void setCompte(UserNomade obj, int index) {
	        Account compte = accountDataOnDemand.getNewTransientAccount(index);
	        obj.setCompte(compte);
	    }
	    
	    public void setProfil(UserNomade obj, int index) {
	        Profil profil = dataOnDemand.getNewTransientProfil(index);
	        obj.setProfil(profil);
	    }
	    
	    public void setRoleNames(UserNomade obj, int index){
	    	Set<RoleName> roleNames = new HashSet<RoleName>();
			roleNames.add(RoleName.ROLE_SIMPLE_USER);
			if(index == 0){
				roleNames.add(RoleName.ROLE_ADMIN);
				roleNames.add(RoleName.ROLE_MODERATOR);
			}
			if(index == 1){
				roleNames.add(RoleName.ROLE_MODERATOR);
			}
	    	obj.setRoleNames(roleNames);
	    }
	    

	private Random rnd = new SecureRandom();

	private List<UserNomade> data;

	@Autowired
    UserService userService;

	public UserNomade getNewTransientUserNomade(int index) {
        UserNomade obj = new UserNomade();
        setAccountExpiration(obj, index);
        setAccountLocked(obj, index);
        setCompte(obj, index);
        setCredentialExpiration(obj, index);
        setDisableLogin(obj, index);
        setPassword(obj, index);
        setProfil(obj, index);
        setUserName(obj, index);
        setVehicule(obj, index);
        setRoleNames(obj, index);
        return obj;
    }

	public void setAccountExpiration(UserNomade obj, int index) {
        Date accountExpiration = accExp;
        obj.setAccountExpiration(accountExpiration);
    }

	public void setCredentialExpiration(UserNomade obj, int index) {
        Date credentialExpiration = accExp;
        obj.setCredentialExpiration(credentialExpiration);
    }

	public void setUserName(UserNomade obj, int index) {
        String userName = "username" + index;
        obj.setUserName(userName);
    }

	public void setVehicule(UserNomade obj, int index) {
        Vehicule vehicule = vehiculeDataOnDemand.getNewTransientVehicule(index);
        obj.setVehicule(vehicule);
    }

	public UserNomade getSpecificUserNomade(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserNomade obj = data.get(index);
        BigInteger id = obj.getId();
        return userService.findUserNomade(id);
    }

	public UserNomade getRandomUserNomade() {
        init();
        UserNomade obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return userService.findUserNomade(id);
    }

	public boolean modifyUserNomade(UserNomade obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = userService.findUserNomadeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserNomade' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserNomade>();
        for (int i = 0; i < 10; i++) {
            UserNomade obj = getNewTransientUserNomade(i);
            try {
                userService.saveUserNomade(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            data.add(obj);
        }
    }
}
