package com.nomade.dataTest;

import com.nomade.domain.FriendState;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;
import com.nomade.service.RelationService;
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
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class RelationDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Relation> data;

	@Autowired
    RelationService relationService;

	public Relation getNewTransientRelation(int index) {
        Relation obj = new Relation();
        setFriendState(obj, index);
        setNomade(obj, index);
        setNomade2(obj, index);
        return obj;
    }

	public void setFriendState(Relation obj, int index) {
        FriendState friendState = null;
        obj.setFriendState(friendState);
    }

	public void setNomade(Relation obj, int index) {
        UserNomade nomade = null;
        obj.setNomade(nomade);
    }

	public void setNomade2(Relation obj, int index) {
        UserNomade nomade2 = null;
        obj.setNomade2(nomade2);
    }

	public Relation getSpecificRelation(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Relation obj = data.get(index);
        BigInteger id = obj.getId();
        return relationService.findRelation(id);
    }

	public Relation getRandomRelation() {
        init();
        Relation obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return relationService.findRelation(id);
    }

	public boolean modifyRelation(Relation obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = relationService.findRelationEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Relation' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Relation>();
        for (int i = 0; i < 10; i++) {
            Relation obj = getNewTransientRelation(i);
            try {
                relationService.saveRelation(obj);
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
