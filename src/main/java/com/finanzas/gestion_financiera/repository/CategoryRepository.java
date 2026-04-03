package com.finanzas.gestion_financiera.repository;


import com.finanzas.gestion_financiera.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUsuarioId(Long usuarioId);

    Optional<Category> findByIdAndUsuarioId(Long id, Long usuarioId);
}