package br.com.atelieritalara.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "produto_variacao_adicional",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_variacao_adicional",
                        columnNames = {
                                "produto_variacao_id",
                                "produto_opcao_valor_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProdutoVariacaoAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_variacao_id", nullable = false)
    private ProdutoVariacao produtoVariacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_opcao_valor_id", nullable = false)
    private ProdutoOpcaoValor produtoOpcaoValor;

    @Column(name = "valor_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAdicional = BigDecimal.ZERO;
}