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
import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.model.CursoModel;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.service.ICursoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.CURSO)
@RestController
@RequestMapping("/curso")
public class CursoController {

	@Autowired
	private ICursoService cursoService;
	
	@ApiOperation(value = "Cadastrar um novo curso")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Entidade criada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@PostMapping
	public ResponseEntity<Response<Boolean>> cadastrarCurso(@Valid @RequestBody CursoModel curso) {
		Response<Boolean> response = new Response<>();
		response.setData(this.cursoService.cadastrar(curso));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).cadastrarCurso(curso))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).atualizarCurso(curso))
				.withRel(HyperLinkConstant.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).listarCurso())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@ApiOperation(value = "Listar todos os cursos cadastrados")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de cursos exibida com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@GetMapping
	public ResponseEntity<Response<List<CursoDto>>> listarCurso() {
		Response<List<CursoDto>> response = new Response<>();
		response.setData(this.cursoService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).listarCurso())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation(value = "Consultar curso por código")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso encontrado com sucesso"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@GetMapping("/codigo/{codCurso}")
	public ResponseEntity<Response<CursoDto>> consultarCursoPorCodigo(@PathVariable String codCurso) {
		Response<CursoDto> response = new Response<>();
		response.setData(this.cursoService.consultarPorCodigo(codCurso));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).consultarCursoPorCodigo(codCurso))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).listarCurso())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation(value = "Consultar por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Entidade criada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@GetMapping("/id/{id}")
	public ResponseEntity<Response<CursoDto>> consultar(@PathVariable Long id) {
		Response<CursoDto> response = new Response<>();
		response.setData(this.cursoService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).consultar(id))
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation(value = "Atualizar um curso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@PutMapping
	public ResponseEntity<Response<Boolean>> atualizarCurso(@Valid @RequestBody CursoModel curso) {
		Response<Boolean> response = new Response<>();
		response.setData(this.cursoService.atualizar(curso));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).atualizarCurso(curso))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).listarCurso())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation(value = "Excluir um curso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Entidade criada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno do servidor")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> deletarCurso(@PathVariable Long id) {
		Response<Boolean> response = new Response<>();
		response.setData(this.cursoService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).deletarCurso(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).listarCurso())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
