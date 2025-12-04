package interfaz;

import modelo.Mision;
import java.awt.Component;
import java.net.URL;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class RecursosMision extends DefaultListCellRenderer {

    private ImageIcon iconoSalud;
    private ImageIcon iconoFuerza;
    private ImageIcon iconoIntelecto;
    private ImageIcon iconoDefault;

    public RecursosMision() {
        iconoSalud = cargarIcono("/recursos/icono_pocion.png");
        iconoFuerza = cargarIcono("/recursos/icono_espada.png");
        iconoIntelecto = cargarIcono("/recursos/icono_libro.png");
        iconoDefault = cargarIcono("/recursos/icono_pocion.png"); 
    }

    private ImageIcon cargarIcono(String ruta) {
        URL imgUrl = getClass().getResource(ruta);
        if (imgUrl != null) {
            return new ImageIcon(imgUrl);
        } else {
            return null; 
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof Mision) {
            Mision m = (Mision) value;
            
            String colorEstado = m.estaCompletada() ? "#27ae60" : "black";
            String estadoTexto = m.estaCompletada() ? "<b>¡COMPLETADA!</b>" : "Pendiente";
            
            String textoHTML = String.format(
                "<html><body style='width: 300px; padding: 5px;'>" +
                "<span style='font-size:14px; font-weight:bold; color:%s;'>%s</span><br>" + 
                "<span style='font-size:11px;'>Recompensa: <font color='#f1c40f'>%d XP</font> | <font color='#e67e22'>%d Oro</font></span><br>" +
                "<span style='font-size:10px; color:gray;'>%s</span>" +
                "</body></html>",
                colorEstado,
                m.obtenerTitulo(),
                m.obtenerRecompensaXP(),
                m.obtenerRecompensaOro(),
                estadoTexto
            );
            
            label.setText(textoHTML);

            String tipo = m.obtenerTipo(); 
            if (tipo == null) tipo = "default";
            
            if (tipo.equalsIgnoreCase("fuerza")) {
                label.setIcon(iconoFuerza);
            } else if (tipo.equalsIgnoreCase("intelecto")) {
                label.setIcon(iconoIntelecto);
            } else if (tipo.equalsIgnoreCase("salud")) {
                label.setIcon(iconoSalud);
            } else {
                label.setIcon(iconoDefault);
            }
        }
        
        return label;
    }
}