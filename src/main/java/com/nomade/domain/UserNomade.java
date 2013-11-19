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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
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
	
	@Indexed
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
    
    public int fieldNonNull(){
    	int som = 0;
    	if(profil.getButVoyage()!=null && !profil.getButVoyage().equals(""))
    		som+=1;
    	if(profil.getFile()!=null && !profil.getFile().equals(""))
    		som+=1;
    	if(profil.getLangues()!=null && !profil.getLangues().equals(""))
    		som+=1;
    	if(profil.getMetier()!=null && !profil.getMetier().equals(""))
    		som+=1;
    	if(profil.getPseudo()!=null && !profil.getPseudo().equals(""))
    		som+=1;
    	if(profil.getVisitedCountry()!=null && !profil.getVisitedCountry().equals(""))
    		som+=1;
    	if(profil.getWebsite()!=null && !profil.getWebsite().equals(""))
    		som+=1;
    	if(compte.getAdress()!=null && !compte.getAdress().equals(""))
    		som+=1;
    	if(compte.getBirthDate()!=null && !compte.getBirthDate().equals(""))
    		som+=1;
    	if(compte.getConfidentiality()!=null && !compte.getConfidentiality().equals(""))
    		som+=1;
    	if(compte.getEmail()!=null && !compte.getEmail().equals(""))
    		som+=1;
    	if(compte.getFullName()!=null && !compte.getFullName().equals(""))
    		som+=1;
    	if(compte.getGender()!=null && !compte.getGender().equals(""))
    		som+=1;
    	if(vehicule.getAnneMiseEnService()!=null && !vehicule.getAnneMiseEnService().equals(""))
    		som+=1;
    	if(vehicule.getCouleur()!=null && !vehicule.getCouleur().equals(""))
    		som+=1;
    	if(vehicule.getDescription()!=null && !vehicule.getDescription().equals(""))
    		som+=1;
    	if(vehicule.getForSellCountry()!=null && !vehicule.getForSellCountry().equals(""))
    		som+=1;
    	if(vehicule.getModel()!=null && !vehicule.getModel().equals(""))
    		som+=1;
    	if(vehicule.getPhoto()!=null && !vehicule.getPhoto().equals(""))
    		som+=1;
    	if(vehicule.getVehiculeName()!=null && !vehicule.getVehiculeName().equals(""))
    		som+=1;
    	if(vehicule.getVehiculeState()!=null && !vehicule.getVehiculeState().equals(""))
    		som+=1;
    	if(vehicule.getVehiculeType()!=null && !vehicule.getVehiculeType().equals(""))
    		som+=1;
    	
    	return som;
    }
    
    public int fieldPercent(){
    	final int numberField = 22;
    	int fieldNonNull = fieldNonNull();
    	return fieldNonNull*100/numberField;
    }
    
    public void findAndRemAlbum(String albumId){
    	for(Album a:this.albums){
    		if(a.get_id().toString().equals(albumId)){
    			this.albums.remove(a);
    			break;
    		}
    	}
    }    

}
