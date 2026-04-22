package domain.port.in;

import domain.model.Pedido;

public interface CrearPedidoUseCase {
    void ejecutar(Pedido pedido);
}
