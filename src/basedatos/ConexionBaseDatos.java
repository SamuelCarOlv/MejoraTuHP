package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    
    private static final String URL = "jdbc:mysql://localhost:3306/mejoratuhp_db";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println(">> Conexión a Base de Datos establecida.");
        } catch (ClassNotFoundException e) {
            System.err.println("FALTA DRIVER: Agrega la librería MySQL JDBC Driver a tu proyecto.");
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR SQL: " + e.getMessage());
            return null;
        }
        return conexion;
    }
}