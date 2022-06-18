package com.bugTracker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws ServletException, IOException {
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		String redirectURL = request.getContextPath();
		
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			redirectURL = "/login";
		}else {
			redirectURL = "/dashboard";
		}
		
		//check the role of the logged-in user, and then redirect it to the appropriate pages with the right role
		/*if (userDetails.hasRole("Admin")) {
			redirectURL = "/admin";
		} else if (userDetails.hasRole("User")) {
			redirectURL = "/dashboard";
		}*/

		response.sendRedirect(redirectURL);
	}
}
