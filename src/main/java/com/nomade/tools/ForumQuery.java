/**
 * 
 */
package com.nomade.tools;

import java.util.Date;
import java.util.List;

import com.nomade.domain.Confidentiality;
import com.nomade.domain.UserNomade;

/**
 * @author bwa
 *
 */
public interface ForumQuery<T> {

    public T findByTitle(String title);
    public List<T> findByNomade(UserNomade nomade);
    public List<T> findByConfidentiality(Confidentiality confidentiality);
    public List<T> findByCreated(Date date);
}
