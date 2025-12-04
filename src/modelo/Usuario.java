package modelo;

public class Usuario {
    private int id;
    private String nombre;
    private int nivel;
    private int experiencia;
    private int monedas;

    public Usuario(int id, String nombre, int nivel, int experiencia, int monedas) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.monedas = monedas;
    }

    public int obtenerId() { return id; }
    public String obtenerNombre() { return nombre; }
    
    public void establecerNombre(String nombre) { this.nombre = nombre; }
    
    public int obtenerNivel() { return nivel; }
    public void establecerNivel(int nivel) { this.nivel = nivel; }
    public int obtenerExperiencia() { return experiencia; }
    public void establecerExperiencia(int experiencia) { this.experiencia = experiencia; }
    public int obtenerMonedas() { return monedas; }
    public void establecerMonedas(int monedas) { this.monedas = monedas; }
}