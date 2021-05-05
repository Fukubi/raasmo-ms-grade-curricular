package com.rasmoo.client.escola.gradecurricular.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.model.CursoModel;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.service.ICursoService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CursoControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private ICursoService cursoService;

	@Autowired
	private TestRestTemplate restTemplate;

	private static CursoDto cursoDto;
	private static List<MateriaEntity> materias;

	@BeforeAll
	public static void init() {
		cursoDto = new CursoDto();
		cursoDto.setId(1L);
		cursoDto.setNome("PROGRAMACAO BASICA");
		cursoDto.setCodigo("PB001");

		MateriaEntity m1 = new MateriaEntity();
		m1.setId(1L);
		m1.setCodigo("ILP");
		m1.setFrequencia(1);
		m1.setHoras(64);
		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

		MateriaEntity m2 = new MateriaEntity();
		m2.setId(2L);
		m2.setCodigo("IAC");
		m2.setFrequencia(2);
		m2.setHoras(80);
		m2.setNome("INTRODUCAO A ALGORITMOS COMPUTACIONAIS");

		MateriaEntity m3 = new MateriaEntity();
		m3.setId(3L);
		m3.setCodigo("POO");
		m3.setFrequencia(1);
		m3.setHoras(90);
		m3.setNome("PROGRAMACAO ORIENTADA A OBJETOS");

		materias = new ArrayList<>();
		materias.addAll(List.of(m1, m2, m3));

		cursoDto.setMaterias(materias);
	}

	@Test
	void testListarCurso() {
		Mockito.when(this.cursoService.listar()).thenReturn(new ArrayList<CursoDto>());

		ResponseEntity<Response<List<CursoDto>>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso/", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<CursoDto>>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getStatusCode().value());
	}

	@Test
	void testConsultarCurso() {
		Mockito.when(this.cursoService.consultar(1L)).thenReturn(cursoDto);

		ResponseEntity<Response<CursoDto>> cursos = restTemplate.exchange("http://localhost:" + this.port + "/curso/id/1",
				HttpMethod.GET, null, new ParameterizedTypeReference<Response<CursoDto>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getStatusCode().value());
	}
	
	@Test
	void testConsultarCursoPorCodigo() {
		Mockito.when(this.cursoService.consultarPorCodigo("PB001")).thenReturn(cursoDto);

		ResponseEntity<Response<CursoDto>> cursos = restTemplate.exchange("http://localhost:" + this.port + "/curso/codigo/PB001",
				HttpMethod.GET, null, new ParameterizedTypeReference<Response<CursoDto>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getStatusCode().value());
	}
	
	@Test
	void testCadastrarCurso() {
		CursoModel cursoModel = this.converterCursoDtoParaModel(cursoDto);

		Mockito.when(this.cursoService.cadastrar(cursoModel)).thenReturn(Boolean.TRUE);

		HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange("http://localhost:" + this.port + "/curso/",
				HttpMethod.POST, request, new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(201, cursos.getStatusCode().value());
	}
	
	@Test
	void testAtualizarCurso() {
		CursoModel cursoModel = this.converterCursoDtoParaModel(cursoDto);

		Mockito.when(this.cursoService.atualizar(cursoModel)).thenReturn(Boolean.TRUE);

		HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange("http://localhost:" + this.port + "/curso/",
				HttpMethod.PUT, request, new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getStatusCode().value());
	}
	
	@Test
	void testExcluirCurso() {
		Mockito.when(this.cursoService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange("http://localhost:" + this.port + "/curso/1",
				HttpMethod.DELETE, null, new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getStatusCode().value());
	}

	private CursoModel converterCursoDtoParaModel(CursoDto cursoDto) {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setCodigo(cursoDto.getCodigo());
		cursoModel.setNome(cursoDto.getNome());

		List<Long> materiasId = new ArrayList<>();

		cursoDto.getMaterias().forEach(materia -> {
			materiasId.add(materia.getId());
		});

		cursoModel.setMaterias(materiasId);
		return cursoModel;
	}

}
