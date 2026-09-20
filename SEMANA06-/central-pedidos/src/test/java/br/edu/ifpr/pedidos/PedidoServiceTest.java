package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        // 1. Preparar: cliente comum, uma compra anterior e item disponível de R$ 100,00.
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        // Simula o pagamento e registra as cobranças, sem banco ou serviço externo.
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        // 2. Executar: percorrer um caminho completo do fechamento.
        ResultadoPedido resultado = service.fechar(pedido, cliente);

        // 3. Verificar: sem desconto; frete de R$ 12,00; total de R$ 112,00.
        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(1_200L, resultado.freteCentavos()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            // A lista comprova uma única cobrança, com o valor correto.
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void bloqueadoRetornaZerosSemChamarPagamento() {
        Cliente bloqueado = new Cliente(false, true, 5);
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, bloqueado);

        assertAll(
            () -> assertEquals("BLOQUEADO", r.status()),
            () -> assertEquals(0L, r.subtotalCentavos()),
            () -> assertEquals(0L, r.descontoCentavos()),
            () -> assertEquals(0L, r.freteCentavos()),
            () -> assertEquals(0L, r.totalCentavos()),
            () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void pedidoSemItensAtivosLancaExcecao() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido inativo = new ItemPedido("A", 10_000, 0, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(inativo), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> service.fechar(pedido, cliente));
    }

    @Test
    void semEstoqueRetornaZerosAntesDoCupom() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("A", 10_000, 5, 1, 1_000, false);
        // Cupom desconhecido provaria avaliacao do cupom; como falta estoque
        // retorna antes, nenhuma excecao deve ocorrer.
        Pedido pedido = new Pedido(List.of(item), "PR", false, "INEXISTENTE");
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("SEM_ESTOQUE", r.status()),
            () -> assertEquals(0L, r.totalCentavos()),
            () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void revisaoPorExpressoComValoresCalculadosESemCobranca() {
        Cliente novo = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", true, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, novo);

        // subtotal 10_000, desconto 0, frete 1_200+1_500=2_700, total 12_700
        assertAll(
            () -> assertEquals("REVISAO", r.status()),
            () -> assertEquals(10_000L, r.subtotalCentavos()),
            () -> assertEquals(0L, r.descontoCentavos()),
            () -> assertEquals(2_700L, r.freteCentavos()),
            () -> assertEquals(12_700L, r.totalCentavos()),
            () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void revisaoPorTotalAltoDeClienteNovo() {
        Cliente novo = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("A", 50_000, 3, 5, 100, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, novo);

        // subtotal 150_000, desconto 5%=7_500, liquido 142_500,
        // frete 0 (gratuidade), total 142_500 > 100_000 -> REVISAO
        assertAll(
            () -> assertEquals("REVISAO", r.status()),
            () -> assertEquals(150_000L, r.subtotalCentavos()),
            () -> assertEquals(7_500L, r.descontoCentavos()),
            () -> assertEquals(0L, r.freteCentavos()),
            () -> assertEquals(142_500L, r.totalCentavos()),
            () -> assertTrue(cobrancas.isEmpty())
        );
    }

    @Test
    void pagamentoRecusadoMantemValoresEUmaTentativa() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return false;
        });

        ResultadoPedido r = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGAMENTO_RECUSADO", r.status()),
            () -> assertEquals(11_200L, r.totalCentavos()),
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void vipComGratuidadePagaMetadeDoFrete() {
        Cliente vip = new Cliente(true, false, 5);
        ItemPedido item = new ItemPedido("A", 20_000, 2, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "SP", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, vip);

        // subtotal 40_000, desconto 10%=4_000, liquido 36_000 -> frete 0, total 36_000
        assertAll(
            () -> assertEquals("PAGO", r.status()),
            () -> assertEquals(40_000L, r.subtotalCentavos()),
            () -> assertEquals(4_000L, r.descontoCentavos()),
            () -> assertEquals(0L, r.freteCentavos()),
            () -> assertEquals(36_000L, r.totalCentavos()),
            () -> assertEquals(List.of(36_000L), cobrancas)
        );
    }

    @Test
    void cupomBemvindoAplicadoNoFechamento() {
        Cliente novo = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("A", 15_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, "BEMVINDO");
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido r = service.fechar(pedido, novo);

        // subtotal 15_000, desconto 2_000, liquido 13_000, frete 1_200, total 14_200
        assertAll(
            () -> assertEquals("PAGO", r.status()),
            () -> assertEquals(15_000L, r.subtotalCentavos()),
            () -> assertEquals(2_000L, r.descontoCentavos()),
            () -> assertEquals(1_200L, r.freteCentavos()),
            () -> assertEquals(14_200L, r.totalCentavos()),
            () -> assertEquals(List.of(14_200L), cobrancas)
        );
    }

    @Test
    void pedidoOuClienteNuloLancaNpe() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);
        assertThrows(NullPointerException.class, () -> service.fechar(null, cliente));
        assertThrows(NullPointerException.class, () -> service.fechar(pedido, null));
    }

    @Test
    void cupomDesconhecidoPropagaExcecao() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, "PROMO99");
        PedidoService service = new PedidoService(total -> true);
        assertThrows(IllegalArgumentException.class, () -> service.fechar(pedido, cliente));
    }
}
