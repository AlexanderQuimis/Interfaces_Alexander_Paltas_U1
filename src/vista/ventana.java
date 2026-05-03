package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import javax.swing.RowFilter;
import javax.swing.ImageIcon;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.GridLayout;

public class ventana extends JFrame {
    
	public JPanel contentPane; // Panel principal que contendrá todos los componentes de la interfaz.
	public JTextField txt_nombres; // Campo de texto para ingresar nombres.
	public JTextField txt_telefono; // Campo de texto para ingresar números de teléfono.
	public JTextField txt_email; // Campo de texto para ingresar direcciones de correo electrónico.
	public JTextField txt_buscar; // Campo de texto adicional.
	public JCheckBox chb_favorito; // Casilla de verificación para marcar un contacto como favorito.
	public JComboBox cmb_categoria; // Menú desplegable para seleccionar la categoría de contacto.
	public JButton btn_add; // Botón para agregar un nuevo contacto.
	public JButton btn_modificar; // Botón para modificar un contacto existente.
	public JButton btn_eliminar; // Botón para eliminar un contacto.
        public JButton btn_exportar;
	public JScrollPane scrLista; // Panel de desplazamiento para la lista de contactos.
        public JTable tabla;
        public DefaultTableModel modeloTabla;
        public TableRowSorter<DefaultTableModel> sorter;
        public JProgressBar progressBar;
        private ResourceBundle mensajes;
        private Locale idiomaActual;
        private JLabel lbl1, lbl2, lbl3, lbl4;
        private JLabel lblIdioma;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 // Invoca el método invokeLater de la clase EventQueue para ejecutar la creación de la interfaz de usuario en un hilo de despacho de eventos (Event Dispatch Thread).
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Dentro de este método, se crea una instancia de la clase ventana, que es la ventana principal de la aplicación.
	                ventana frame = new ventana();
	                // Establece la visibilidad de la ventana como verdadera, lo que hace que la ventana sea visible para el usuario.
	                frame.setVisible(true);
	            } catch (Exception e) {
	                // En caso de que ocurra una excepción durante la creación o visualización de la ventana, se imprime la traza de la pila de la excepción.
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public ventana() {
                idiomaActual = new Locale("es"); // idioma por defecto
                mensajes = ResourceBundle.getBundle("recursos.idiomas.mensajes", idiomaActual);
                
                Color fondo = new Color(245, 247, 250);     // fondo general
                Color primario = new Color(33, 150, 243);   // azul
                Color secundario = new Color(76, 175, 80);  // verde
                Color texto = new Color(33, 33, 33);        // gris oscuro
                
                Font tituloFont = new Font("Arial", Font.BOLD, 16);
                Font textoFont = new Font("Arial", Font.PLAIN, 13);
                
		setTitle(mensajes.getString("titulo"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana.
		setBounds(100, 100, 1026, 748); // Establece el tamaño y la posición inicial de la ventana
                contentPane = new JPanel(new BorderLayout());
                contentPane.setBackground(fondo);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Establece un borde vacío alrededor del panel.
		setContentPane(contentPane); // Establece el panel de contenido como el panel principal de la ventana.
                
                JTabbedPane tabs = new JTabbedPane();
                contentPane.add(tabs, BorderLayout.CENTER);
                
                // Panel CONTACTOS
                JPanel panelContactos = new JPanel(new BorderLayout(10, 10)); //Nuevo Layout
                panelContactos.setBackground(fondo); //Fondo
                tabs.addTab("Contactos", panelContactos);
                
                //Componentes del panel contactos.
                lbl1 = new JLabel(mensajes.getString("nombre"));
                lbl2 = new JLabel(mensajes.getString("telefono"));
                lbl3 = new JLabel(mensajes.getString("email"));
                lbl4 = new JLabel(mensajes.getString("buscar"));
                //Estilo de tipografia de texto principal
                lbl1.setFont(tituloFont);
                lbl2.setFont(tituloFont);
                lbl3.setFont(tituloFont);
                lbl4.setFont(tituloFont);
                //Estilo de texto secundario
                lbl1.setForeground(texto);
                lbl2.setForeground(texto);
                lbl3.setForeground(texto);
                lbl4.setForeground(texto);
                
                txt_nombres = new JTextField();
                txt_telefono = new JTextField();
                txt_email = new JTextField();
                txt_buscar = new JTextField();

                //Estilo de tipografia de texto secundario
                txt_nombres.setFont(textoFont);
                txt_telefono.setFont(textoFont);
                txt_email.setFont(textoFont);
                txt_buscar.setFont(textoFont);
                
                chb_favorito = new JCheckBox(mensajes.getString("favorito"));
                chb_favorito.setFont(textoFont);
                chb_favorito.setBackground(fondo);

                cmb_categoria = new JComboBox<>();
                cargarCategorias();

                // 🔹 Botones
                btn_add = new JButton(mensajes.getString("agregar"));
                btn_modificar = new JButton(mensajes.getString("modificar"));
                btn_eliminar = new JButton(mensajes.getString("eliminar"));
                btn_exportar = new JButton(mensajes.getString("exportar"));

                btn_add.setIcon(new ImageIcon(getClass().getResource("/recursos/iconos/agregar.png")));
                btn_modificar.setIcon(new ImageIcon(getClass().getResource("/recursos/iconos/editar.png")));
                btn_eliminar.setIcon(new ImageIcon(getClass().getResource("/recursos/iconos/eliminar.png")));
                btn_exportar.setIcon(new ImageIcon(getClass().getResource("/recursos/iconos/exportar.png")));

                JButton[] botones = {btn_add, btn_modificar, btn_eliminar, btn_exportar};

                for (JButton b : botones) {
                    b.setFont(textoFont);
                    b.setForeground(Color.WHITE);
                    b.setHorizontalTextPosition(JButton.CENTER);
                    b.setVerticalTextPosition(JButton.BOTTOM);
                }

                btn_add.setBackground(primario);
                btn_modificar.setBackground(primario);
                btn_eliminar.setBackground(new Color(244, 67, 54));
                btn_exportar.setBackground(secundario);

                progressBar = new JProgressBar();
                progressBar.setForeground(secundario);
                progressBar.setStringPainted(true);

                // 🔹 Panel Form
                JComboBox<String> cmb_idioma = new JComboBox<>(new String[]{"ES", "EN", "FR"});

                JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
                panelForm.setBackground(fondo);

                panelForm.add(lbl1); panelForm.add(txt_nombres);
                panelForm.add(lbl2); panelForm.add(txt_telefono);
                panelForm.add(lbl3); panelForm.add(txt_email);
                panelForm.add(chb_favorito); panelForm.add(cmb_categoria);
                lblIdioma = new JLabel(mensajes.getString("idioma"));
                lblIdioma.setFont(tituloFont);
                lblIdioma.setForeground(texto);

                panelForm.add(lblIdioma);
                panelForm.add(cmb_idioma);

                // 🔹 Panel Botones
                JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
                panelBotones.setBackground(fondo);

                panelBotones.add(btn_add);
                panelBotones.add(btn_modificar);
                panelBotones.add(btn_eliminar);
                panelBotones.add(btn_exportar);

                // 🔹 Tabla
                modeloTabla = new DefaultTableModel();
                modeloTabla.setColumnIdentifiers(new String[]{
                    mensajes.getString("nombre"),
                    mensajes.getString("telefono"),
                    mensajes.getString("email"),
                    mensajes.getString("categoria"),
                    mensajes.getString("favorito")
                });

                tabla = new JTable(modeloTabla);
                tabla.setFont(textoFont);
                tabla.setRowHeight(25);

                tabla.getTableHeader().setFont(tituloFont);
                tabla.getTableHeader().setBackground(primario);
                tabla.getTableHeader().setForeground(Color.WHITE);

                sorter = new TableRowSorter<>(modeloTabla);
                tabla.setRowSorter(sorter);

                JScrollPane scrollTabla = new JScrollPane(tabla);

                // 🔹 Panel búsqueda
                JPanel panelBusqueda = new JPanel(new BorderLayout(10, 10));
                panelBusqueda.setBackground(fondo);
                panelBusqueda.add(lbl4, BorderLayout.WEST);
                panelBusqueda.add(txt_buscar, BorderLayout.CENTER);

                JPanel panelSur = new JPanel(new BorderLayout());
                panelSur.add(panelBusqueda, BorderLayout.NORTH);
                panelSur.add(progressBar, BorderLayout.SOUTH);

                // 🔹 Armado final
                panelContactos.add(panelForm, BorderLayout.NORTH);
                panelContactos.add(scrollTabla, BorderLayout.CENTER);
                panelContactos.add(panelBotones, BorderLayout.EAST);
                panelContactos.add(panelSur, BorderLayout.SOUTH);

                // 🔹 Cambio idioma
                cmb_idioma.addActionListener(e -> {
                    String sel = cmb_idioma.getSelectedItem().toString();

                    switch (sel) {
                        case "EN": idiomaActual = new Locale("en"); break;
                        case "FR": idiomaActual = new Locale("fr"); break;
                        default: idiomaActual = new Locale("es");
                    }

                    mensajes = ResourceBundle.getBundle("recursos.idiomas.mensajes", idiomaActual);
                    actualizarTextos();
                });

                // 🔹 Buscador
                txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent e) {
                        String texto = txt_buscar.getText();
                        sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
                    }
                });

                new logica_ventana(this);
            }

    // 🔹 Método de actualización
    private void actualizarTextos() {
        setTitle(mensajes.getString("titulo"));

        lbl1.setText(mensajes.getString("nombre"));
        lbl2.setText(mensajes.getString("telefono"));
        lbl3.setText(mensajes.getString("email"));
        lbl4.setText(mensajes.getString("buscar"));
         lblIdioma.setText(mensajes.getString("idioma"));

        btn_add.setText(mensajes.getString("agregar"));
        btn_modificar.setText(mensajes.getString("modificar"));
        btn_eliminar.setText(mensajes.getString("eliminar"));
        btn_exportar.setText(mensajes.getString("exportar"));

        chb_favorito.setText(mensajes.getString("favorito"));
        
        modeloTabla.setColumnIdentifiers(new String[]{
            mensajes.getString("nombre"),
            mensajes.getString("telefono"),
            mensajes.getString("email"),
            mensajes.getString("categoria"),
            mensajes.getString("favorito")
        });

        cargarCategorias();
    }

    private void cargarCategorias() {
        cmb_categoria.removeAllItems();
        cmb_categoria.addItem(mensajes.getString("categoria"));
        cmb_categoria.addItem(mensajes.getString("familia"));
        cmb_categoria.addItem(mensajes.getString("amigos"));
        cmb_categoria.addItem(mensajes.getString("trabajo"));
    }
}