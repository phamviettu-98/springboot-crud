package com.pvt.crud.mapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pvt.crud.dto.Signup;
import com.pvt.crud.dto.UserDTO;
import com.pvt.crud.entity.User;


@Mapper
public interface UserMapper {
	public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	public abstract UserDTO toUserDTO(User user);
	
	public abstract List<UserDTO> toUserDTOs(List<User> users);
	
	public abstract User toUser(UserDTO userDTO);
	
	public abstract User toUserFromSignup(Signup signup);
}
