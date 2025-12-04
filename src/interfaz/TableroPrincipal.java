/* Archivo: interfaz/TableroPrincipal.java */
package interfaz;

import logica.ControladorJuego;
import modelo.Mision;
import modelo.Articulo;
import modelo.Usuario;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class TableroPrincipal extends JFrame {

    private ControladorJuego controlador;
    private JLabel lblNivel, lblXP, lblMonedas;
    private PanelAvatar panelAvatarVisual;
    
    private JList<Mision> listMisiones;
    private DefaultListModel<Mision> listModel;
    private DefaultListModel<Articulo> modeloTienda;
    private DefaultListModel<Articulo> modeloInventario;
    
    private JPanel pMisiones, pPerfil, pTienda, pDatos, pDerecho;
    private JList<Articulo> listTienda;
    private JList<Articulo> listaInventario;

    public TableroPrincipal(ControladorJuego ctrl) {
        this.controlador = ctrl;
        inicializarUI();
        actualizarDatosUsuario();
        actualizarCosmeticosVisuales();
    }

    private void inicializarUI() {
        setTitle("MejoraTuHP - Panel Principal");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}

        JTabbedPane tabs = new JTabbedPane();

        pMisiones = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<>();
        actualizarListaMisiones();
        
        listMisiones = new JList<>(listModel);
        listMisiones.setCellRenderer(new RecursosMision());
        
        JPanel pBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnCompletar = new JButton("✔ Completar"); btnCompletar.setBackground(new Color(39, 174, 96)); btnCompletar.setForeground(Color.WHITE);
        JButton btnEliminar = new JButton("🗑 Eliminar"); btnEliminar.setBackground(new Color(192, 57, 43)); btnEliminar.setForeground(Color.WHITE);
        JButton btnCrear = new JButton("➕ Crear Nueva");
        JButton btnPredefinida = new JButton("📋 Predefinidas");
        
        btnCompletar.addActionListener(e -> {
            Mision sel = listMisiones.getSelectedValue();
            if(sel != null) { controlador.completarMision(sel); actualizarDatosUsuario(); actualizarListaMisiones(); }
        });
        btnEliminar.addActionListener(e -> {
            Mision sel = listMisiones.getSelectedValue();
            if(sel != null && JOptionPane.showConfirmDialog(this, "¿Borrar?", "Confirmar", JOptionPane.YES_NO_OPTION)==0) { controlador.eliminarMision(sel); actualizarListaMisiones(); }
        });
        btnCrear.addActionListener(e -> {
            JPanel p = new JPanel(new GridLayout(0,1)); JTextField t = new JTextField(); JComboBox<String> c = new JComboBox<>(new String[]{"salud","fuerza","intelecto"});
            p.add(new JLabel("Título:")); p.add(t); p.add(new JLabel("Tipo:")); p.add(c);
            if(JOptionPane.showConfirmDialog(this, p, "Nueva", JOptionPane.OK_CANCEL_OPTION)==0 && !t.getText().isEmpty()) {
                controlador.crearMisionPersonalizada(t.getText(), 50, 10, (String)c.getSelectedItem()); actualizarListaMisiones();
            }
        });
        btnPredefinida.addActionListener(e -> {
            java.util.List<Mision> l = controlador.obtenerPlantillasPredefinidas();
            if(!l.isEmpty()) {
                Mision m = (Mision)JOptionPane.showInputDialog(this,"Elige:","Plantillas",JOptionPane.PLAIN_MESSAGE,null,l.toArray(),l.get(0));
                if(m!=null){ controlador.agregarMisionPredefinida(m); actualizarListaMisiones(); }
            }
        });

        pBotones.add(btnCompletar); pBotones.add(btnEliminar); pBotones.add(btnCrear); pBotones.add(btnPredefinida);
        pMisiones.add(new JScrollPane(listMisiones), BorderLayout.CENTER); pMisiones.add(pBotones, BorderLayout.SOUTH);


        pPerfil = new JPanel(new BorderLayout());
        
        pDatos = new JPanel(new BorderLayout());
        JPanel pStats = new JPanel(new GridLayout(4, 1));
        
        JLabel lblNombreUsuario = new JLabel(controlador.obtenerUsuario().obtenerNombre(), SwingConstants.CENTER);
        lblNombreUsuario.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombreUsuario.setForeground(new Color(41, 128, 185));

        lblNivel = new JLabel("Nivel: 1"); lblXP = new JLabel("XP: 0"); lblMonedas = new JLabel("Oro: 0");
        Font fStats = new Font("Segoe UI", Font.BOLD, 16);
        lblNivel.setFont(fStats); lblXP.setFont(fStats); lblMonedas.setFont(fStats);
        
        pStats.add(lblNombreUsuario); pStats.add(lblNivel); pStats.add(lblXP); pStats.add(lblMonedas);
        pStats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelAvatarVisual = new PanelAvatar();
        panelAvatarVisual.setPreferredSize(new Dimension(180, 200));
        
        pDatos.add(panelAvatarVisual, BorderLayout.CENTER); 
        pDatos.add(pStats, BorderLayout.SOUTH);
        pDatos.setPreferredSize(new Dimension(280, 0));

        pDerecho = new JPanel(new BorderLayout());
        pDerecho.setBorder(BorderFactory.createTitledBorder("Mi Inventario"));

        modeloInventario = new DefaultListModel<>();
        listaInventario = new JList<>(modeloInventario);
        listaInventario.setCellRenderer(new RecursosTienda());

        JPanel pBotonesPerfil = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton btnEquipar = new JButton("★ Equipar");
        JButton btnDesequipar = new JButton("✖ Desequipar");
        JButton btnCambiarNombre = new JButton("Cambiar Nombre");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(192, 57, 43)); btnCerrarSesion.setForeground(Color.WHITE);

        btnEquipar.addActionListener(e -> {
            Articulo sel = listaInventario.getSelectedValue();
            if (sel != null) {
                controlador.equiparArticulo(sel);
                recargarInventario();
                actualizarCosmeticosVisuales(); 
            }
        });
        btnDesequipar.addActionListener(e -> {
            Articulo sel = listaInventario.getSelectedValue();
            if (sel != null && sel.estaEquipado()) {
                controlador.desequiparArticulo(sel);
                recargarInventario();
                actualizarCosmeticosVisuales();
                JOptionPane.showMessageDialog(this, "Desequipado.");
            }
        });
        btnCambiarNombre.addActionListener(e -> {
            String n = JOptionPane.showInputDialog(this,"Nuevo:");
            if(n!=null && !n.trim().isEmpty() && controlador.cambiarNombreUsuario(n.trim())) lblNombreUsuario.setText(n.trim());
        });
        btnCerrarSesion.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this,"¿Salir?","Salir",JOptionPane.YES_NO_OPTION)==0) { dispose(); new VentanaLogin().setVisible(true); }
        });

        pBotonesPerfil.add(btnEquipar); pBotonesPerfil.add(btnDesequipar); pBotonesPerfil.add(btnCambiarNombre); pBotonesPerfil.add(btnCerrarSesion);
        pDerecho.add(new JScrollPane(listaInventario), BorderLayout.CENTER); pDerecho.add(pBotonesPerfil, BorderLayout.SOUTH);
        
        pPerfil.add(pDatos, BorderLayout.WEST); pPerfil.add(pDerecho, BorderLayout.CENTER);


        pTienda = new JPanel(new BorderLayout());
        modeloTienda = new DefaultListModel<>();
        listTienda = new JList<>(modeloTienda);
        listTienda.setCellRenderer(new RecursosTienda());
        for (Articulo a : controlador.obtenerArticulosTienda()) modeloTienda.addElement(a);

        JPanel pInfoTienda = new JPanel(new GridLayout(4, 1));
        JLabel lNom = new JLabel("Item..."); JLabel lDesc = new JLabel(""); JLabel lPre = new JLabel("Precio: -");
        JButton btnComprar = new JButton("Comprar"); btnComprar.setBackground(new Color(241, 196, 15));
        lNom.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        listTienda.addListSelectionListener(e -> {
            Articulo a = listTienda.getSelectedValue();
            if(a!=null) {
                lNom.setText(a.obtenerNombre()); lDesc.setText("<html>"+a.obtenerDescripcion()+"</html>"); lPre.setText("Precio: $"+a.obtenerPrecio());
                if(controlador.tieneArticulo(a.obtenerId())) {
                    btnComprar.setText("Comprado"); btnComprar.setEnabled(false);
                } else {
                    btnComprar.setText("Comprar"); btnComprar.setEnabled(true);
                }
            }
        });
        btnComprar.addActionListener(e -> {
            Articulo a = listTienda.getSelectedValue();
            if(a!=null && controlador.comprarArticulo(a)) { actualizarDatosUsuario(); listTienda.clearSelection(); }
        });
        pInfoTienda.add(lNom); pInfoTienda.add(lDesc); pInfoTienda.add(lPre); pInfoTienda.add(btnComprar);
        pTienda.add(new JScrollPane(listTienda), BorderLayout.CENTER); pTienda.add(pInfoTienda, BorderLayout.SOUTH);

        tabs.addTab("Misiones", pMisiones);
        tabs.addTab("Mi Perfil", pPerfil);
        tabs.addTab("Tienda", pTienda);
        tabs.addChangeListener(e -> { if(tabs.getSelectedIndex()==1) recargarInventario(); });
        
        add(tabs);
    }

    private void actualizarCosmeticosVisuales() {
        Articulo fondo = controlador.obtenerArticuloEquipadoPorNombre("Fondo");
        
        panelAvatarVisual.setTieneFondo(fondo != null);
        
        Articulo marco = controlador.obtenerArticuloEquipadoPorNombre("Marco");
        Articulo corona = controlador.obtenerArticuloEquipadoPorNombre("Corona");
        
        panelAvatarVisual.setTieneMarco(marco != null);
        panelAvatarVisual.setTieneCorona(corona != null);
        
        panelAvatarVisual.repaint();
        
        actualizarTemaGlobal();
    }
    
    private void actualizarTemaGlobal() {
        boolean oscuro = controlador.esTemaOscuroActivo();
        Color fondo = oscuro ? new Color(44, 62, 80) : null;
        Color texto = oscuro ? Color.WHITE : Color.BLACK;
        
        JPanel[] paneles = {pMisiones, pPerfil, pTienda, pDatos, pDerecho};
        for(JPanel p : paneles) {
            if(p != null) {
                p.setBackground(fondo);
                for(Component c : p.getComponents()) {
                    if(c instanceof JLabel) c.setForeground(texto);
                    if(c instanceof JPanel) c.setBackground(fondo);
                }
            }
        }
        if(listMisiones != null) {
            listMisiones.setBackground(oscuro ? Color.GRAY : Color.WHITE);
            listMisiones.setForeground(texto);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void recargarInventario() { modeloInventario.clear(); for(Articulo a:controlador.obtenerInventarioUsuario()) modeloInventario.addElement(a); }
    private void actualizarDatosUsuario() { Usuario u = controlador.obtenerUsuario(); if(u!=null) { lblNivel.setText("Nivel: "+u.obtenerNivel()); lblXP.setText("XP: "+u.obtenerExperiencia()+" / "+(u.obtenerNivel()*200)); lblMonedas.setText("Oro: "+u.obtenerMonedas()); } }
    private void actualizarListaMisiones() { listModel.clear(); for(Mision m:controlador.obtenerMisiones()) listModel.addElement(m); }

    class PanelAvatar extends JPanel {
        private Image imgBase, imgMarco, imgCorona, imgFondoItem;
        
        private boolean tieneMarco = false;
        private boolean tieneCorona = false;
        private boolean tieneFondo = false;

        public PanelAvatar() {
            imgBase = cargarImagen("/recursos/avatar_default.png");
            imgMarco = cargarImagen("/recursos/cosmetico_marco.png");
            imgCorona = cargarImagen("/recursos/cosmetico_corona.png");
            imgFondoItem = cargarImagen("/recursos/img_fondo.png"); 
        }
        
        public void setTieneMarco(boolean b) { tieneMarco = b; }
        public void setTieneCorona(boolean b) { tieneCorona = b; }
        public void setTieneFondo(boolean b) { tieneFondo = b; }

        private Image cargarImagen(String ruta) {
            URL url = getClass().getResource(ruta);
            return (url != null) ? new ImageIcon(url).getImage() : null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int sizeAvatar = 120;
            int x = (getWidth() - sizeAvatar) / 2;
            int y = (getHeight() - sizeAvatar) / 2;
            
            if (tieneFondo && imgFondoItem != null) {
                g2.drawImage(imgFondoItem, x - 10, y - 10, 140, 140, this);
            }

            if (imgBase != null) g2.drawImage(imgBase, x, y, sizeAvatar, sizeAvatar, this);
            
            if (tieneMarco && imgMarco != null) {
                g2.drawImage(imgMarco, x - 15, y - 15, 150, 150, this);
            }
            
            if (tieneCorona && imgCorona != null) {
                g2.drawImage(imgCorona, x + 30, y - 40, 60, 60, this);
            }
        }
    }
}