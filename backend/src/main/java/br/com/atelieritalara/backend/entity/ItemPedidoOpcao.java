package br.com.atelieritalara.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido_opcao")
@Getter
@Setter
@NoArgsConstructor
public class ItemPedidoOpcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_item_id", nullable = false)
    private PedidoItem pedidoItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_opcao_id", nullable = false)
    private ProdutoOpcao produtoOpcao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_opcao_valor_id", nullable = false)
    private ProdutoOpcaoValor produtoOpcaoValor;

    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(name = "valor_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAdicional = BigDecimal.ZERO;
}