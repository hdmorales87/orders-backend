package infrastructure.adapter.in.rest;

import domain.model.Pedido;
import domain.port.in.CrearPedidoUseCase;
import domain.port.out.PedidoRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final PedidoRepository pedidoRepository;

    public PedidoController(CrearPedidoUseCase crearPedidoUseCase, PedidoRepository pedidoRepository) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping
    public void crear(@RequestBody Map<String, Object> request) {
        Pedido pedido = new Pedido(
                (String) request.get("nombre"),
                ((Number) request.get("total")).doubleValue()
        );
        crearPedidoUseCase.ejecutar(pedido);
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.listar();
    }
}
