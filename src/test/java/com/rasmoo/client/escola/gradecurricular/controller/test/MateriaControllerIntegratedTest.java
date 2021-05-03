package com.rasmoo.client.escola.gradecurricular.controller.test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import com.rasmoo.client.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.client.escola.gradecurricular.service.IMateriaService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class MateriaControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private IMateriaRepository materiaRepository;

	@BeforeEach
	public void init() {
		this.montaBaseDeDados();
	}
	
	@AfterEach
	public void finish() {
		this.materiaRepository.deleteAll();
	}
	
	private void montaBaseDeDados() {
		MateriaEntity m1 = new MateriaEntity();
		m1.setCodigo("ILP");
		m1.setFrequencia(2);
		m1.setHoras(64);
		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");
		
		MateriaEntity m2 = new MateriaEntity();
		m2.setCodigo("POO");
		m2.setFrequencia(2);
		m2.setHoras(84);
		m2.setNome("PROGRAMACAO ORIENTADA A OBJETOS");
		
		MateriaEntity m3 = new MateriaEntity();
		m3.setCodigo("APA");
		m3.setFrequencia(1);
		m3.setHoras(102);
		m3.setNome("ANALISE E PROJETOS DE ALGORITMOS");
		
		this.materiaRepository.saveAll(Arrays.asList(m1, m2, m3));
	}

	@Test
	void testListarMaterias() {
		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(3, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarMateriasPorHoraMinima() {
		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/horario-minimo/80", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(2, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	void testConsultarMateriasPorFrequencia() {
		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/frequencia/1", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});

		assertNotNull(materias.getBody().getData());
		assertEquals(1, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

}
