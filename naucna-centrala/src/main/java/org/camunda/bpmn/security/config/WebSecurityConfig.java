package org.camunda.bpmn.security.config;

import org.camunda.bpmn.security.CustomUserDetailsService;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.security.auth.RestAuthenticationEntryPoint;
import org.camunda.bpmn.security.auth.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, proxyTargetClass = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Autowired
    private CustomUserDetailsService userDetailsService;

	// Neautorizovani pristup zastcenim resursima
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	TokenUtils tokenUtils;
	
	@Autowired
	private CustomUserDetailsService jwtUserDetailsService;	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}	
    
   
	// Definisemo prava pristupa odredjenim URL-ovima
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//		.csrf().disable()
		
			// komunikacija izmedju klijenta i servera je stateless
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			 .rememberMe().and()
			// za neautorizovane zahteve posalji 401 gresku
			.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
			
			.authorizeRequests()
				.antMatchers("/users/**").permitAll()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/repository/**").permitAll()
				.antMatchers("/magazines/**").permitAll()
				.antMatchers("/kp/**").permitAll()
				.antMatchers("/orders/**").permitAll()
				.antMatchers("/science-paper/**").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				// svaki zahtev mora biti autorizovan
				.anyRequest().authenticated()
				.and()
			// presretni svaki zahtev filterom
			.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService), BasicAuthenticationFilter.class)
				;
		http.headers().frameOptions().disable();
//		http.headers().contentSecurityPolicy("script-src 'self' https://localhost:4200; object-src https://localhost:4200");
	}
	

}
