package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}