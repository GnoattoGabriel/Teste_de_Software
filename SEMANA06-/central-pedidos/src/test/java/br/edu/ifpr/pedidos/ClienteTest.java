package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void aceitaHistoricoZeroEPositivo() {
        assertEquals(0, new Cliente(false, false, 0).comprasAnteriores());
        assertEquals(3, new Cliente(true, false, 3).comprasAnteriores());
    }

    @Test
    void rejeitaHistoricoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(false, false, -1));
    }
}
