package application;

import domain.model.Pedido;
import domain.port.in.CrearPedidoUseCase;
import domain.port.out.PedidoRepository;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CrearPedidoService(PedidoRepository repo) {
        this.pedidoRepository = repo;
    }

    public void ejecutar(Pedido pedido) {
        pedidoRepository.guardar(pedido);
    }
}
