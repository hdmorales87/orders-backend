package infrastructure.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PedidoEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private double total;

}
