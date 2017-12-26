package com.example.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
	private final String token;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		token = request.getParameter("token");
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; Token: ").append(this.getToken());
		return sb.toString();
	}

	private static final long serialVersionUID = 1L;

}
