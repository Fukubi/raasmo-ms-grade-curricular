package com.rasmoo.client.escola.gradecurricular.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;

@Repository
public interface IMateriaRepository extends JpaRepository<MateriaEntity, Long> {

	@Query("select m from MateriaEntity m where m.horas >= :horaMinima")
	public List<MateriaEntity> findByHoraMinima(@Param("horaMinima")int horaMinima);
	
	@Query("select m from MateriaEntity m where m.frequencia = :frequencia")
	public List<MateriaEntity> findByFrequencia(@Param("frequencia") int frequencia);
	
	@Query("select m from MateriaEntity m where m.codigo = :codigo")
	public MateriaEntity findByCodigo(@Param("codigo")String codigo);
}
