package com.rasmoo.client.escola.gradecurricular.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rasmoo.client.escola.gradecurricular.entity.CursoEntity;

public interface ICursoRepository extends JpaRepository<CursoEntity, Long> {

	@Query("select c from CursoEntity c where c.codigo = :codigo")
	public Optional<List<CursoEntity>> findByCodigo(@Param("codigo")String codigo);
	
}
