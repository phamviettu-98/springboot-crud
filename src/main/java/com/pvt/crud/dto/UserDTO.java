package com.pvt.crud.dto;

import java.util.List;

import com.pvt.crud.entity.User.UserActive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	Long id;

	private String username;

	private String email;

	private String name;

	private UserActive active;

	private List<RoleDTO> roles;
}
