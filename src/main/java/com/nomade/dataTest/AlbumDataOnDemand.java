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

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.nomade.domain.Album;
import com.nomade.service.AlbumService;

@Component
@Configurable
public class AlbumDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Album> data;

	@Autowired
    AlbumService albumService;

	public Album getNewTransientAlbum(int index) {
        Album obj = new Album();
        setCreated(obj, index);
        setName(obj, index);
        setNumPhoto(obj, index);
        setPhotos(obj, index);
        set_id(obj, index);
        return obj;
    }

	public void setCreated(Album obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	public void setName(Album obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setNumPhoto(Album obj, int index) {
        int numPhoto = 0;
        obj.setNumPhoto(numPhoto);
    }

	public void setPhotos(Album obj, int index) {
        String[] photos = { "Y", "N" };
        obj.setPhotos(photos);
    }

	public void set_id(Album obj, int index) {
        ObjectId _id = null;
        obj.set_id(_id);
    }

	public Album getSpecificAlbum(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Album obj = data.get(index);
        BigInteger id = obj.getId();
        return albumService.findAlbum(id);
    }

	public Album getRandomAlbum() {
        init();
        Album obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return albumService.findAlbum(id);
    }

	public boolean modifyAlbum(Album obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = albumService.findAlbumEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Album' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Album>();
        for (int i = 0; i < 10; i++) {
            Album obj = getNewTransientAlbum(i);
            try {
                albumService.saveAlbum(obj);
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
