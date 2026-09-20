package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    private final CalculadoraFrete frete = new CalculadoraFrete();

    private Pedido pedido(String uf, boolean expresso, boolean fragil, int pesoUnitario) {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, pesoUnitario, fragil);
        return new Pedido(List.of(item), uf, expresso, null);
    }

    private Cliente comum() {
        return new Cliente(false, false, 1);
    }

    @Test
    void baseParana() {
        assertEquals(1_200L, frete.calcular(pedido("PR", false, false, 1_000), comum(), 10_000L));
    }

    @Test
    void baseSaoPauloERio() {
        assertEquals(2_000L, frete.calcular(pedido("SP", false, false, 1_000), comum(), 10_000L));
        assertEquals(2_000L, frete.calcular(pedido("RJ", false, false, 1_000), comum(), 10_000L));
    }

    @Test
    void baseDefaultParaOutraUF() {
        assertEquals(3_000L, frete.calcular(pedido("MG", false, false, 1_000), comum(), 10_000L));
    }

    @Test
    void pesoExato2000SemAdicional() {
        // peso total 2_000 -> excedente 0, zero iteracoes
        assertEquals(1_200L, frete.calcular(pedido("PR", false, false, 2_000), comum(), 10_000L));
    }

    @Test
    void pesoFracaoUmaIteracao() {
        // peso 2_001 -> excedente 1 -> 1 iteracao (+300)
        assertEquals(1_500L, frete.calcular(pedido("PR", false, false, 2_001), comum(), 10_000L));
    }

    @Test
    void pesoExato3000UmaIteracao() {
        assertEquals(1_500L, frete.calcular(pedido("PR", false, false, 3_000), comum(), 10_000L));
    }

    @Test
    void peso3001DuasIteracoes() {
        // excedente 1001 -> +300, excedente 1 -> +300
        assertEquals(1_800L, frete.calcular(pedido("PR", false, false, 3_001), comum(), 10_000L));
    }

    @Test
    void peso5000TresIteracoes() {
        // excedente 3000 -> 3 iteracoes (+900)
        assertEquals(2_100L, frete.calcular(pedido("PR", false, false, 5_000), comum(), 10_000L));
    }

    @Test
    void gratuidadeComLiquidoNaFronteira() {
        // liquido 30_000 + entrega normal zera base e peso
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, 5_000, false);
        Pedido p = new Pedido(List.of(item), "PR", false, null);
        assertEquals(0L, frete.calcular(p, comum(), 30_000L));
        assertEquals(2_100L, frete.calcular(p, comum(), 29_999L));
    }

    @Test
    void expressoImpedeGratuidade() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, 1_000, false);
        Pedido p = new Pedido(List.of(item), "PR", true, null);
        // base 1_200 mantida + 1_500 do expresso
        assertEquals(2_700L, frete.calcular(p, comum(), 50_000L));
    }

    @Test
    void vipPagaMetade() {
        Cliente vip = new Cliente(true, false, 5);
        assertEquals(600L, frete.calcular(pedido("PR", false, false, 1_000), vip, 10_000L));
    }

    @Test
    void vipComGratuidadeZeraAntesDaMetade() {
        Cliente vip = new Cliente(true, false, 5);
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, 1_000, false);
        Pedido p = new Pedido(List.of(item), "PR", false, null);
        assertEquals(0L, frete.calcular(p, vip, 50_000L));
    }

    @Test
    void fragilAdicionaUmaVezMesmoComBaseZerada() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, 1_000, true);
        Pedido p = new Pedido(List.of(item), "PR", false, null);
        // 0 (gratuidade) + 500
        assertEquals(500L, frete.calcular(p, comum(), 50_000L));
    }

    @Test
    void fragilCobraUmaUnicaVezComDoisItensFrageis() {
        ItemPedido a = new ItemPedido("A", 1_000, 1, 5, 500, true);
        ItemPedido b = new ItemPedido("B", 1_000, 1, 5, 500, true);
        Pedido p = new Pedido(List.of(a, b), "PR", false, null);
        assertEquals(1_700L, frete.calcular(p, comum(), 10_000L));
    }

    @Test
    void expressoMaisFragilAcumulam() {
        ItemPedido item = new ItemPedido("SKU-1", 1_000, 1, 5, 1_000, true);
        Pedido p = new Pedido(List.of(item), "PR", true, null);
        assertEquals(3_200L, frete.calcular(p, comum(), 10_000L));
    }

    @Test
    void liquidoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> frete.calcular(pedido("PR", false, false, 1_000), comum(), -1L));
    }
}
