package infrastructure.adapter.out.persistence;

import domain.model.Pedido;
import domain.port.out.PedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final SpringDataPedidoRepository springDataPedidoRepository;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository springDataPedidoRepository) {
        this.springDataPedidoRepository = springDataPedidoRepository;
    }

    @Override
    public void guardar(Pedido pedido) {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setNombre(pedido.getNombre());
        pedidoEntity.setTotal(pedido.getTotal());
        springDataPedidoRepository.save(pedidoEntity);
    }

    @Override
    public List<Pedido> listar() {
        return springDataPedidoRepository.findAll().stream()
                .map(pedidoEntity -> new Pedido(pedidoEntity.getNombre(), pedidoEntity.getTotal()))
                .toList();
    }
}
