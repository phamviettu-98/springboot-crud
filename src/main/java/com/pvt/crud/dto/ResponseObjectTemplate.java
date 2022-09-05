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
public class ResponseObjectTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6976881484261561517L;
	private int status_code;
	private String status;
	private Object data;
	private long total;
	private long page;
	private long size;

	public ResponseObjectTemplate(int status_code, String status) {
		this.status_code = status_code;
		this.status = status;
	}

	public ResponseObjectTemplate(int status_code, String status, Object data) {
		this(status_code, status);
		this.data = data;
	}
}
