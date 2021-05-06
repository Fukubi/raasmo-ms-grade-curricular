package com.rasmoo.client.escola.gradecurricular.v1.service;

import java.util.List;

import com.rasmoo.client.escola.gradecurricular.v1.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.v1.model.CursoModel;

public interface ICursoService {

	public Boolean cadastrar(final CursoModel curso);
	
	public List<CursoDto> listar();
	
	public CursoDto consultar(final Long id);
	
	public CursoDto consultarPorCodigo(final String codigo);
	
	public Boolean atualizar(final CursoModel curso);
	
	public Boolean excluir(final Long id);
	
}
