package modelo;

public class Mision {
    private int id;
    private String titulo;
    private String descripcion;
    private int recompensaXP;
    private int recompensaOro;
    private boolean completada;
    private String tipo;

    public Mision(int id, String titulo, String descripcion, int xp, int oro, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.recompensaXP = xp;
        this.recompensaOro = oro;
        this.tipo = tipo; 
        this.completada = false;
    }

    public int obtenerId() { return id; }
    public String obtenerTitulo() { return titulo; }
    public String obtenerDescripcion() { return descripcion; }
    public int obtenerRecompensaXP() { return recompensaXP; }
    public int obtenerRecompensaOro() { return recompensaOro; }
    public boolean estaCompletada() { return completada; }
    public void establecerCompletada(boolean completada) { this.completada = completada; }
    
    public String obtenerTipo() { return tipo; }
    
    @Override
    public String toString() {
        return titulo; 
    }
}