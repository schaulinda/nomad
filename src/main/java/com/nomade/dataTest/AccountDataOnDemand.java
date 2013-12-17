package com.nomade.dataTest;

import com.nomade.domain.Account;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Gender;
import com.nomade.domain.Nationality;
import com.nomade.service.AccountService;
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
public class AccountDataOnDemand {
	
	public void setEmail(Account obj, int index) {
        String email = "nomad" + index + "@google.com";
        obj.setEmail(email);
    }

	private Random rnd = new SecureRandom();

	private List<Account> data;

	@Autowired
    AccountService accountService;

	public Account getNewTransientAccount(int index) {
        Account obj = new Account();
        setAdress(obj, index);
        setBirthDate(obj, index);
        setCommercialParteners(obj, index);
        setConfidentiality(obj, index);
        setEmail(obj, index);
        setFullName(obj, index);
        setGender(obj, index);
        setNationality(obj, index);
        setNewsletter(obj, index);
        setNotifications(obj, index);
        setPhoneNumber(obj, index);
        return obj;
    }

	public void setAdress(Account obj, int index) {
        String adress = "adress_" + index;
        obj.setAdress(adress);
    }

	public void setBirthDate(Account obj, int index) {
        Date birthDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setBirthDate(birthDate);
    }

	public void setCommercialParteners(Account obj, int index) {
        Boolean commercialParteners = Boolean.TRUE;
        obj.setCommercialParteners(commercialParteners);
    }

	public void setConfidentiality(Account obj, int index) {
        Confidentiality confidentiality = Confidentiality.class.getEnumConstants()[0];
        obj.setConfidentiality(confidentiality);
    }

	public void setFullName(Account obj, int index) {
        String fullName = "fullName_" + index;
        obj.setFullName(fullName);
    }

	public void setGender(Account obj, int index) {
        Gender gender = Gender.class.getEnumConstants()[0];
        obj.setGender(gender);
    }

	public void setNationality(Account obj, int index) {
        Nationality nationality = Nationality.class.getEnumConstants()[0];
        obj.setNationality(nationality);
    }

	public void setNewsletter(Account obj, int index) {
        Boolean newsletter = Boolean.TRUE;
        obj.setNewsletter(newsletter);
    }

	public void setNotifications(Account obj, int index) {
        Boolean notifications = Boolean.TRUE;
        obj.setNotifications(notifications);
    }

	public void setPhoneNumber(Account obj, int index) {
        String phoneNumber = "phoneNumber_" + index;
        obj.setPhoneNumber(phoneNumber);
    }

	public Account getSpecificAccount(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Account obj = data.get(index);
        BigInteger id = obj.getId();
        return accountService.findAccount(id);
    }

	public Account getRandomAccount() {
        init();
        Account obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return accountService.findAccount(id);
    }

	public boolean modifyAccount(Account obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = accountService.findAccountEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Account' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Account>();
        for (int i = 0; i < 10; i++) {
            Account obj = getNewTransientAccount(i);
            try {
                accountService.saveAccount(obj);
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
