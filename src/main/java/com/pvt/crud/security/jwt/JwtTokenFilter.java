package com.pvt.crud.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt.crud.dto.ResponseObjectTemplate;


public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {

        try{
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
        	
        	String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        	if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }else {
            	if(!validUrl(request)) {
            		throw new InvalidJwtAuthenticationException("invalid token");
            	}
            }
//        	if(token != null && !validUrl(request)) {
//        		if (jwtTokenProvider.validateToken(token)) {
//                    Authentication auth = jwtTokenProvider.getAuthentication(token);
//                    if (auth != null) {
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//                    }
//                }
//        	}else {
//            	if(!validUrl(request)) {
//            		throw new InvalidJwtAuthenticationException("invalid token");
//            	}
//            }
            filterChain.doFilter(req, res);            
        }catch(InvalidJwtAuthenticationException e) {
        	ObjectMapper mapper = new ObjectMapper();
        	HttpServletResponse response = (HttpServletResponse) res;
        	response.setContentType("application/json");
        	PrintWriter out = response.getWriter();
			out.print(mapper.writeValueAsString(
					ResponseObjectTemplate.builder().status("Authorization failed").status_code(401).build()));
        	out.flush();

        }catch(Exception e) {
        	e.printStackTrace();
        	System.err.println("--------------------");
        	ObjectMapper mapper = new ObjectMapper();
        	HttpServletResponse response = (HttpServletResponse) res;
        	response.setContentType("application/json");
        	PrintWriter out = response.getWriter();
        	out.print(mapper.writeValueAsString( ResponseObjectTemplate.builder().status("System Error").status_code(500).build()));
        	out.flush();
        }
        

    }
    
    @SuppressWarnings("unused")
	private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        @SuppressWarnings("rawtypes")
		Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
    
    private boolean validUrl(HttpServletRequest request) {
    	String uri = request.getRequestURI();
    	if("/v1/auth/signin".equals(uri) || "/auth/signin".equals(uri) || "/auth/signup".equals(uri)) {
    		return true;
    	}
    	return false;
    }

}
