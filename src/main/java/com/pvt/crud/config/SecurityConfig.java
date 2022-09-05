package com.pvt.crud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.pvt.crud.security.jwt.Http401UnauthorizedEntryPoint;
import com.pvt.crud.security.jwt.JwtConfigurer;
import com.pvt.crud.security.jwt.JwtTokenProvider;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	   String[] staticResources  =  {
			   "/upload/**",
		        "/css/**",
		        "/images/**",
		        "/fonts/**",
		        "/scripts/**",
		        "/resources/**",
		        "/image/**"
		    };


	@Autowired
	private Http401UnauthorizedEntryPoint authenticationEntryPoint;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	public void configure(WebSecurity web) throws Exception {
	      web.ignoring()
          .antMatchers("/upload/**","/image/**","image/**","/css/**", "/js/**", "/images/**",
                       "/webjars/**", "/webjarsjs","/resources/**");
		super.configure(web);
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
//        http
//            .httpBasic().disable()
//            .csrf().disable()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//                .authorizeRequests()
////                .antMatchers("/auth/**").permitAll()
////                .antMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
//                .anyRequest().permitAll()
//            .and()
//            .apply(new JwtConfigurer(jwtTokenProvider));
//        //@formatter:on

		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable().headers().frameOptions()
				.disable()
				.and()
				.authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/upload/**").permitAll()
				.antMatchers("/image/**").permitAll()
				.antMatchers("/api/**").hasAuthority("admin")
				.antMatchers("/").permitAll()
				.anyRequest().authenticated().and().apply(new JwtConfigurer(jwtTokenProvider));
	}

}
