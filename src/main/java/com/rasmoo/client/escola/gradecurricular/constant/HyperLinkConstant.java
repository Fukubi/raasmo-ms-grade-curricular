package com.rasmoo.client.escola.gradecurricular.constant;

import lombok.Getter;

@Getter
public enum HyperLinkConstant {
	
	ATUALIZAR("UPDATE"),
	EXCLUIR("DELETE"),
	LISTAR("GET_ALL"),
	CONSULTAS("GET");
	
	private final String valor;
	
	private HyperLinkConstant(String valor) {
		this.valor = valor;
	}
}
