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

    private final CrearPedidoUseCase useCase;
    private final PedidoRepository repo;

    public PedidoController(CrearPedidoUseCase useCase, PedidoRepository repo) {
        this.useCase = useCase;
        this.repo = repo;
    }

    @PostMapping
    public void crear(@RequestBody Map<String, Object> req) {
        Pedido p = new Pedido(
                (String) req.get("nombre"),
                ((Number) req.get("total")).doubleValue()
        );
        useCase.ejecutar(p);
    }

    @GetMapping
    public List<Pedido> listar() {
        return repo.listar();
    }
}
