package br.com.atelieritalara.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "produto_opcao",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_produto_opcao_nome",
                        columnNames = {"produto_id", "nome"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProdutoOpcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false)
    private Boolean obrigatorio = false;

    @Column(nullable = false)
    private Integer ordem = 0;
}