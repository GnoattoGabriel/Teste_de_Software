package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void aprovaNaPrimeiraTentativaComUmaChamada() {
        List<Long> chamadas = new ArrayList<>();
        PagamentoService svc = new PagamentoService(total -> {
            chamadas.add(total);
            return true;
        });
        assertTrue(svc.pagar(11_200L, 3));
        assertEquals(List.of(11_200L), chamadas);
    }

    @Test
    void recusaImediataSemRepetir() {
        AtomicInteger n = new AtomicInteger();
        PagamentoService svc = new PagamentoService(total -> {
            n.incrementAndGet();
            return false;
        });
        assertFalse(svc.pagar(5_000L, 3));
        assertEquals(1, n.get());
    }

    @Test
    void indisponibilidadeTemporariaPermiteNovaTentativaComSucesso() {
        AtomicInteger n = new AtomicInteger();
        List<Long> valores = new ArrayList<>();
        PagamentoService svc = new PagamentoService(total -> {
            valores.add(total);
            if (n.incrementAndGet() == 1) throw new IllegalStateException("fora do ar");
            return true;
        });
        assertTrue(svc.pagar(7_000L, 3));
        assertEquals(2, n.get());
        assertEquals(List.of(7_000L, 7_000L), valores);
    }

    @Test
    void esgotaTentativasRetornaFalso() {
        AtomicInteger n = new AtomicInteger();
        PagamentoService svc = new PagamentoService(total -> {
            n.incrementAndGet();
            throw new IllegalStateException("fora do ar");
        });
        assertFalse(svc.pagar(7_000L, 3));
        assertEquals(3, n.get());
    }

    @Test
    void respeitaLimiteDeUmaTentativa() {
        AtomicInteger n = new AtomicInteger();
        PagamentoService svc = new PagamentoService(total -> {
            n.incrementAndGet();
            throw new IllegalStateException("fora do ar");
        });
        assertFalse(svc.pagar(7_000L, 1));
        assertEquals(1, n.get());
    }

    @Test
    void outraExcecaoPropagaSemRepetir() {
        AtomicInteger n = new AtomicInteger();
        PagamentoService svc = new PagamentoService(total -> {
            n.incrementAndGet();
            throw new IllegalArgumentException("boom");
        });
        assertThrows(IllegalArgumentException.class, () -> svc.pagar(7_000L, 3));
        assertEquals(1, n.get());
    }

    @Test
    void totalNaoPositivoLancaExcecao() {
        PagamentoService svc = new PagamentoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> svc.pagar(0L, 3));
        assertThrows(IllegalArgumentException.class, () -> svc.pagar(-10L, 3));
    }

    @Test
    void limiteForaDe1a3LancaExcecao() {
        PagamentoService svc = new PagamentoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> svc.pagar(100L, 0));
        assertThrows(IllegalArgumentException.class, () -> svc.pagar(100L, 4));
    }

    @Test
    void processadorNuloLancaNpe() {
        assertThrows(NullPointerException.class, () -> new PagamentoService(null));
    }
}
