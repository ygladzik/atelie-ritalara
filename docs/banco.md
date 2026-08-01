# Modelo de Domínio

## Entidades

- Usuario
- Cliente
- Categoria
- Produto
- ProdutoImagem
- ProdutoOpcao
- ProdutoOpcaoValor
- Pedido
- ItemPedido
- ItemPedidoOpcao
- EnderecoEntrega
- Pagamento

## Relacionamentos

Usuario (1) -> (N) Pedido

Cliente (1) -> (N) Pedido

Categoria (1) -> (N) Produto

Produto (1) -> (N) ProdutoImagem

Produto (1) -> (N) ProdutoOpcao

ProdutoOpcao (1) -> (N) ProdutoOpcaoValor

Pedido (1) -> (N) ItemPedido

Pedido (1) -> (N) Pagamento

Pedido (1) -> (0..1) EnderecoEntrega

Produto (1) -> (N) ItemPedido

ItemPedido (1) -> (N) ItemPedidoOpcao

ProdutoOpcao (1) -> (N) ItemPedidoOpcao

ProdutoOpcaoValor (1) -> (N) ItemPedidoOpcao