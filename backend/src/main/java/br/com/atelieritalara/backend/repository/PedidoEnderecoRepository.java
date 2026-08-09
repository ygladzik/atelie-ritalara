package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.PedidoEndereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoEnderecoRepository extends JpaRepository<PedidoEndereco, Long> {
}