package modelo;

public class Articulo {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String tipo;
    
    private boolean equipado = false;

    public Articulo(int id, String nombre, String descripcion, int precio, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
    }

    public int obtenerId() { return id; }
    public String obtenerNombre() { return nombre; }
    public String obtenerDescripcion() { return descripcion; }
    public int obtenerPrecio() { return precio; }
    public String obtenerTipo() { return tipo; }

    public boolean estaEquipado() { return equipado; }
    public void establecerEquipado(boolean equipado) { this.equipado = equipado; }

    @Override
    public String toString() {
        return nombre + " - $" + precio + " oro";
    }
}