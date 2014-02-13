package com.nomade.tools;

import java.util.UUID;

/**
 * The id generator is a class that generates entity id's based on
 * a synchronized time, the bean name and server id.
 *  
 * @author guymoyo
 *
 */
public class IdGenerator {
	
	public static final String generateId(){
		return UUID.randomUUID().toString();
	}
}
