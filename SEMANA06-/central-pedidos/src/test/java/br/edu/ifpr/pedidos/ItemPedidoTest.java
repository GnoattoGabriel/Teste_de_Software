package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void totalCentavosMultiplicaPrecoPorQuantidade() {
        assertEquals(3_000L, new ItemPedido("A", 1_000, 3, 5, 100, false).totalCentavos());
        assertEquals(0L, new ItemPedido("A", 1_000, 0, 5, 100, false).totalCentavos());
    }

    @Test
    void disponivelComparaQuantidadeComEstoque() {
        assertTrue(new ItemPedido("A", 1_000, 2, 2, 100, false).disponivel());
        assertFalse(new ItemPedido("A", 1_000, 3, 2, 100, false).disponivel());
    }

    @Test
    void rejeitaSkuNuloOuBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 1_000, 1, 5, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("  ", 1_000, 1, 5, 100, false));
    }

    @Test
    void rejeitaPrecoForaDoDominio() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 0, 1, 5, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000_001, 1, 5, 100, false));
    }

    @Test
    void rejeitaQuantidadeForaDoDominio() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000, -1, 5, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000, 101, 5, 100, false));
    }

    @Test
    void rejeitaEstoqueNegativoEPesoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000, 1, -1, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000, 1, 5, 0, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("A", 1_000, 1, 5, 100_001, false));
    }
}
