// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.domain;

import com.nomade.domain.UserNomade;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect UserNomade_Roo_Json {
    
    public String UserNomade.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static UserNomade UserNomade.fromJsonToUserNomade(String json) {
        return new JSONDeserializer<UserNomade>().use(null, UserNomade.class).deserialize(json);
    }
    
    public static String UserNomade.toJsonArray(Collection<UserNomade> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<UserNomade> UserNomade.fromJsonArrayToUserNomades(String json) {
        return new JSONDeserializer<List<UserNomade>>().use(null, ArrayList.class).use("values", UserNomade.class).deserialize(json);
    }
    
}
