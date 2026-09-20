package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void bloqueadoRecusadoMesmoComTotalBaixo() {
        Cliente bloqueado = new Cliente(false, true, 5);
        assertEquals("RECUSADO", risco.avaliar(bloqueado, 1_000L, false));
    }

    @Test
    void novoClienteAprovadoComTotalBaixoESemExpresso() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("APROVADO", risco.avaliar(novo, 100_000L, false));
    }

    @Test
    void novoClienteRevisaoPorTotalAlto() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(novo, 100_001L, false));
    }

    @Test
    void novoClienteRevisaoPorExpressoMesmoComTotalBaixo() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(novo, 1_000L, true));
    }

    @Test
    void novoClienteRevisaoComTotalAltoEExpresso() {
        Cliente novo = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(novo, 200_000L, true));
    }

    @Test
    void clienteAntigoComumRevisaoPorTotalMuitoAlto() {
        Cliente antigo = new Cliente(false, false, 3);
        assertEquals("REVISAO", risco.avaliar(antigo, 500_001L, false));
    }

    @Test
    void clienteAntigoVipAprovadoMesmoComTotalMuitoAlto() {
        Cliente vip = new Cliente(true, false, 3);
        assertEquals("APROVADO", risco.avaliar(vip, 600_000L, false));
    }

    @Test
    void fronteira500000NaoGeraRevisao() {
        Cliente antigo = new Cliente(false, false, 3);
        assertEquals("APROVADO", risco.avaliar(antigo, 500_000L, false));
    }

    @Test
    void clienteAntigoAprovadoComTotalBaixo() {
        Cliente antigo = new Cliente(false, false, 3);
        assertEquals("APROVADO", risco.avaliar(antigo, 10_000L, false));
    }

    @Test
    void totalNegativoLancaExcecao() {
        Cliente c = new Cliente(false, false, 1);
        assertThrows(IllegalArgumentException.class, () -> risco.avaliar(c, -1L, false));
    }
}
