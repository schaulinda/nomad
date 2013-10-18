package com.nomade.tools;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValideEmailUtil {

	private static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

	public static boolean isValid(String emailAddress) {
		
		if(StringUtils.isNotBlank(emailAddress) && StringUtils.isNotEmpty(emailAddress))
			return PATTERN.matcher(emailAddress).matches();
		else
			return false;
	}

}
