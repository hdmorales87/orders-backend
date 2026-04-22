package infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import application.CrearPedidoService;
import domain.port.out.PedidoRepository;
import domain.port.in.CrearPedidoUseCase;

@Configuration
public class BeanConfig {

    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(PedidoRepository pedidoRepository) {
        return new CrearPedidoService(pedidoRepository);
    }
}
