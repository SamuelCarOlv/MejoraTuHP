package interfaz;

import logica.*;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private ControladorJuego controlador;

    public VentanaLogin() {
        controlador = new ControladorJuego();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("MejoraTuHP - Acceso");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}

        JLabel lblTitulo = new JLabel("MejoraTuHP", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(0, 20, 400, 30);
        add(lblTitulo);

        add(crearLabel("Correo:", 50, 70));
        txtCorreo = new JTextField();
        txtCorreo.setBounds(50, 95, 280, 30);
        add(txtCorreo);

        add(crearLabel("Contraseña:", 50, 135));
        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 160, 280, 30);
        add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(50, 220, 130, 40);
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        add(btnLogin);

        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setBounds(200, 220, 130, 40);
        add(btnRegistro);

        btnLogin.addActionListener(e -> {
            if (controlador.iniciarSesion(txtCorreo.getText(), new String(txtPassword.getPassword()))) {
                this.dispose();
                new TableroPrincipal(controlador).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }
        });

        btnRegistro.addActionListener(e -> {
            JPanel p = new JPanel(new GridLayout(3, 2));
            JTextField tNom = new JTextField();
            JTextField tCor = new JTextField();
            JTextField tPas = new JTextField();
            p.add(new JLabel("Nombre:")); p.add(tNom);
            p.add(new JLabel("Correo:")); p.add(tCor);
            p.add(new JLabel("Contraseña:")); p.add(tPas);
            
            if (JOptionPane.showConfirmDialog(this, p, "Nuevo Usuario", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (controlador.registrarUsuario(tNom.getText(), tCor.getText(), tPas.getText())) {
                    JOptionPane.showMessageDialog(this, "¡Creado! Inicia sesión.");
                }
            }
        });
    }

    private JLabel crearLabel(String texto, int x, int y) {
        JLabel l = new JLabel(texto);
        l.setBounds(x, y, 200, 20);
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}