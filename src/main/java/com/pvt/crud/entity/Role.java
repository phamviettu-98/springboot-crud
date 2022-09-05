package com.pvt.crud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Role implements GrantedAuthority{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    @Column
    private String name;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

	@Override
	public String getAuthority() {
		return name;
	}

}
