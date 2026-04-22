package infrastructure.adapter.out.persistence;

import domain.model.Pedido;
import domain.port.out.PedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final SpringDataPedidoRepository jpa;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(Pedido pedido) {
        PedidoEntity e = new PedidoEntity();
        e.setNombre(pedido.getNombre());
        e.setTotal(pedido.getTotal());
        jpa.save(e);
    }

    @Override
    public List<Pedido> listar() {
        return jpa.findAll().stream()
                .map(e -> new Pedido(e.getNombre(), e.getTotal()))
                .toList();
    }
}
