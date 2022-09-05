package com.pvt.crud.dto;

import com.pvt.crud.entity.User.UserActive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserClientDTO {
	Long id;
	
	private String username;

	private String email;

	private String note;

	private UserActive active;
}
