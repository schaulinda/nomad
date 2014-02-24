package com.nomade.dataTest;

import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;
import com.nomade.service.InfoPratiqueService;
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
public class InfoPratiqueDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<InfoPratique> data;

	@Autowired
    InfoPratiqueService infoPratiqueService;
	@Autowired
	UserNomadeDataOnDemand userNomadeDataOnDemand;

	public InfoPratique getNewTransientInfoPratique(int index) {
        InfoPratique obj = new InfoPratique();
        setComment(obj, index);
        setCreated(obj, index);
        /*setDure(obj, index);
        setEstimationValidite(obj, index);*/
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

	public void setComment(InfoPratique obj, int index) {
        String comment = "comment_" + index;
        obj.setComment(comment);
    }

	public void setCreated(InfoPratique obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	/*public void setDure(InfoPratique obj, int index) {
		TypeTime dure = index;
        obj.setDure(dure);
    }

	public void setEstimationValidite(InfoPratique obj, int index) {
        int estimationValidite = index;
        obj.setEstimationValidite(estimationValidite);
    }*/

	public void setGeoLocation(InfoPratique obj, int index) {
        double[] geoLocation = { new Integer(index+10).doubleValue()+48.890681, new Integer(index+2).doubleValue()+2.335911 };
        obj.setGeoLocation(geoLocation);
    }

	public void setInfoVerif(InfoPratique obj, int index) {
        Boolean infoVerif = true;
        obj.setInfoVerif(infoVerif);
    }

	public void setLocation(InfoPratique obj, int index) {
        String location = "location_" + index;
        obj.setLocation(location);
    }

	public void setLocationLat(InfoPratique obj, int index) {
        double locationLat = new Integer(index).doubleValue();
        obj.setLocationLat(locationLat);
    }

	public void setLocationLng(InfoPratique obj, int index) {
        double locationLng = new Integer(index).doubleValue();
        obj.setLocationLng(locationLng);
    }

	public void setNomade(InfoPratique obj, int index) {
		 UserNomade nomade = userNomadeDataOnDemand.getSpecificUserNomade(index);
        obj.setNomade(nomade);
    }

	public void setPhoto(InfoPratique obj, int index) {
        String photo = "";
        obj.setPhoto(photo);
    }

	public void setSelecteur(InfoPratique obj, int index) {
        String selecteur = "selecteur_" + index;
        obj.setSelecteur(selecteur);
    }

	public void setSelecteur1(InfoPratique obj, int index) {
        String selecteur1 = "selecteur1_" + index;
        obj.setSelecteur1(selecteur1);
    }

	public void setTitle(InfoPratique obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }

	public void setVoteNegatif(InfoPratique obj, int index) {
        int voteNegatif = index;
        obj.setVoteNegatif(voteNegatif);
    }

	public void setVotePositif(InfoPratique obj, int index) {
        int votePositif = index;
        obj.setVotePositif(votePositif);
    }

	public InfoPratique getSpecificInfoPratique(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        InfoPratique obj = data.get(index);
        BigInteger id = obj.getId();
        return infoPratiqueService.findInfoPratique(id);
    }

	public InfoPratique getRandomInfoPratique() {
        init();
        InfoPratique obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return infoPratiqueService.findInfoPratique(id);
    }

	public boolean modifyInfoPratique(InfoPratique obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = infoPratiqueService.findInfoPratiqueEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'InfoPratique' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<InfoPratique>();
        for (int i = 0; i < 10; i++) {
            InfoPratique obj = getNewTransientInfoPratique(i);
            try {
                infoPratiqueService.saveInfoPratique(obj);
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
