package application;

import domain.model.Pedido;
import domain.port.in.CrearPedidoUseCase;
import domain.port.out.PedidoRepository;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepository repo;

    public CrearPedidoService(PedidoRepository repo) {
        this.repo = repo;
    }

    public void ejecutar(Pedido pedido) {
        repo.guardar(pedido);
    }
}
