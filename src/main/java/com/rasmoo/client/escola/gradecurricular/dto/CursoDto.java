package com.rasmoo.client.escola.gradecurricular.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CursoDto extends RepresentationModel<CursoDto> {
	
	private Long id;
	
	@NotBlank(message = "nome deve ser preenchido")
	@Size(min = 10, max = 30, message = "O nome deve ter no máximo 10 caracteres e no mínimo 30")
	private String nome;
	
	@NotBlank(message = "código deve ser preenchido")
	@Size(min = 3, max = 6, message = "O código deve ter no máximo 6 caracteres e no mínimo 3")
	private String codCurso;
	
	private List<MateriaEntity> materias;
}
