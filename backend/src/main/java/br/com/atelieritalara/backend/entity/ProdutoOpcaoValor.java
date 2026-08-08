package br.com.atelieritalara.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "produto_opcao_valor",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_opcao_valor_nome",
                        columnNames = {"produto_opcao_id", "nome"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProdutoOpcaoValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_opcao_id", nullable = false)
    private ProdutoOpcao produtoOpcao;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "valor_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAdicional = BigDecimal.ZERO;

    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Integer ordem = 0;
}