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

import com.rasmoo.client.escola.gradecurricular.config.SwaggerConfig;
import com.rasmoo.client.escola.gradecurricular.constant.HyperLinkConstant;
import com.rasmoo.client.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.service.IMateriaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.MATERIA)
@RestController
@RequestMapping("/materia")
public class MateriaController {

	@Autowired
	private IMateriaService materiaService;

	@ApiOperation("Listar todas as matérias cadastradas")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matérias listadas com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
	@GetMapping
	public ResponseEntity<Response<List<MateriaDto>>> listarMaterias() {
		Response<List<MateriaDto>> response = new Response<>();
		response.setData(this.materiaService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Consultar matéria por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria consultada com sucesso"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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

	@ApiOperation("Cadastrar nova matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Matéria cadastrada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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

	@ApiOperation("Atualizar uma matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria atualizada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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

	@ApiOperation("Deletar uma matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria excluida com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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

	@ApiOperation("Listar matérias por horário minimo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matérias listadas com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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

	@ApiOperation("Listar matérias por frequência")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matérias listadas com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição"),
			@ApiResponse(code = 500, message = "Erro interno no servidor")
	})
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
