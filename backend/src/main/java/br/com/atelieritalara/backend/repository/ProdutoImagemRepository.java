package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.ProdutoImagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoImagemRepository extends JpaRepository<ProdutoImagem, Long> {
}