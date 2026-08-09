package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}