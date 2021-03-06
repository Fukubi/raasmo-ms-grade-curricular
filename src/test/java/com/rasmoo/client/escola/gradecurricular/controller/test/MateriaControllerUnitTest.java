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

import com.rasmoo.client.escola.gradecurricular.v1.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.v1.model.Response;
import com.rasmoo.client.escola.gradecurricular.v1.service.IMateriaService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class MateriaControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private IMateriaService materiaService;

	@Autowired
	private TestRestTemplate restTemplate;

	private static MateriaDto materiaDto;

	@BeforeAll
	public static void init() {
		materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");
	}

	@Test
	void testListarMaterias() {
		Mockito.when(this.materiaService.listar()).thenReturn(new ArrayList<MateriaDto>());

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular")
				.exchange("http://localhost:" + this.port + "/v1/materia/", HttpMethod.GET, null,
						new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
						});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testConsultarMateria() {
		Mockito.when(this.materiaService.consultar(1L)).thenReturn(materiaDto);

		ResponseEntity<Response<MateriaDto>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular")
				.exchange("http://localhost:" + this.port + "/v1/materia/1", HttpMethod.GET, null,
						new ParameterizedTypeReference<Response<MateriaDto>>() {
						});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testCadastrarMaterias() {
		Mockito.when(this.materiaService.cadastrar(materiaDto)).thenReturn(true);

		HttpEntity<MateriaDto> request = new HttpEntity<>(materiaDto);

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/materia/", HttpMethod.POST, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(201, materias.getBody().getStatusCode());
	}

	@Test
	void testAtualizarMaterias() {
		Mockito.when(this.materiaService.atualizar(materiaDto)).thenReturn(Boolean.TRUE);

		HttpEntity<MateriaDto> request = new HttpEntity<>(materiaDto);

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/materia/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testExcluirMaterias() {
		Mockito.when(this.materiaService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/materia/1", HttpMethod.DELETE, null,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testConsultarMateriasPorHoraMinima() {
		Mockito.when(this.materiaService.listarPorHorarioMinimo(64)).thenReturn(new ArrayList<MateriaDto>());

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular")
				.exchange("http://localhost:" + this.port + "/v1/materia/horario-minimo/64", HttpMethod.GET, null,
						new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
						});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testConsultarMateriasPorFrequencia() {
		Mockito.when(this.materiaService.listarPorFequencia(1)).thenReturn(new ArrayList<MateriaDto>());

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth("rasmoo", "msgradecurricular")
				.exchange("http://localhost:" + this.port + "/v1/materia/frequencia/1", HttpMethod.GET, null,
						new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
						});

		assertNotNull(materias.getBody().getData());
		assertEquals(200, materias.getBody().getStatusCode());
	}

}
