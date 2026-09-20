package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private ItemPedido item(String sku, long preco, int qtd, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, preco, qtd, estoque, peso, fragil);
    }

    @Test
    void subtotalIgnoraLinhaInativa() {
        Pedido p = new Pedido(List.of(
                item("A", 1_000, 0, 5, 100, false),
                item("B", 2_000, 2, 5, 100, false)), "PR", false, null);
        assertEquals(4_000L, p.subtotalCentavos());
    }

    @Test
    void subtotalListaVaziaEhZero() {
        assertEquals(0L, new Pedido(List.of(), "PR", false, null).subtotalCentavos());
    }

    @Test
    void pesoSomaQuantidade() {
        Pedido p = new Pedido(List.of(
                item("A", 1_000, 2, 5, 300, false),
                item("B", 1_000, 1, 5, 100, false)), "PR", false, null);
        assertEquals(700, p.pesoGramas());
    }

    @Test
    void temFragilIgnoraInativo() {
        Pedido soInativoFragil = new Pedido(
                List.of(item("A", 1_000, 0, 5, 100, true)), "PR", false, null);
        assertFalse(soInativoFragil.temFragil());

        Pedido comFragilNoFim = new Pedido(List.of(
                item("A", 1_000, 1, 5, 100, false),
                item("B", 1_000, 1, 5, 100, true)), "PR", false, null);
        assertTrue(comFragilNoFim.temFragil());

        Pedido semFragil = new Pedido(
                List.of(item("A", 1_000, 1, 5, 100, false)), "PR", false, null);
        assertFalse(semFragil.temFragil());
    }

    @Test
    void estoqueSuficienteComBreakNoInicioENoFim() {
        Pedido faltaNoInicio = new Pedido(List.of(
                item("A", 1_000, 5, 1, 100, false),
                item("B", 1_000, 1, 5, 100, false)), "PR", false, null);
        assertFalse(faltaNoInicio.estoqueSuficiente());

        Pedido faltaNoFim = new Pedido(List.of(
                item("A", 1_000, 1, 5, 100, false),
                item("B", 1_000, 5, 1, 100, false)), "PR", false, null);
        assertFalse(faltaNoFim.estoqueSuficiente());

        Pedido ok = new Pedido(List.of(
                item("A", 1_000, 1, 5, 100, false),
                item("B", 1_000, 2, 2, 100, false)), "PR", false, null);
        assertTrue(ok.estoqueSuficiente());
    }

    @Test
    void copiaDefensivaDaLista() {
        List<ItemPedido> mutavel = new ArrayList<>();
        mutavel.add(item("A", 1_000, 1, 5, 100, false));
        Pedido p = new Pedido(mutavel, "PR", false, null);
        mutavel.add(item("B", 9_999, 1, 5, 100, false));
        assertEquals(1_000L, p.subtotalCentavos());
    }

    @Test
    void construtorRejeitaListaNulaOuGrandeDemais() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(null, "PR", false, null));
        List<ItemPedido> grande = new ArrayList<>();
        for (int i = 0; i < 101; i++) grande.add(item("SKU-" + i, 1_000, 1, 5, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(grande, "PR", false, null));
    }

    @Test
    void construtorRejeitaUFInvalida() {
        ItemPedido it = item("A", 1_000, 1, 5, 100, false);
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(it), null, false, null));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(it), "pr", false, null));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(it), "PRR", false, null));
    }
}
