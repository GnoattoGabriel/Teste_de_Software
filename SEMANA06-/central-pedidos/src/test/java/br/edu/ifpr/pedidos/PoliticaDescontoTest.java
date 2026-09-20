package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    private final PoliticaDesconto politica = new PoliticaDesconto();

    @Test
    void vipRecebeDezPorcentoSemCupom() {
        Cliente vip = new Cliente(true, false, 5);
        assertEquals(1_000L, politica.calcular(vip, 10_000L, null));
    }

    @Test
    void comumComSubtotalAltoRecebeCincoPorcento() {
        Cliente comum = new Cliente(false, false, 1);
        // 60_000 * 5 / 100 = 3_000
        assertEquals(3_000L, politica.calcular(comum, 60_000L, null));
    }

    @Test
    void comumComSubtotalBaixoRecebeZero() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(0L, politica.calcular(comum, 10_000L, null));
    }

    @Test
    void cupomBrancoMantemDescontoBase() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(3_000L, politica.calcular(comum, 60_000L, "   "));
    }

    @Test
    void bemvindoElegivelSomaFixo() {
        Cliente novo = new Cliente(false, false, 0);
        // base 0 (10_000 < 50_000) + 2_000
        assertEquals(2_000L, politica.calcular(novo, 10_000L, "BEMVINDO"));
    }

    @Test
    void bemvindoNormalizadoComEspacosEMinusculas() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals(2_000L, politica.calcular(novo, 10_000L, " bemvindo "));
    }

    @Test
    void bemvindoSemElegibilidadePorHistorico() {
        Cliente antigo = new Cliente(false, false, 2);
        assertEquals(0L, politica.calcular(antigo, 10_000L, "BEMVINDO"));
    }

    @Test
    void bemvindoSemElegibilidadePorSubtotal() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals(0L, politica.calcular(novo, 9_999L, "BEMVINDO"));
    }

    @Test
    void extra10ElegivelSomaDezPorcento() {
        Cliente comum = new Cliente(false, false, 1);
        // base 0 (20_000 < 50_000) + 2_000
        assertEquals(2_000L, politica.calcular(comum, 20_000L, "EXTRA10"));
    }

    @Test
    void extra10SemElegibilidade() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(0L, politica.calcular(comum, 19_999L, "EXTRA10"));
    }

    @Test
    void extra10ComBaseComumAcumula() {
        Cliente comum = new Cliente(false, false, 1);
        // base 60_000*5%=3_000 + 6_000 = 9_000, teto 12_000
        assertEquals(9_000L, politica.calcular(comum, 60_000L, "EXTRA10"));
    }

    @Test
    void tetoLimitaCombinacaoVipMaisBemvindo() {
        Cliente vipNovo = new Cliente(true, false, 0);
        // base 1_000 + 2_000 = 3_000, teto 2_000
        assertEquals(2_000L, politica.calcular(vipNovo, 10_000L, "BEMVINDO"));
    }

    @Test
    void tetoExatoVipMaisExtra10() {
        Cliente vip = new Cliente(true, false, 3);
        // base 10_000 + 10_000 = 20_000, teto 20_000
        assertEquals(20_000L, politica.calcular(vip, 100_000L, "EXTRA10"));
    }

    @Test
    void truncamentoParaBaixo() {
        Cliente vip = new Cliente(true, false, 1);
        // 333 * 10 / 100 = 33 (33.3 truncado)
        assertEquals(33L, politica.calcular(vip, 333L, null));
    }

    @Test
    void cupomDesconhecidoLancaExcecao() {
        Cliente comum = new Cliente(false, false, 1);
        assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(comum, 10_000L, "PROMO99"));
    }

    @Test
    void subtotalNegativoLancaExcecao() {
        Cliente comum = new Cliente(false, false, 1);
        assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(comum, -1L, null));
    }

    @Test
    void fronteiraCincoPorcentoEm50000() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(2_500L, politica.calcular(comum, 50_000L, null));
        assertEquals(0L, politica.calcular(comum, 49_999L, null));
    }
}
