package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}