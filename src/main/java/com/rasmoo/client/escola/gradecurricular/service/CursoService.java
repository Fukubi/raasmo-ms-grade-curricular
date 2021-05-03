package com.rasmoo.client.escola.gradecurricular.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.client.escola.gradecurricular.controller.CursoController;
import com.rasmoo.client.escola.gradecurricular.dto.CursoDto;
import com.rasmoo.client.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.exception.CursoException;
import com.rasmoo.client.escola.gradecurricular.model.CursoModel;
import com.rasmoo.client.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;

@CacheConfig(cacheNames = "curso")
@Service
public class CursoService implements ICursoService {

	private static final String MENSAGEM_ERRO = "Erro interno identificado, contate o suporte";
	private static final String CURSO_NAO_ENCONTRADO = "Curso não encontrado";

	private ICursoRepository cursoRepository;
	private IMateriaRepository materiaRepository;
	private ModelMapper mapper;

	@Autowired
	public CursoService(ICursoRepository cursoRepository, IMateriaRepository materiaRepository) {
		this.mapper = new ModelMapper();
		this.cursoRepository = cursoRepository;
		this.materiaRepository = materiaRepository;
	}

	@Override
	public Boolean cadastrar(CursoModel curso) {
		try {
			CursoDto cursoDto = this.mapper.map(curso, CursoDto.class);
			
			curso.getMaterias().forEach(materiaId -> {
				Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(materiaId);
				if (materiaOptional.isEmpty()) {
					throw new CursoException("Matéria não encontrada", HttpStatus.NOT_FOUND);
				}
				cursoDto.getMaterias().add(materiaOptional.get());
			});
			
			CursoEntity cursoEntity = this.mapper.map(cursoDto, CursoEntity.class);
			this.cursoRepository.save(cursoEntity);
			return Boolean.TRUE;
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<CursoDto> listar() {
		try {
			List<CursoDto> cursoDto = this.mapper.map(this.cursoRepository.findAll(), new TypeToken<List<CursoDto>>() {
			}.getType());

			cursoDto.forEach(curso -> curso.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).consultar(curso.getId())).withSelfRel()));

			return cursoDto;
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#codigo")
	@Override
	public List<CursoDto> consultarPorCodigo(String codigo) {
		try {
			Optional<List<CursoEntity>> cursoOptional = this.cursoRepository.findByCodigo(codigo);
			if (cursoOptional.isPresent()) {
				List<CursoDto> cursoDto = this.mapper.map(cursoOptional.get(), new TypeToken<List<CursoDto>>() {
				}.getType());

				cursoDto.forEach(curso -> curso.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(CursoController.class).consultar(curso.getId()))
						.withSelfRel()));

				return cursoDto;
			}
			throw new CursoException(CURSO_NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(CursoModel curso) {
		try {
			CursoDto cursoDto = new CursoDto();
			
			curso.getMaterias().forEach(materiaId -> {
				Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(materiaId);
				if (materiaOptional.isEmpty()) {
					throw new CursoException("Matéria não encontrada", HttpStatus.NOT_FOUND);
				}
				cursoDto.getMaterias().add(materiaOptional.get());
			});
			
			this.consultar(cursoDto.getId());
			CursoEntity cursoEntityAtualizado = this.mapper.map(cursoDto, CursoEntity.class);

			this.cursoRepository.save(cursoEntityAtualizado);

			return Boolean.TRUE;
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public CursoDto consultar(Long id) {
		try {
			Optional<CursoEntity> cursoOptional = this.cursoRepository.findById(id);
			if (cursoOptional.isPresent()) {
				return this.mapper.map(cursoOptional.get(), CursoDto.class);
			}
			throw new CursoException(CURSO_NAO_ENCONTRADO, HttpStatus.NOT_FOUND);
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.cursoRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
