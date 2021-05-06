package com.rasmoo.client.escola.gradecurricular.v1.service;

import java.util.List;

import com.rasmoo.client.escola.gradecurricular.v1.dto.MateriaDto;

public interface IMateriaService {
	
	public List<MateriaDto> listar();
	
	public MateriaDto consultar(final Long id);
	
	public Boolean cadastrar(final MateriaDto materia);

	public Boolean atualizar(final MateriaDto materia);
	
	public Boolean excluir(final Long id);
	
	public List<MateriaDto> listarPorHorarioMinimo(final int horaMinima);
	
	public List<MateriaDto> listarPorFequencia(final int frequencia);
	
}
