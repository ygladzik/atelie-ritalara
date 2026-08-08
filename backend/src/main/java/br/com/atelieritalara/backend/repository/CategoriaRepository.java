package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}