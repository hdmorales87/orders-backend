package domain.model;

public class Pedido {
    private String nombre;
    private double total;

    public Pedido(String nombre, double total) {
        if (total <= 0) throw new IllegalArgumentException();
        this.setNombre(nombre);
        this.setTotal(total);
    }

    public String getNombre() { return nombre; }
    public double getTotal() { return total; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
