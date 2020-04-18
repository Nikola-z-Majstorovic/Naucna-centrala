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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled=true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

		// Implementacija PasswordEncoder-a koriscenjem BCrypt hashing funkcije.
		// BCrypt po defalt-u radi 10 rundi hesiranja prosledjene vrednosti.
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Autowired
		private CustomUserDetailsService jwtUserDetailsService;
		
		// Neautorizovani pristup zastcenim resursima
		@Autowired
		private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
		
		@Autowired
		TokenUtils tokenUtils;
		
		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		// Definisemo nacin autentifikacije
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
		}
		
		
		// Definisemo prava pristupa odredjenim URL-ovima
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
			
				// komunikacija izmedju klijenta i servera je stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				
				// za neautorizovane zahteve posalji 401 gresku
				.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
				
				.authorizeRequests()

	                .antMatchers("/h2-console/**").permitAll()
					.antMatchers("/users/**").permitAll()
					.antMatchers("/auth/**").permitAll()
					.antMatchers("/admin/**").permitAll()
					.antMatchers("/repository/**").permitAll()
					.antMatchers("/magazines/**").permitAll()
					.antMatchers("/test/**").permitAll()
					.antMatchers("/kp/**").permitAll()
					.antMatchers("/orders/**").permitAll()
					.antMatchers("/science-paper/**").permitAll()
					.antMatchers("/**").permitAll()
					// svaki zahtev mora biti autorizovan
					.anyRequest().authenticated().and()
				// presretni svaki zahtev filterom
				.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService), BasicAuthenticationFilter.class);
			http.headers().frameOptions().disable();
			http.headers().contentSecurityPolicy("script-src 'self' https://localhost:4200; object-src https://localhost:4200");
		}
		
		
		// Generalna bezbednost aplikacije
		@Override
		public void configure(WebSecurity web) throws Exception {
			// TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
			web.ignoring().antMatchers(HttpMethod.POST, "/auth/login");
			web.ignoring().antMatchers(HttpMethod.POST, "/auth/logout");
			// nije potrebna autentifikacija za obicne user metode
			web.ignoring().antMatchers(HttpMethod.POST, "/users/**");
			web.ignoring().antMatchers(HttpMethod.GET, "/users/**");
			web.ignoring().antMatchers(HttpMethod.PUT, "/users/**");

			web.ignoring().antMatchers(HttpMethod.GET, "/magazines");
			web.ignoring().antMatchers(HttpMethod.POST, "/orders/init");
			web.ignoring().antMatchers(HttpMethod.GET, "/test/**");
			web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
		}
		

	
}
