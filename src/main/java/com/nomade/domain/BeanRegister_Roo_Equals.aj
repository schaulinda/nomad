// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.BeanRegister;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

privileged aspect BeanRegister_Roo_Equals {
    
    public boolean BeanRegister.equals(Object obj) {
        if (!(obj instanceof BeanRegister)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BeanRegister rhs = (BeanRegister) obj;
        return new EqualsBuilder().append(email, rhs.email).append(password, rhs.password).append(userName, rhs.userName).isEquals();
    }
    
    public int BeanRegister.hashCode() {
        return new HashCodeBuilder().append(email).append(password).append(userName).toHashCode();
    }
    
}
