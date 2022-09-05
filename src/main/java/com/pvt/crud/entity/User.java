package com.pvt.crud.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
	/**
	 *
	 */
	private static final long serialVersionUID = 3045687331453904129L;

	public enum UserActive {
		INACTIVE, ACTIVE
	}

	public enum SNSType {
		NONE, FACEBOOK, GOOGLE, APPLE, ZALO;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column
	private String address;

	@Column
	private String name;

	@Column(name = "birth_date")
	private String birthDate;

	@Column(name = "date_joined")
	private Date dateJoined;

	@Column(name = "avatar_url")
	private String avatarUrl;

	@Column(name = "sns_type")
	private SNSType snsType;

	@Column(name = "sns_id")
	private String snsId;

	@Column(columnDefinition = "tinyint(1) default 0")
	private UserActive active;

	@Column(name = "is_change_name", columnDefinition = "bool default false")
	private boolean isChangeName;

	@CreatedDate
	@Column(name = "createdAt")
	private Date createdAt;

	@Column(name = "updatedAt")
	private Date updatedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
			@JoinColumn(name = "roleId") })
	private List<Role> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
