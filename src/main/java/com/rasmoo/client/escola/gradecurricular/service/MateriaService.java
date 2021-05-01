package com.rasmoo.client.escola.gradecurricular.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.client.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.exception.MateriaException;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;

@Service
public class MateriaService implements IMateriaService {

	@Autowired
	private IMateriaRepository materiaRepository;

	@Override
	public Boolean atualizar(MateriaDto materia) {
		try {
			ModelMapper mapper = new ModelMapper();
			MateriaDto materiaConsultada = this.consultar(materia.getId());
			MateriaEntity materiaEntityAtualizada = mapper.map(materiaConsultada, MateriaEntity.class);

			this.materiaRepository.save(materiaEntityAtualizada);

			return true;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.materiaRepository.deleteById(id);
			return true;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MateriaDto> listar() {
		try {
			ModelMapper mapper = new ModelMapper();
			return mapper.map(this.materiaRepository.findAll(), new TypeToken<List<MateriaDto>>() {}.getType());
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public MateriaDto consultar(final Long id) {
		try {
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {
				ModelMapper mapper = new ModelMapper();
				return mapper.map(materiaOptional, MateriaDto.class);
			}
			throw new MateriaException("Matéria não encontrada", HttpStatus.NOT_FOUND);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException("Erro interno identificado, Contate o suporte", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(final MateriaDto materia) {
		try {
			ModelMapper mapper = new ModelMapper();
			MateriaEntity materiaEntity = mapper.map(materia, MateriaEntity.class);
			this.materiaRepository.save(materiaEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
