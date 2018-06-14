package com.restapi;

import com.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.restapi.security.filter.TokenAuthenticationFilter;
import com.restapi.security.service.TokenAuthenticationService;
import com.restapi.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    protected SecurityConfig(final TokenAuthenticationService tokenAuthenticationService) {
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/userlogin").permitAll()
                .antMatchers("/send").permitAll()
                .antMatchers("/save_question").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
    }
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("ADMIN"));
		auth
				.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
//		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
	}
}
