/* Archivo: interfaz/RecursosTienda.java */
package interfaz;

import modelo.Articulo;
import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class RecursosTienda extends DefaultListCellRenderer {

    private ImageIcon imgMarco, imgTema, imgPocion, imgCorona, imgFondo, imgDefault;

    public RecursosTienda() {
        imgMarco = cargarIcono("/recursos/item_marco.png");
        imgTema = cargarIcono("/recursos/item_tema.png");
        imgPocion = cargarIcono("/recursos/item_pocion.png");
        imgCorona = cargarIcono("/recursos/item_corona.png");
        imgFondo = cargarIcono("/recursos/icono_fondogamer.png"); 
        imgDefault = cargarIcono("/recursos/item_default.png");
    }

    private ImageIcon cargarIcono(String ruta) {
        URL imgUrl = getClass().getResource(ruta);
        return (imgUrl != null) ? new ImageIcon(imgUrl) : null;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof Articulo) {
            Articulo a = (Articulo) value;
            String nombre = a.obtenerNombre();
            
            if (nombre.contains("Marco")) label.setIcon(imgMarco);
            else if (nombre.contains("Tema")) label.setIcon(imgTema);
            else if (nombre.contains("Poción")) label.setIcon(imgPocion);
            else if (nombre.contains("Corona")) label.setIcon(imgCorona);
            else if (nombre.contains("Fondo")) label.setIcon(imgFondo);
            else label.setIcon(imgDefault);

            if (a.estaEquipado()) {
                label.setText("★ " + nombre + " [EQUIPADO]");
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setText(nombre);
            }
        }
        return label;
    }
}