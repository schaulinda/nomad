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

import com.nomade.domain.DangerPratique;
import com.nomade.domain.UserNomade;
import com.nomade.service.DangerPratiqueService;

@Component
@Configurable
public class DangerPratiqueDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<DangerPratique> data;

	@Autowired
    DangerPratiqueService dangerPratiqueService;
	@Autowired
	UserNomadeDataOnDemand userNomadeDataOnDemand;

	public DangerPratique getNewTransientDangerPratique(int index) {
        DangerPratique obj = new DangerPratique();
        setComment(obj, index);
        setCreated(obj, index);
        setDure(obj, index);
        setEstimationValidite(obj, index);
        setGeoLocation(obj, index);
        setInfoVerif(obj, index);
        setLocation(obj, index);
        setLocationLat(obj, index);
        setLocationLng(obj, index);
        setNomade(obj, index);
        setPhoto(obj, index);
        setSelecteur(obj, index);
        setSelecteur1(obj, index);
        setTitle(obj, index);
        setVoteNegatif(obj, index);
        setVotePositif(obj, index);
        return obj;
    }

	public void setComment(DangerPratique obj, int index) {
        String comment = "comment_" + index;
        obj.setComment(comment);
    }

	public void setCreated(DangerPratique obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	public void setDure(DangerPratique obj, int index) {
        int dure = index;
        obj.setDure(dure);
    }

	public void setEstimationValidite(DangerPratique obj, int index) {
        int estimationValidite = index;
        obj.setEstimationValidite(estimationValidite);
    }

	public void setGeoLocation(DangerPratique obj, int index) {
		double[] geoLocation = { new Integer(index+6).doubleValue()+28.890681, new Integer(index+1).doubleValue()+8.335911 };
        obj.setGeoLocation(geoLocation);
    }

	public void setInfoVerif(DangerPratique obj, int index) {
        Boolean infoVerif = true;
        obj.setInfoVerif(infoVerif);
    }

	public void setLocation(DangerPratique obj, int index) {
        String location = "location_" + index;
        obj.setLocation(location);
    }

	public void setLocationLat(DangerPratique obj, int index) {
        double locationLat = new Integer(index).doubleValue();
        obj.setLocationLat(locationLat);
    }

	public void setLocationLng(DangerPratique obj, int index) {
        double locationLng = new Integer(index).doubleValue();
        obj.setLocationLng(locationLng);
    }

	public void setNomade(DangerPratique obj, int index) {
		 UserNomade nomade = userNomadeDataOnDemand.getSpecificUserNomade(index);
        obj.setNomade(nomade);
    }

	public void setPhoto(DangerPratique obj, int index) {
        String photo = "";
        obj.setPhoto(photo);
    }

	public void setSelecteur(DangerPratique obj, int index) {
        String selecteur = "selecteur_" + index;
        obj.setSelecteur(selecteur);
    }

	public void setSelecteur1(DangerPratique obj, int index) {
        String selecteur1 = "selecteur1_" + index;
        obj.setSelecteur1(selecteur1);
    }

	public void setTitle(DangerPratique obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }

	public void setVoteNegatif(DangerPratique obj, int index) {
        int voteNegatif = index;
        obj.setVoteNegatif(voteNegatif);
    }

	public void setVotePositif(DangerPratique obj, int index) {
        int votePositif = index;
        obj.setVotePositif(votePositif);
    }

	public DangerPratique getSpecificDangerPratique(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        DangerPratique obj = data.get(index);
        BigInteger id = obj.getId();
        return dangerPratiqueService.findDangerPratique(id);
    }

	public DangerPratique getRandomDangerPratique() {
        init();
        DangerPratique obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return dangerPratiqueService.findDangerPratique(id);
    }

	public boolean modifyDangerPratique(DangerPratique obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = dangerPratiqueService.findDangerPratiqueEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'DangerPratique' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<DangerPratique>();
        for (int i = 0; i < 10; i++) {
            DangerPratique obj = getNewTransientDangerPratique(i);
            try {
                dangerPratiqueService.saveDangerPratique(obj);
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
