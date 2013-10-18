package com.nomade.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooMongoEntity
public class Account {

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;

    private String fullName;

    @Enumerated
    private Gender gender;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;

    @Enumerated
    private Nationality nationality;

    private String adress;

    private String phoneNumber;

    @Enumerated
    private Confidentiality confidentiality;

    private Boolean newsletter;

    private Boolean notifications;

    private Boolean commercialParteners;
}
