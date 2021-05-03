package com.rasmoo.client.escola.gradecurricular.service;

import java.util.List;

import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;

public interface ICursoService {

	public Boolean cadastrar(final CursoDto curso);
	
	public List<CursoDto> listar();
	
	public CursoDto consultar(final Long id);
	
	public List<CursoDto> consultarPorCodigo(final String codigo);
	
	public Boolean atualizar(final CursoDto curso);
	
	public Boolean excluir(final Long id);
	
}
