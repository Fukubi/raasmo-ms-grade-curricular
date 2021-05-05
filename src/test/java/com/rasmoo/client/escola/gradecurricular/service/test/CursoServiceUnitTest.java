package com.rasmoo.client.escola.gradecurricular.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.exception.CursoException;
import com.rasmoo.client.escola.gradecurricular.model.CursoModel;
import com.rasmoo.client.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.client.escola.gradecurricular.service.CursoService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CursoServiceUnitTest {

	@Mock
	private ICursoRepository cursoRepository;

	@Mock
	private IMateriaRepository materiaRepository;

	@InjectMocks
	private CursoService cursoService;

	private static CursoEntity cursoEntity;

	@BeforeAll
	public static void init() {
		cursoEntity = new CursoEntity();
		cursoEntity.setId(1L);
		cursoEntity.setNome("PROGRAMACAO BASICA");
		cursoEntity.setCodigo("PB001");

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

		cursoEntity.setMaterias(List.of(m1, m2, m3));
	}

	@Test
	void testListarSucesso() {
		List<CursoEntity> listCurso = new ArrayList<>();
		listCurso.add(cursoEntity);

		Mockito.when(this.cursoRepository.findAll()).thenReturn(listCurso);

		List<CursoDto> listCursoDto = this.cursoService.listar();

		assertNotNull(listCursoDto);
		assertEquals(1L, listCursoDto.get(0).getId());
		assertEquals("PROGRAMACAO BASICA", listCursoDto.get(0).getNome());
		assertEquals("PB001", listCursoDto.get(0).getCodigo());
		assertEquals("/curso/id/1", listCursoDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listCursoDto.size());

		Mockito.verify(this.cursoRepository, times(1)).findAll();
	}

	@Test
	void testConsultarSucesso() {
		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
		CursoDto cursoDto = this.cursoService.consultar(1L);

		assertNotNull(cursoDto);
		assertEquals(1L, cursoDto.getId());
		assertEquals("PROGRAMACAO BASICA", cursoDto.getNome());
		assertEquals("PB001", cursoDto.getCodigo());

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
	}

	@Test
	void testCadastrarSucesso() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

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

		cursoEntity.setId(null);

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(m1));
		Mockito.when(this.materiaRepository.findById(2L)).thenReturn(Optional.of(m2));
		Mockito.when(this.materiaRepository.findById(3L)).thenReturn(Optional.of(m3));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenReturn(cursoEntity);

		Boolean sucesso = this.cursoService.cadastrar(cursoModel);

		assertTrue(sucesso);

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(2L);
		Mockito.verify(this.materiaRepository, times(1)).findById(3L);
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);

		cursoEntity.setId(1L);
	}

	@Test
	void testAtualizarSucesso() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

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

		cursoEntity.setId(null);

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(m1));
		Mockito.when(this.materiaRepository.findById(2L)).thenReturn(Optional.of(m2));
		Mockito.when(this.materiaRepository.findById(3L)).thenReturn(Optional.of(m3));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenReturn(cursoEntity);

		Boolean sucesso = this.cursoService.atualizar(cursoModel);

		assertTrue(sucesso);

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(2L);
		Mockito.verify(this.materiaRepository, times(1)).findById(3L);
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);

		cursoEntity.setId(1L);
	}

	@Test
	void testExcluirSucesso() {
		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
		Boolean sucesso = this.cursoService.excluir(1L);

		assertNotNull(sucesso);

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).deleteById(1L);
	}

	@Test
	void testAtualizarThrowCursoException() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.empty());

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.atualizar(cursoModel);
		});

		assertEquals(HttpStatus.NOT_FOUND, cursoException.getHttpStatus());
		assertEquals("Matéria não encontrada", cursoException.getMessage());

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(0)).findById(2L);
		Mockito.verify(this.materiaRepository, times(0)).findById(3L);
		Mockito.verify(this.cursoRepository, times(0)).save(cursoEntity);
	}

	@Test
	void testExcluirThrowCursoException() {
		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.empty());

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.excluir(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, cursoException.getHttpStatus());
		assertEquals("Curso não encontrado", cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(0)).deleteById(1L);
	}

	@Test
	void testCadastrarComCodigoExistenteThrowCursoException() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

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

		cursoEntity.setId(null);

		Mockito.when(this.cursoRepository.findByCodigo("PB001")).thenReturn(Optional.of(cursoEntity));

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.cadastrar(cursoModel);
		});

		assertEquals(HttpStatus.BAD_REQUEST, cursoException.getHttpStatus());
		assertEquals("Este código já foi cadastrado", cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findByCodigo("PB001");
		Mockito.verify(this.materiaRepository, times(0)).findById(1L);
		Mockito.verify(this.materiaRepository, times(0)).findById(2L);
		Mockito.verify(this.materiaRepository, times(0)).findById(3L);
		Mockito.verify(this.cursoRepository, times(0)).save(cursoEntity);

		cursoEntity.setId(1L);
	}

	@Test
	void testAtualizarThrowException() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

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

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(m1));
		Mockito.when(this.materiaRepository.findById(2L)).thenReturn(Optional.of(m2));
		Mockito.when(this.materiaRepository.findById(3L)).thenReturn(Optional.of(m3));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenThrow(IllegalStateException.class);

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.atualizar(cursoModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals("Erro interno identificado, contate o suporte", cursoException.getMessage());

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(2L);
		Mockito.verify(this.materiaRepository, times(1)).findById(3L);
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);
	}

	@Test
	void testCadastrarThrowException() {
		CursoModel cursoModel = new CursoModel();
		cursoModel.setNome("PROGRAMACAO BASICA");
		cursoModel.setCodigo("PB001");
		cursoModel.setMaterias(List.of(1L, 2L, 3L));

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

		cursoEntity.setId(null);

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(m1));
		Mockito.when(this.materiaRepository.findById(2L)).thenReturn(Optional.of(m2));
		Mockito.when(this.materiaRepository.findById(3L)).thenReturn(Optional.of(m3));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenThrow(IllegalStateException.class);

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.cadastrar(cursoModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals("Erro interno identificado, contate o suporte", cursoException.getMessage());

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(2L);
		Mockito.verify(this.materiaRepository, times(1)).findById(3L);
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);

		cursoEntity.setId(1L);
	}

	@Test
	void testConsultarThrowException() {
		Mockito.when(this.cursoRepository.findById(1L)).thenThrow(IllegalStateException.class);

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.consultar(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals("Erro interno identificado, contate o suporte", cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
	}
	
	@Test
	void testListarThrowException() {
		Mockito.when(this.cursoRepository.findAll()).thenThrow(IllegalStateException.class);

		CursoException cursoException;
		
		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.listar();
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals("Erro interno identificado, contate o suporte", cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findAll();
	}
	
	@Test
	void testExcluirThrowException() {
		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
		Mockito.doThrow(IllegalStateException.class).when(this.cursoRepository).deleteById(1L);
		
		CursoException cursoException;
		
		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.excluir(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals("Erro interno identificado, contate o suporte", cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).deleteById(1L);
	}
}
