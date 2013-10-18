package com.nomade.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

@RooJavaBean
@RooToString
@RooMongoEntity
public class UserNomade {
	
	private static Date accExp = DateUtils.addYears(new Date(), 50);
	
    @Column(unique = true)
    private String userName;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date accountExpiration = accExp;

    @NotNull
    private boolean disableLogin;

    @NotNull
    private boolean accountLocked;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date credentialExpiration = accExp;

    private Set<RoleName> roleNames = new HashSet<RoleName>();

    private Profil profil = new Profil();

    private Account compte = new Account();

    private Vehicule vehicule = new Vehicule();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Album> albums = new HashSet<Album>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<EtapeVoyage> etapeVoyages = new HashSet<EtapeVoyage>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<EtapeVehicule> etapeVehicules = new HashSet<EtapeVehicule>();

	public UserNomade(String userName, String password, boolean accountLocked,
			Set<RoleName> roleNames) {
		super();
		this.userName = userName;
		this.password = encodePassword(password, getSalt());
		this.accountLocked = accountLocked;
		this.roleNames = roleNames;
	}
	
	

	public UserNomade(String userName, String password, boolean disableLogin,
			boolean accountLocked, Set<RoleName> roleNames) {
		super();
		this.userName = userName;
		this.password = encodePassword(password, getSalt());
		this.disableLogin = disableLogin;
		this.accountLocked = accountLocked;
		this.roleNames = roleNames;
	}



	public UserNomade() {
		super();
	}
    
	@PrePersist
    public void prePersist() {
        this.password = encodePassword(password, getSalt());
    }

    public String encodePassword(String password, String salt) {
        if (StringUtils.isEmpty(password) || salt == null) throw new NullPointerException("Neither password nor salt may not be null");
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder.encodePassword(password, salt);
    }

    private final String getSalt() {
        return "ace6b4f53";
    }
    public boolean checkExistingPasword(String currentPassword) {
        String md5EncodedPassword = encodePassword(currentPassword);
        return StringUtils.equals(password, md5EncodedPassword);
    }
    private String encodePassword(String input) {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        md5PasswordEncoder.setEncodeHashAsBase64(false);
        return md5PasswordEncoder.encodePassword(input, getSalt());
    }
    public void changePassword(String password) {
        this.password = encodePassword(password);
    } 
}
