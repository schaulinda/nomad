package com.nomade.dataTest;

import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.service.EtapeVoyageService;
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
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class EtapeVoyageDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<EtapeVoyage> data;

	@Autowired
    EtapeVoyageService etapeVoyageService;
	@Autowired
	UserNomadeDataOnDemand userNomadeDataOnDemand;
	@Autowired
	ParcoursDataOnDemand dataOnDemand;

	public EtapeVoyage getNewTransientEtapeVoyage(int index) {
        EtapeVoyage obj = new EtapeVoyage();
        setCreated(obj, index);
        setDateEtape(obj, index);
        setDescription(obj, index);
        setGeolocation(obj, index);
        setLocation(obj, index);
        setNomade(obj, index);
        setParcours(obj, index);
        setUserPhoto(obj, index);
        setUserlat(obj, index);
        setUserlng(obj, index);
        return obj;
    }

	public void setCreated(EtapeVoyage obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	public void setDateEtape(EtapeVoyage obj, int index) {
        Date dateEtape = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateEtape(dateEtape);
    }

	public void setDescription(EtapeVoyage obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setGeolocation(EtapeVoyage obj, int index) {
        double[] geolocation = { new Integer(index).doubleValue(), new Integer(index).doubleValue() };
        obj.setGeolocation(geolocation);
    }

	public void setLocation(EtapeVoyage obj, int index) {
        String location = "location_" + index;
        obj.setLocation(location);
    }

	public void setNomade(EtapeVoyage obj, int index) {
        UserNomade nomade = userNomadeDataOnDemand.getSpecificUserNomade(index);
        obj.setNomade(nomade);
    }

	public void setParcours(EtapeVoyage obj, int index) {
		Parcours parcours = dataOnDemand.getSpecificParcours(index);
    }

	public void setUserPhoto(EtapeVoyage obj, int index) {
        String userPhoto = "";
        obj.setUserPhoto(userPhoto);
    }

	public void setUserlat(EtapeVoyage obj, int index) {
        double userlat = new Integer(index).doubleValue();
        obj.setUserlat(userlat);
    }

	public void setUserlng(EtapeVoyage obj, int index) {
        double userlng = new Integer(index).doubleValue();
        obj.setUserlng(userlng);
    }

	public EtapeVoyage getSpecificEtapeVoyage(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        EtapeVoyage obj = data.get(index);
        BigInteger id = obj.getId();
        return etapeVoyageService.findEtapeVoyage(id);
    }

	public EtapeVoyage getRandomEtapeVoyage() {
        init();
        EtapeVoyage obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return etapeVoyageService.findEtapeVoyage(id);
    }

	public boolean modifyEtapeVoyage(EtapeVoyage obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = etapeVoyageService.findEtapeVoyageEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'EtapeVoyage' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<EtapeVoyage>();
        for (int i = 0; i < 10; i++) {
            EtapeVoyage obj = getNewTransientEtapeVoyage(i);
            try {
                etapeVoyageService.saveEtapeVoyage(obj);
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
