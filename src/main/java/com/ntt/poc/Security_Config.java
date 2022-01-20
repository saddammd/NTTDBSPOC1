package com.ntt.poc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import com.ntt.poc.filters.AuthorizationFilter;
import com.ntt.poc.repository.User_Repository;
import com.ntt.poc.utilities.AuthenticationUtility;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security_Config extends WebSecurityConfigurerAdapter {
	
	@Autowired
	Environment environment;
	
	ArrayList<String> list1 = new ArrayList<>();
	ArrayList<String> list2 = new ArrayList<>();
	ArrayList<String> list3 = new ArrayList<>();
	
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationUtility getAuthenticationUtility() {
		return new AuthenticationUtility(environment);
	}

	@Override
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
		
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
		http.authorizeRequests().antMatchers("/test").permitAll(); //authenticated();//.and().oauth2Login();
		http.authorizeRequests().antMatchers("/poc/register").permitAll();
		http.authorizeRequests().antMatchers("/poc/login", "/oauth2/**").permitAll();
		http.authorizeRequests().antMatchers("/poc/userRoles/**").permitAll();
		http.authorizeRequests().antMatchers("/poc/products")
		.authenticated().and().addFilter(getAuthorizationFilter()).sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and().oauth2Login();       

			
		
		
		http.authorizeRequests().antMatchers("/poc/retailers")
		.authenticated().and().addFilter(getAuthorizationFilter()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and().oauth2Login();
		list1.add("*");
		//list1.add("https://saddammd.github.io/**");
		list2.add("GET");
		list2.add("POST");
		list2.add("PUT");
		list2.add("DELETE");
		list2.add("OPTIONS");
		list3.add("Content-Type: application/json");
				
		
		http.cors().configurationSource(request -> {
		      CorsConfiguration cors = new CorsConfiguration();
		      cors.setAllowedOrigins(list1);
		      cors.setAllowedMethods(list1);
		      cors.setAllowedHeaders(list1);
		      return cors;
		    });
		
   }
	
	public AuthorizationFilter getAuthorizationFilter() throws Exception {

		AuthorizationFilter authenticationFilter = new AuthorizationFilter(authenticationManagerBean(), getAuthenticationUtility());
		return authenticationFilter;					
	
	}
	

}