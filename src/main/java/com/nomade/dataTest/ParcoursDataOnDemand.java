package com.nomade.dataTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.service.ParcoursService;

@Configurable
@Component
public class ParcoursDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Parcours> data;

	@Autowired
    ParcoursService parcoursService;
	@Autowired
	UserNomadeDataOnDemand userNomadeDataOnDemand;

	public Parcours getNewTransientParcours(int index) {
        Parcours obj = new Parcours();
        setCreated(obj, index);
        
        setNomad(obj, index);
       
        return obj;
    }

	public void setCreated(Parcours obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	


	public void setNomad(Parcours obj, int index) {
		 UserNomade nomad = userNomadeDataOnDemand.getSpecificUserNomade(index);
        obj.setNomad(nomad);
    }

	

	public Parcours getSpecificParcours(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Parcours obj = data.get(index);
        BigInteger id = obj.getId();
        return parcoursService.findParcours(id);
    }

	public Parcours getRandomParcours() {
        init();
        Parcours obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return parcoursService.findParcours(id);
    }

	public boolean modifyParcours(Parcours obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = parcoursService.findParcoursEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Parcours' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Parcours>();
        for (int i = 0; i < 10; i++) {
            Parcours obj = getNewTransientParcours(i);
            try {
                parcoursService.saveParcours(obj);
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
