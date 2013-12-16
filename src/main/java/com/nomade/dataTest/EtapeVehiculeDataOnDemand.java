package com.nomade.dataTest;

import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.Parcours;
import com.nomade.domain.UserNomade;
import com.nomade.service.EtapeVehiculeService;
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
public class EtapeVehiculeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<EtapeVehicule> data;

	@Autowired
    EtapeVehiculeService etapeVehiculeService;
	@Autowired
	UserNomadeDataOnDemand userNomadeDataOnDemand;
	@Autowired
	ParcoursDataOnDemand dataOnDemand;

	public EtapeVehicule getNewTransientEtapeVehicule(int index) {
        EtapeVehicule obj = new EtapeVehicule();
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

	public void setCreated(EtapeVehicule obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	public void setDateEtape(EtapeVehicule obj, int index) {
        Date dateEtape = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateEtape(dateEtape);
    }

	public void setDescription(EtapeVehicule obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setGeolocation(EtapeVehicule obj, int index) {
        double[] geolocation = { new Integer(index).doubleValue(), new Integer(index).doubleValue() };
        obj.setGeolocation(geolocation);
    }

	public void setLocation(EtapeVehicule obj, int index) {
        String location = "location_" + index;
        obj.setLocation(location);
    }

	public void setNomade(EtapeVehicule obj, int index) {
		 UserNomade nomade = userNomadeDataOnDemand.getSpecificUserNomade(index);
        obj.setNomade(nomade);
    }

	public void setParcours(EtapeVehicule obj, int index) {
        Parcours parcours = dataOnDemand.getSpecificParcours(index);
        obj.setParcours(parcours);
    }

	public void setUserPhoto(EtapeVehicule obj, int index) {
        String userPhoto = "";
        obj.setUserPhoto(userPhoto);
    }

	public void setUserlat(EtapeVehicule obj, int index) {
        double userlat = new Integer(index).doubleValue();
        obj.setUserlat(userlat);
    }

	public void setUserlng(EtapeVehicule obj, int index) {
        double userlng = new Integer(index).doubleValue();
        obj.setUserlng(userlng);
    }

	public EtapeVehicule getSpecificEtapeVehicule(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        EtapeVehicule obj = data.get(index);
        BigInteger id = obj.getId();
        return etapeVehiculeService.findEtapeVehicule(id);
    }

	public EtapeVehicule getRandomEtapeVehicule() {
        init();
        EtapeVehicule obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return etapeVehiculeService.findEtapeVehicule(id);
    }

	public boolean modifyEtapeVehicule(EtapeVehicule obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = etapeVehiculeService.findEtapeVehiculeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'EtapeVehicule' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<EtapeVehicule>();
        for (int i = 0; i < 10; i++) {
            EtapeVehicule obj = getNewTransientEtapeVehicule(i);
            try {
                etapeVehiculeService.saveEtapeVehicule(obj);
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
