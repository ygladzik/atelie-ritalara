package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}