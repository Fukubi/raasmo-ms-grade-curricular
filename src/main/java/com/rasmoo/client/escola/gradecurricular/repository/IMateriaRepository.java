package com.rasmoo.client.escola.gradecurricular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rasmoo.client.escola.gradecurricular.entity.MateriaEntity;

@Repository
public interface IMateriaRepository extends JpaRepository<MateriaEntity, Long> {

}
