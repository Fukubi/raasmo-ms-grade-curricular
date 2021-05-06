package com.rasmoo.client.escola.gradecurricular.v1.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CursoException extends RuntimeException {

	private static final long serialVersionUID = -3683181237742403180L;
	
	private final HttpStatus httpStatus;
	
	public CursoException(final String mensagem, final HttpStatus httpStatus) {
		super(mensagem);
		this.httpStatus = httpStatus;
	}

}
