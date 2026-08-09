package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}