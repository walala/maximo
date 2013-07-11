package com.zt.maximo.service.domain;

import java.io.Serializable;

public class AppProxyResultDo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean error;
	
	private String errorMessage;
	
	private Object resut;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getResut() {
		return resut;
	}

	public void setResut(Object resut) {
		this.resut = resut;
	}
	
}
