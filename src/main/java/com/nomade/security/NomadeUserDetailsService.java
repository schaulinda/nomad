package com.nomade.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.service.UserService;

@Service
@Transactional
public class NomadeUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		try {

			if (StringUtils.isBlank(username)) {
				throw new UsernameNotFoundException(
						"user not found");
			}
			List<UserNomade> resultList = userService.findByUserName(username);
			if(resultList!=null && resultList.size()>0){
				UserNomade userNomade = null;
				for (UserNomade found : resultList) {
					userNomade = found;
				}
				if (userNomade == null)
					throw new UsernameNotFoundException(
							"user not found");
				return createUserDetails(userNomade);
			}else{
				List<UserNomade> resultList1 = userService.findByEmail(username);
				
					UserNomade userNomade = null;
					for (UserNomade found : resultList1) {
						userNomade = found;
					}
					if (userNomade == null)
						throw new UsernameNotFoundException(
								"user not found");
					return createUserDetails(userNomade);
			}
		} finally {

		}
	}

	public static UserDetails createUserDetails(UserNomade userNomade)
			throws UsernameNotFoundException, DataAccessException {
		String password = userNomade.getPassword();
		boolean enabled = !userNomade.isDisableLogin();
		Date accountExpiration = userNomade.getAccountExpiration();
		boolean accountNonExpired = false;
		if (accountExpiration != null) {
			accountNonExpired = new Date().before(accountExpiration);
		}
		Date credentialExpiration = userNomade.getCredentialExpiration();
		boolean credentialsNonExpired = false;
		if (credentialExpiration != null) {
			credentialsNonExpired = new Date().before(credentialExpiration);
		}
		boolean accountNonLocked = !userNomade.isAccountLocked();

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		Set<RoleName> roles = userNomade.getRoleNames();
		for (RoleName roleName : roles) {
			@SuppressWarnings("deprecation")
			GrantedAuthorityImpl ga = new GrantedAuthorityImpl(roleName.name());
			authorities.add(ga);
		}
		UserDetails userDetails = new User(userNomade.getUserName(), password,
				enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		return userDetails;
	}
}
