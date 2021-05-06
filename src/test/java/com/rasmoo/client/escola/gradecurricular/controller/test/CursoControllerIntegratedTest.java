package com.rasmoo.client.escola.gradecurricular.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.model.CursoModel;
import com.rasmoo.client.escola.gradecurricular.model.Response;
import com.rasmoo.client.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CursoControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ICursoRepository cursoRepository;
	
	@Autowired
	private IMateriaRepository materiaRepository;
	@BeforeEach
	public void init() {
		this.montaBaseDeDados();
	}

	@AfterEach
	public void finish() {
		this.cursoRepository.deleteAll();
		this.materiaRepository.deleteAll();
	}

	private void montaBaseDeDados() {
		MateriaEntity m1 = new MateriaEntity();
		m1.setCodigo("ILP");
		m1.setFrequencia(1);
		m1.setHoras(64);
		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

		MateriaEntity m2 = new MateriaEntity();
		m2.setCodigo("IAC");
		m2.setFrequencia(2);
		m2.setHoras(80);
		m2.setNome("INTRODUCAO A ALGORITMOS COMPUTACIONAIS");

		MateriaEntity m3 = new MateriaEntity();
		m3.setCodigo("POO");
		m3.setFrequencia(1);
		m3.setHoras(90);
		m3.setNome("PROGRAMACAO ORIENTADA A OBJETOS");

		this.materiaRepository.saveAll(List.of(m1, m2, m3));
		List<MateriaEntity> materias = this.materiaRepository.findAll();

		CursoEntity c1 = new CursoEntity();
		c1.setNome("PROGRAMACAO BASICA");
		c1.setCodigo("PB001");
		c1.setMaterias(materias);

		CursoEntity c2 = new CursoEntity();
		c2.setNome("ROBOTICA BASICA");
		c2.setCodigo("RB001");
		c2.setMaterias(materias);

		CursoEntity c3 = new CursoEntity();
		c3.setNome("SEGURANCA BASICA");
		c3.setCodigo("SB001");
		c3.setMaterias(materias);

		this.cursoRepository.saveAll(List.of(c1, c2, c3));
	}

	@Test
	void testListarCursos() {
		ResponseEntity<Response<List<CursoDto>>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso/", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<CursoDto>>>() {
				});
		
		assertNotNull(cursos.getBody().getData());
		assertEquals(3, cursos.getBody().getData().size());
		assertEquals(200, cursos.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarCursosPorCodigo() {
		ResponseEntity<Response<CursoDto>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso/codigo/PB001", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<CursoDto>>() {
				});
		
		assertNotNull(cursos.getBody().getData());
		assertEquals("PB001", cursos.getBody().getData().getCodigo());
		assertEquals(200, cursos.getBody().getStatusCode());
	}
	
	@Test
	void testAtualizarCurso() {
		List<CursoEntity> cursoList = this.cursoRepository.findAll();
		CursoEntity curso = cursoList.get(0);
		
		curso.setNome("TESTE ATUALIZA CURSO");
		
		HttpEntity<CursoModel> request = new HttpEntity<>(this.converterCursoEntityParaModel(curso));
		
		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso", HttpMethod.PUT, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		
		CursoEntity cursoAtualizado = this.cursoRepository.findById(curso.getId()).get();
		
		assertTrue(cursos.getBody().getData());
		assertEquals("TESTE ATUALIZA CURSO", cursoAtualizado.getNome());
		assertEquals(200, cursos.getBody().getStatusCode());
	}
	
	@Test
	void testCadastrarCurso() {
		CursoModel curso = new CursoModel();
		curso.setCodigo("CC001");
		curso.setNome("CURSO CRIADO");
		curso.setMaterias(new ArrayList<>());
		List<MateriaEntity> materias = this.materiaRepository.findAll();
		materias.forEach(materia -> {
			curso.getMaterias().add(materia.getId());
		});
		
		HttpEntity<CursoModel> request = new HttpEntity<>(curso);
		
		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso", HttpMethod.POST, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		
		List<CursoEntity> cursoList = this.cursoRepository.findAll();
		
		assertTrue(cursos.getBody().getData());
		assertEquals(4, cursoList.size());
		assertEquals(201, cursos.getBody().getStatusCode());
	}
	
	@Test
	void testExcluirCursoPorId() {
		CursoEntity curso = this.cursoRepository.findAll().get(0);
		
		ResponseEntity<Response<Boolean>> cursos = restTemplate.exchange(
				"http://localhost:" + this.port + "/curso/" + curso.getId(), HttpMethod.DELETE, null,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		
		List<CursoEntity> cursoList = this.cursoRepository.findAll();
		
		assertTrue(cursos.getBody().getData());
		assertEquals(2, cursoList.size());
		assertEquals(200, cursos.getBody().getStatusCode());
	}
	
	private CursoModel converterCursoEntityParaModel(CursoEntity cursoEntity) {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setCodigo(cursoEntity.getCodigo());
		cursoModel.setNome(cursoEntity.getNome());
		cursoModel.setId(cursoEntity.getId());

		List<Long> materiasId = new ArrayList<>();

		cursoEntity.getMaterias().forEach(materia -> {
			materiasId.add(materia.getId());
		});

		cursoModel.setMaterias(materiasId);
		return cursoModel;
	}
}
