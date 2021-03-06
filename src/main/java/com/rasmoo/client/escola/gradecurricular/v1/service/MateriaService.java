package com.rasmoo.client.escola.gradecurricular.v1.service;

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

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.client.escola.gradecurricular.v1.controller.MateriaController;
import com.rasmoo.client.escola.gradecurricular.v1.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.v1.exception.MateriaException;

@CacheConfig(cacheNames = "materia")
@Service
public class MateriaService implements IMateriaService {

	private static final String MENSAGEM_ERRO = "Erro interno identificado, Contate o suporte";
	private static final String MATERIA_NAO_ENCONTRADA = "Matéria não encontrada";

	private IMateriaRepository materiaRepository;
	private ModelMapper mapper;

	@Autowired
	public MateriaService(IMateriaRepository materiaRepository) {
		this.mapper = new ModelMapper();
		this.materiaRepository = materiaRepository;
	}

	@Override
	public Boolean atualizar(MateriaDto materia) {
		try {
			this.consultar(materia.getId());
			
			return this.cadastrarOuAtualizar(materia);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.materiaRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<MateriaDto> listar() {
		try {
			List<MateriaDto> materiaDto = this.mapper.map(this.materiaRepository.findAll(),
					new TypeToken<List<MateriaDto>>() {
					}.getType());

			materiaDto.forEach(materia -> materia.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(materia.getId()))
					.withSelfRel()));

			return materiaDto;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public MateriaDto consultar(final Long id) {
		try {
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {
				return this.mapper.map(materiaOptional.get(), MateriaDto.class);
			}
			throw new MateriaException(MATERIA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(final MateriaDto materia) {
		try {
			if (materia.getId() != null) {
				throw new MateriaException("ID não foi informado", HttpStatus.BAD_REQUEST);
			}

			if (this.materiaRepository.findByCodigo(materia.getCodigo()) != null) {
				throw new MateriaException("Esse código já está cadastrado em uma matéria", HttpStatus.BAD_REQUEST);
			}
			
			return this.cadastrarOuAtualizar(materia);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Boolean cadastrarOuAtualizar(final MateriaDto materia) {
		MateriaEntity materiaEntity = this.mapper.map(materia, MateriaEntity.class);
		this.materiaRepository.save(materiaEntity);
		return Boolean.TRUE;
	}

	@Override
	public List<MateriaDto> listarPorHorarioMinimo(final int horaMinima) {
		try {
			List<MateriaDto> materiaDto = this.mapper.map(this.materiaRepository.findByHoraMinima(horaMinima),
					new TypeToken<List<MateriaDto>>() {
					}.getType());

			materiaDto.forEach(materia -> materia.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(materia.getId()))
					.withSelfRel()));

			return materiaDto;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<MateriaDto> listarPorFequencia(int frequencia) {
		try {
			List<MateriaDto> materiaDto = this.mapper.map(this.materiaRepository.findByFrequencia(frequencia),
					new TypeToken<List<MateriaDto>>() {
					}.getType());

			materiaDto.forEach(materia -> materia.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(materia.getId()))
					.withSelfRel()));

			return materiaDto;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
