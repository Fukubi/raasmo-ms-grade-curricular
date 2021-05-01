package com.rasmoo.client.escola.gradecurricular.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.exception.MateriaException;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;

@Service
public class MateriaService implements IMateriaService {

	@Autowired
	private IMateriaRepository materiaRepository;

	@Override
	public Boolean atualizar(MateriaEntity materia) {
		try {
			MateriaEntity materiaConsultada = this.consultar(materia.getId());
			MateriaEntity materiaEntityAtualizada = materiaConsultada;

			materiaEntityAtualizada.setNome(materiaConsultada.getNome());
			materiaEntityAtualizada.setCodigo(materiaConsultada.getCodigo());
			materiaEntityAtualizada.setHoras(materiaConsultada.getHoras());
			materiaEntityAtualizada.setNome(materiaConsultada.getNome());
			materiaEntityAtualizada.setFrequencia(materiaConsultada.getFrequencia());

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
	public List<MateriaEntity> listar() {
		try {
			return this.materiaRepository.findAll();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public MateriaEntity consultar(final Long id) {
		try {
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {
				return materiaOptional.get();
			}
			throw new MateriaException("Matéria não encontrada", HttpStatus.NOT_FOUND);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException("Erro interno identificado, Contate o suporte",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(final MateriaEntity materia) {
		try {
			this.materiaRepository.save(materia);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
