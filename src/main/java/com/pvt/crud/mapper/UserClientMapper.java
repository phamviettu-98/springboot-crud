package com.pvt.crud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pvt.crud.dto.UserClientDTO;
import com.pvt.crud.entity.User;
import com.pvt.crud.entity.UserClient;


@Mapper
public interface UserClientMapper {
	public UserClientMapper INSTANCE = Mappers.getMapper(UserClientMapper.class);

	public abstract UserClientDTO toUserDTO(User user);
	
	public abstract UserClient toUser(UserClientDTO userDTO);
}
