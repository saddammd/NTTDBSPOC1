//package com.ntt.poc.service;
//
//import java.util.Collection;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import com.ntt.poc.entities.User;
//
//public class CustomOAuth2User implements OAuth2User {
//	
//	private OAuth2User oauth2User;
//	
//	@Autowired
//	private User_ServiceImpl user_ServiceImpl;
//    
//    public CustomOAuth2User(OAuth2User oauth2User) {
//        this.oauth2User = oauth2User;
//    }
//
//	@Override
//	public Map<String, Object> getAttributes() {
//		return oauth2User.getAttributes();
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return oauth2User.getAuthorities();
//	}
//
//	@Override
//	public String getName() {
//		 return oauth2User.getAttribute("name");
//	}
//	
//	public String getEmail() {
//        return oauth2User.<String>getAttribute("email");     
//    }	
//
//}