// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.BeanSubTopicView;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

privileged aspect BeanSubTopicView_Roo_Equals {
    
    public boolean BeanSubTopicView.equals(Object obj) {
        if (!(obj instanceof BeanSubTopicView)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BeanSubTopicView rhs = (BeanSubTopicView) obj;
        return new EqualsBuilder().append(lastMessageDate, rhs.lastMessageDate).append(numberOfDiscussion, rhs.numberOfDiscussion).append(numberOfMessage, rhs.numberOfMessage).append(subTopic, rhs.subTopic).isEquals();
    }
    
    public int BeanSubTopicView.hashCode() {
        return new HashCodeBuilder().append(lastMessageDate).append(numberOfDiscussion).append(numberOfMessage).append(subTopic).toHashCode();
    }
    
}
