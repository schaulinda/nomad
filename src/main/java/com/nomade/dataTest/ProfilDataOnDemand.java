package com.nomade.dataTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.nomade.domain.Profil;
import com.nomade.service.ProfilService;

@Configurable
@Component
public class ProfilDataOnDemand {
	
	public void setFile(Profil obj, int index) {
        String file = "";
        obj.setFile(file);
    }

	private Random rnd = new SecureRandom();

	private List<Profil> data;

	@Autowired
    ProfilService profilService;

	public Profil getNewTransientProfil(int index) {
        Profil obj = new Profil();
        setButVoyage(obj, index);
        setFile(obj, index);
        setMetier(obj, index);
        setPseudo(obj, index);
        setWebsite(obj, index);
        return obj;
    }

	public void setButVoyage(Profil obj, int index) {
        String butVoyage = "butVoyage_" + index;
        obj.setButVoyage(butVoyage);
    }

	public void setMetier(Profil obj, int index) {
        String metier = "metier_" + index;
        obj.setMetier(metier);
    }

	public void setPseudo(Profil obj, int index) {
        String pseudo = "pseudo_" + index;
        obj.setPseudo(pseudo);
    }

	public void setWebsite(Profil obj, int index) {
        String website = "website_" + index;
        obj.setWebsite(website);
    }

	public Profil getSpecificProfil(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Profil obj = data.get(index);
        BigInteger id = obj.getId();
        return profilService.findProfil(id);
    }

	public Profil getRandomProfil() {
        init();
        Profil obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return profilService.findProfil(id);
    }

	public boolean modifyProfil(Profil obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = profilService.findProfilEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Profil' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Profil>();
        for (int i = 0; i < 10; i++) {
            Profil obj = getNewTransientProfil(i);
            try {
                profilService.saveProfil(obj);
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
