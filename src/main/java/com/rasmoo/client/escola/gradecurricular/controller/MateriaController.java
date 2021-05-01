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
import com.rasmoo.client.escola.gradecurricular.service.IMateriaService;

@RestController
@RequestMapping("/materia")
public class MateriaController {
	
	@Autowired
	private IMateriaService materiaService;

	@GetMapping
	public ResponseEntity<List<MateriaEntity>> listarMaterias() {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.listar());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MateriaEntity> consultaMateria(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.consultar(id));
	}
	
	@PostMapping
	public ResponseEntity<Boolean> cadastrarMaterias(@RequestBody MateriaEntity materia) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.cadastrar(materia));
	}
	
	@PutMapping
	public ResponseEntity<Boolean> atualizarMateria(@RequestBody MateriaEntity materia) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.atualizar(materia));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deletarMateria(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.excluir(id));
	}
}
