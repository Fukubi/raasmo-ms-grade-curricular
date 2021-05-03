package com.rasmoo.client.escola.gradecurricular.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.escola.gradecurricular.constant.HyperLinkConstant;
import com.rasmoo.client.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.service.IMateriaService;

@RestController
@RequestMapping("/materia")
public class MateriaController {

	@Autowired
	private IMateriaService materiaService;

	@GetMapping
	public ResponseEntity<Response<List<MateriaDto>>> listarMaterias() {
		Response<List<MateriaDto>> response = new Response<>();
		response.setData(this.materiaService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<MateriaDto>> consultarMateria(@PathVariable Long id) {
		Response<MateriaDto> response = new Response<>();
		response.setData(this.materiaService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).deletarMateria(id))
				.withRel(HyperLinkConstant.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(response.getData()))
				.withRel(HyperLinkConstant.ATUALIZAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<Response<Boolean>> cadastrarMaterias(@Valid @RequestBody MateriaDto materia) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.cadastrar(materia));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).cadastrarMaterias(materia)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(materia)).withRel(HyperLinkConstant.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping
	public ResponseEntity<Response<Boolean>> atualizarMateria(@Valid @RequestBody MateriaDto materia) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.atualizar(materia));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(materia)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).deletarMateria(materia.getId()))
				.withRel(HyperLinkConstant.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(materia.getId()))
				.withRel(HyperLinkConstant.CONSULTAS.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> deletarMateria(@PathVariable Long id) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).deletarMateria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/horario-minimo/{horaMinima}")
	public ResponseEntity<Response<List<MateriaDto>>> consultarMateriaPorHoraMinima(@PathVariable int horaMinima) {
		Response<List<MateriaDto>> response = new Response<>();
		List<MateriaDto> materias = this.materiaService.listarPorHorarioMinimo(horaMinima);
		response.setData(materias);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateriaPorHoraMinima(horaMinima))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/frequencia/{frequencia}")
	public ResponseEntity<Response<List<MateriaDto>>> consultarMateriaPorFrequencia(@PathVariable int frequencia) {
		Response<List<MateriaDto>> response = new Response<>();
		List<MateriaDto> materias = this.materiaService.listarPorFequencia(frequencia);
		response.setData(materias);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateriaPorFrequencia(frequencia))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
