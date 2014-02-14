/**
 * 
 */
package com.nomade.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bwa
 *
 */
public class CollectionUtil {
	
	public static <E> E getLastElement(Collection<E> elements){
		List<E> transformedList = new ArrayList<E>(elements);
		int size = transformedList.size();
		int lastIndex = 0;
		if(size >0) {
			lastIndex = size -1;
			return transformedList.get(lastIndex);
		}else {
			return null;
		}
	}
}
