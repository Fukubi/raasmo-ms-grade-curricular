package com.rasmoo.client.escola.gradecurricular.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rasmoo.client.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.client.escola.gradecurricular.service.MateriaService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MateriaServiceUnitTest {
	
	@Mock
	private IMateriaRepository materiaRepository;
	
	@InjectMocks
	private MateriaService materiaService;
	
	private static MateriaEntity materiaEntity;
	
	@BeforeAll
	public static void init() {
		materiaEntity = new MateriaEntity();
		materiaEntity.setId(1L);
		materiaEntity.setCodigo("ILP");
		materiaEntity.setFrequencia(1);
		materiaEntity.setHoras(64);
		materiaEntity.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");
	}
	
	@Test
	void testListarSucesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		
		Mockito.when(this.materiaRepository.findAll()).thenReturn(listMateria);
		
		List<MateriaDto> listMateriaDto = this.materiaService.listar();
		
		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1L, listMateriaDto.get(0).getId());
		assertEquals("/materia/1", listMateriaDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listMateriaDto.size());
		
		Mockito.verify(this.materiaRepository, times(1)).findAll();
	}
	
	@Test
	void testListarHorarioMinimoSucesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		
		Mockito.when(this.materiaRepository.findByHoraMinima(64)).thenReturn(listMateria);
		
		List<MateriaDto> listMateriaDto = this.materiaService.listarPorHorarioMinimo(64);
		
		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1L, listMateriaDto.get(0).getId());
		assertEquals("/materia/1", listMateriaDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listMateriaDto.size());
		
		Mockito.verify(this.materiaRepository, times(1)).findByHoraMinima(64);
	}
	
	@Test
	void testListarPorFrequenciaSucesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		
		Mockito.when(this.materiaRepository.findByFrequencia(1)).thenReturn(listMateria);
		
		List<MateriaDto> listMateriaDto = this.materiaService.listarPorFequencia(1);
		
		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1L, listMateriaDto.get(0).getId());
		assertEquals(1, listMateriaDto.size());
		
		Mockito.verify(this.materiaRepository, times(1)).findByFrequencia(1);
	}
}
