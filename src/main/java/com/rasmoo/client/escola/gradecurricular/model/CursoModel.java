package com.rasmoo.client.escola.gradecurricular.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CursoModel {
	@NotBlank(message = "nome deve ser preenchido")
	@Size(min = 10, max = 30)
	private String nome;
	
	@NotBlank(message = "código deve ser preenchido")
	@Size(min = 10, max = 30)
	private String codCurso;
	
	private List<Long> materias;
}