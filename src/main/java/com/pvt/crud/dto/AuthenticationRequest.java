package com.pvt.crud.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5990955385394489773L;
	
	private String username;

	private String password;
}
