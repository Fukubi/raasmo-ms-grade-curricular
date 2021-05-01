package com.rasmoo.client.escola.gradecurricular.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.client.escola.gradecurricular.repository.IMateriaRepository;

@RestController
@RequestMapping("/materia")
public class MateriaController {
	
	@Autowired
	private IMateriaRepository materiaRepository;

	@GetMapping
	public ResponseEntity<List<MateriaEntity>> listarMaterias() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(this.materiaRepository.findAll());	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MateriaEntity> consultaMateria(@PathVariable Long id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(this.materiaRepository.findById(id).get());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}
	
	@PostMapping
	public ResponseEntity<Boolean> cadastrarMaterias(@RequestBody MateriaEntity materia) {
		try {
			this.materiaRepository.save(materia);
			return ResponseEntity.status(HttpStatus.OK).body(true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
	}
	
	@PutMapping
	public ResponseEntity<Boolean> atualizarMateria(@RequestBody MateriaEntity materia) {
		try {
			//buscamos pela materia que gostaríamos de atualizar
			MateriaEntity materiaEntityAtualizada = this.materiaRepository.findById(materia.getId()).get();
			
			//atualizamos todos os valores
			materiaEntityAtualizada.setNome(materia.getNome());
			materiaEntityAtualizada.setCodigo(materia.getCodigo());
			materiaEntityAtualizada.setHoras(materia.getHoras());
			materiaEntityAtualizada.setNome(materia.getNome());
			materiaEntityAtualizada.setFrequencia(materia.getFrequencia());
			
			//salvamos as alteracoes
			this.materiaRepository.save(materiaEntityAtualizada);
			
			return ResponseEntity.status(HttpStatus.OK).body(true);
			
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deletarMateria(@PathVariable Long id) {
		try {
			this.materiaRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}
	}
}
