package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}