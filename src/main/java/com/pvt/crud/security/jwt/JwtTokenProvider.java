package com.pvt.crud.security.jwt;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt.crud.dto.UserDTO;
import com.pvt.crud.entity.User;
import com.pvt.crud.mapper.UserMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.pvt.crud.security.jwt.InvalidJwtAuthenticationException;


@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "FPzwZepsBZ1FX52gi07xEVrmeozf84xoeo77vGVpZmDJiTcZqD3p2nsHvpIqhtuu";

	@Value("${security.jwt.token.expired}")
	private long validityInMilliseconds = 86400000; // 1day 86400000

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(UserDTO user) {

		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("user", user);

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	public Authentication getAuthentication(String token) {
//    	customUserDetailsService.loadUserByUsername(getUsername(token));//
		UserDetails userDetails = getUser(token);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public User getUser(String token) {
		UserDTO userDTO = null;
		try {
			final ObjectMapper mapper = new ObjectMapper();
			Claims jwsMap = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			userDTO =  mapper.convertValue(jwsMap.get("user"), UserDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			userDTO = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("user", UserDTO.class);
		}
		return UserMapper.INSTANCE.toUser(userDTO);
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
		}
	}

}

