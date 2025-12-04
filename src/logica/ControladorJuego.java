/* Archivo: logica/ControladorJuego.java */
package logica;

import basedatos.ConexionBaseDatos;
import modelo.Mision;
import modelo.Usuario;
import modelo.Articulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorJuego {
    private Usuario usuarioActual;
    private List<Mision> listaMisiones;
    private List<Articulo> listaArticulos;

    public ControladorJuego() {
        this.listaMisiones = new ArrayList<>();
        this.listaArticulos = new ArrayList<>();
        this.usuarioActual = null; 
    }

    public Usuario obtenerUsuario() { return usuarioActual; }
    public List<Mision> obtenerMisiones() { return listaMisiones; }

    public boolean iniciarSesion(String correo, String password) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            if (con == null) return false;
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo); ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.usuarioActual = new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getInt("nivel"), rs.getInt("experiencia"), rs.getInt("monedas"));
                cargarMisionesDelUsuario(); 
                return true;
            }
            return false;
        } catch (SQLException e) { return false; }
    }

    public boolean registrarUsuario(String nombre, String correo, String password) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            if (con == null) return false;
            String sql = "INSERT INTO usuarios (nombre, correo, contrasena, nivel, experiencia, monedas) VALUES (?, ?, ?, 1, 0, 0)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre); ps.setString(2, correo); ps.setString(3, password);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public void cargarMisionesDelUsuario() {
        if (usuarioActual == null) return;
        listaMisiones.clear();
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            String sql = "SELECT * FROM misiones WHERE usuario_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioActual.obtenerId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mision m = new Mision(rs.getInt("id"), rs.getString("titulo"), rs.getString("descripcion"), rs.getInt("recompensa_xp"), rs.getInt("recompensa_oro"), rs.getString("tipo"));
                String sqlCheck = "SELECT COUNT(*) FROM historial_misiones WHERE usuario_id = ? AND mision_id = ? AND DATE(fecha_completada) = CURDATE()";
                PreparedStatement psCheck = con.prepareStatement(sqlCheck);
                psCheck.setInt(1, usuarioActual.obtenerId()); psCheck.setInt(2, m.obtenerId());
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) > 0) m.establecerCompletada(true);
                listaMisiones.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void completarMision(Mision mision) {
        if (mision.estaCompletada()) { JOptionPane.showMessageDialog(null, "¡Ya completada!"); return; }
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            mision.establecerCompletada(true);
            int nuevaXP = usuarioActual.obtenerExperiencia() + mision.obtenerRecompensaXP();
            int nuevasMonedas = usuarioActual.obtenerMonedas() + mision.obtenerRecompensaOro();
            usuarioActual.establecerExperiencia(nuevaXP); usuarioActual.establecerMonedas(nuevasMonedas);
            
            PreparedStatement psUpd = con.prepareStatement("UPDATE usuarios SET experiencia = ?, monedas = ? WHERE id = ?");
            psUpd.setInt(1, nuevaXP); psUpd.setInt(2, nuevasMonedas); psUpd.setInt(3, usuarioActual.obtenerId());
            psUpd.executeUpdate();
            
            PreparedStatement psHist = con.prepareStatement("INSERT INTO historial_misiones (usuario_id, mision_id) VALUES (?, ?)");
            psHist.setInt(1, usuarioActual.obtenerId()); psHist.setInt(2, mision.obtenerId()); 
            psHist.executeUpdate();
            verificarSubidaNivel(con);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarMision(Mision mision) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement psHist = con.prepareStatement("DELETE FROM historial_misiones WHERE mision_id = ?");
            psHist.setInt(1, mision.obtenerId()); psHist.executeUpdate();
            PreparedStatement ps = con.prepareStatement("DELETE FROM misiones WHERE id = ? AND usuario_id = ?");
            ps.setInt(1, mision.obtenerId()); ps.setInt(2, usuarioActual.obtenerId());
            ps.executeUpdate();
            cargarMisionesDelUsuario();
        } catch (SQLException e) { JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage()); }
    }

    public void crearMisionPersonalizada(String titulo, int xp, int oro, String tipo) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO misiones (titulo, descripcion, recompensa_xp, recompensa_oro, es_personalizada, usuario_id, tipo) VALUES (?, 'Personalizada', ?, ?, TRUE, ?, ?)");
            ps.setString(1, titulo); ps.setInt(2, xp); ps.setInt(3, oro); ps.setInt(4, usuarioActual.obtenerId()); ps.setString(5, tipo);
            ps.executeUpdate();
            cargarMisionesDelUsuario(); 
        } catch (SQLException e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public List<Mision> obtenerPlantillasPredefinidas() {
        List<Mision> plantillas = new ArrayList<>();
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            ResultSet rs = con.prepareStatement("SELECT * FROM misiones WHERE usuario_id IS NULL").executeQuery();
            while (rs.next()) plantillas.add(new Mision(rs.getInt("id"), rs.getString("titulo"), rs.getString("descripcion"), rs.getInt("recompensa_xp"), rs.getInt("recompensa_oro"), rs.getString("tipo")));
        } catch (SQLException e) { e.printStackTrace(); }
        return plantillas;
    }
    public void agregarMisionPredefinida(Mision p) { crearMisionPersonalizada(p.obtenerTitulo(), p.obtenerRecompensaXP(), p.obtenerRecompensaOro(), p.obtenerTipo()); }

    private void verificarSubidaNivel(Connection con) throws SQLException {
        int xpNec = usuarioActual.obtenerNivel() * 200;
        if (usuarioActual.obtenerExperiencia() >= xpNec) {
            usuarioActual.establecerNivel(usuarioActual.obtenerNivel() + 1);
            usuarioActual.establecerExperiencia(usuarioActual.obtenerExperiencia() - xpNec);
            PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET nivel = ?, experiencia = ? WHERE id = ?");
            ps.setInt(1, usuarioActual.obtenerNivel()); ps.setInt(2, usuarioActual.obtenerExperiencia()); ps.setInt(3, usuarioActual.obtenerId());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "¡NIVEL UP! Nivel " + usuarioActual.obtenerNivel());
        }
    }

    public boolean cambiarNombreUsuario(String nuevoNombre) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement psCheck = con.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE nombre = ?");
            psCheck.setString(1, nuevoNombre);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) { JOptionPane.showMessageDialog(null, "¡Ocupado!"); return false; }
            PreparedStatement psUpd = con.prepareStatement("UPDATE usuarios SET nombre = ? WHERE id = ?");
            psUpd.setString(1, nuevoNombre); psUpd.setInt(2, usuarioActual.obtenerId()); psUpd.executeUpdate();
            usuarioActual.establecerNombre(nuevoNombre);
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<Articulo> obtenerInventarioUsuario() {
        List<Articulo> inv = new ArrayList<>();
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement("SELECT a.*, i.es_equipado FROM articulos_tienda a JOIN inventario_usuario i ON a.id = i.articulo_id WHERE i.usuario_id = ?");
            ps.setInt(1, usuarioActual.obtenerId()); ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Articulo a = new Articulo(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("precio"), rs.getString("tipo"));
                a.establecerEquipado(rs.getBoolean("es_equipado"));
                inv.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return inv;
    }

    public void equiparArticulo(Articulo art) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement psR = con.prepareStatement("UPDATE inventario_usuario i JOIN articulos_tienda a ON i.articulo_id = a.id SET i.es_equipado = FALSE WHERE i.usuario_id = ? AND a.tipo = ?");
            psR.setInt(1, usuarioActual.obtenerId()); psR.setString(2, art.obtenerTipo()); psR.executeUpdate();
            PreparedStatement psE = con.prepareStatement("UPDATE inventario_usuario SET es_equipado = TRUE WHERE usuario_id = ? AND articulo_id = ?");
            psE.setInt(1, usuarioActual.obtenerId()); psE.setInt(2, art.obtenerId()); psE.executeUpdate();
            art.establecerEquipado(true);
        } catch (SQLException e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
    }

    public void desequiparArticulo(Articulo art) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement("UPDATE inventario_usuario SET es_equipado = FALSE WHERE usuario_id = ? AND articulo_id = ?");
            ps.setInt(1, usuarioActual.obtenerId()); ps.setInt(2, art.obtenerId()); ps.executeUpdate();
            art.establecerEquipado(false);
        } catch (SQLException e) { }
    }

    public Articulo obtenerArticuloEquipadoPorNombre(String palabraClave) {
        for (Articulo a : obtenerInventarioUsuario()) {
            if (a.estaEquipado() && a.obtenerNombre().contains(palabraClave)) return a;
        }
        return null;
    }

    public boolean esTemaOscuroActivo() { return obtenerArticuloEquipadoPorNombre("Tema Oscuro") != null; }

    public List<Articulo> obtenerArticulosTienda() {
        listaArticulos.clear();
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            if (con != null) {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM articulos_tienda");
                while (rs.next()) listaArticulos.add(new Articulo(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("precio"), rs.getString("tipo")));
            }
        } catch (SQLException e) { }
        return listaArticulos;
    }

    public boolean tieneArticulo(int id) {
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM inventario_usuario WHERE usuario_id = ? AND articulo_id = ?");
            ps.setInt(1, usuarioActual.obtenerId()); ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { }
        return false;
    }

    public boolean comprarArticulo(Articulo a) {
        if (usuarioActual.obtenerMonedas() < a.obtenerPrecio()) { JOptionPane.showMessageDialog(null, "¡Falta Oro!"); return false; }
        try (Connection con = ConexionBaseDatos.obtenerConexion()) {
            if (tieneArticulo(a.obtenerId())) { JOptionPane.showMessageDialog(null, "¡Ya lo tienes!"); return false; }
            int nMon = usuarioActual.obtenerMonedas() - a.obtenerPrecio();
            PreparedStatement psU = con.prepareStatement("UPDATE usuarios SET monedas = ? WHERE id = ?");
            psU.setInt(1, nMon); psU.setInt(2, usuarioActual.obtenerId()); psU.executeUpdate();
            PreparedStatement psI = con.prepareStatement("INSERT INTO inventario_usuario (usuario_id, articulo_id) VALUES (?, ?)");
            psI.setInt(1, usuarioActual.obtenerId()); psI.setInt(2, a.obtenerId()); psI.executeUpdate();
            usuarioActual.establecerMonedas(nMon);
            JOptionPane.showMessageDialog(null, "¡Comprado!");
            return true;
        } catch (SQLException e) { return false; }
    }
}