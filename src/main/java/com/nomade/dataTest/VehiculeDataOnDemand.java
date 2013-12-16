package com.nomade.dataTest;

import com.nomade.domain.Country;
import com.nomade.domain.Vehicule;
import com.nomade.domain.VehiculeState;
import com.nomade.domain.VehiculeType;
import com.nomade.service.VehiculeService;
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
public class VehiculeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Vehicule> data;

	@Autowired
    VehiculeService vehiculeService;

	public Vehicule getNewTransientVehicule(int index) {
        Vehicule obj = new Vehicule();
        setAnneMiseEnService(obj, index);
        setCouleur(obj, index);
        setDescription(obj, index);
        setForSellCountry(obj, index);
        setModel(obj, index);
        setPhoto(obj, index);
        setVehiculeName(obj, index);
        setVehiculeState(obj, index);
        setVehiculeType(obj, index);
        return obj;
    }

	public void setAnneMiseEnService(Vehicule obj, int index) {
        Date anneMiseEnService = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setAnneMiseEnService(anneMiseEnService);
    }

	public void setCouleur(Vehicule obj, int index) {
        String couleur = "couleur_" + index;
        obj.setCouleur(couleur);
    }

	public void setDescription(Vehicule obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setForSellCountry(Vehicule obj, int index) {
        Country forSellCountry = Country.class.getEnumConstants()[0];
        obj.setForSellCountry(forSellCountry);
    }

	public void setModel(Vehicule obj, int index) {
        String model = "model_" + index;
        obj.setModel(model);
    }

	public void setPhoto(Vehicule obj, int index) {
        String photo = "";
        obj.setPhoto(photo);
    }

	public void setVehiculeName(Vehicule obj, int index) {
        String vehiculeName = "vehiculeName_" + index;
        obj.setVehiculeName(vehiculeName);
    }

	public void setVehiculeState(Vehicule obj, int index) {
        VehiculeState vehiculeState = VehiculeState.class.getEnumConstants()[0];
        obj.setVehiculeState(vehiculeState);
    }

	public void setVehiculeType(Vehicule obj, int index) {
        VehiculeType vehiculeType = VehiculeType.class.getEnumConstants()[0];
        obj.setVehiculeType(vehiculeType);
    }

	public Vehicule getSpecificVehicule(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Vehicule obj = data.get(index);
        BigInteger id = obj.getId();
        return vehiculeService.findVehicule(id);
    }

	public Vehicule getRandomVehicule() {
        init();
        Vehicule obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return vehiculeService.findVehicule(id);
    }

	public boolean modifyVehicule(Vehicule obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = vehiculeService.findVehiculeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Vehicule' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Vehicule>();
        for (int i = 0; i < 10; i++) {
            Vehicule obj = getNewTransientVehicule(i);
            try {
                vehiculeService.saveVehicule(obj);
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
