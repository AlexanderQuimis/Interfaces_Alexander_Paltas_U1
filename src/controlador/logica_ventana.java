package controlador;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import javax.swing.SwingWorker;
import vista.ventana;
import modelo.*;

//Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria=""; // Variables para almacenar datos del contacto.
	private persona persona; // Objeto de tipo persona, que representa un contacto.
	private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false; // Booleano que indica si un contacto es favorito.

	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
		  // Asigna la ventana recibida como parámetro a la variable de instancia delegado.
	    this.delegado = delegado;
	    // Carga los contactos almacenados al inicializar.
	    cargarContactosRegistrados(); 
	    // Registra los ActionListener para los botones de la GUI.
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    // Registra los ListSelectionListener para la lista de contactos.
            this.delegado.tabla.getSelectionModel().addListSelectionListener(this);
	    // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);
            this.delegado.btn_exportar.addActionListener(this);
	}

	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void incializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
		 try {
		        // Lee los contactos almacenados utilizando una instancia de personaDAO.
		        contactos = new personaDAO(new persona()).leerArchivo();
		        DefaultTableModel modelo = delegado.modeloTabla;
		        modelo.setRowCount(0); // limpiar tabla
		        
                        for (persona contacto : contactos) {
                            modelo.addRow(new Object[]{
                                contacto.getNombre(),
                                contacto.getTelefono(),
                                contacto.getEmail(),
                                contacto.getCategoria(),
                                contacto.isFavorito()
                        });
                    }
		    } catch (IOException e) {
		        // Muestra un mensaje de error si ocurre una excepción al cargar los contactos.
		        JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		    }
	}


	// Método privado para limpiar los campos de entrada en la GUI y reiniciar variables.
	private void limpiarCampos() {
		// Limpia los campos de nombres, email y teléfono en la GUI.
	    delegado.txt_nombres.setText("");
	    delegado.txt_telefono.setText("");
	    delegado.txt_email.setText("");
	    // Reinicia las variables de categoría y favorito.
	    categoria = "";
	    favorito = false;
	    // Desmarca la casilla de favorito y establece la categoría por defecto.
	    delegado.chb_favorito.setSelected(favorito);
	    delegado.cmb_categoria.setSelectedIndex(0);
	    // Reinicia las variables con los valores actuales de la GUI.
	    incializacionCampos();
	    // Recarga los contactos en la lista de contactos de la GUI.
	    cargarContactosRegistrados();
	}

	// Método que maneja los eventos de acción (clic) en los botones.
	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos(); // Inicializa las variables con los valores actuales de la GUI.

	    // Verifica si el evento proviene del botón "Agregar".
	    if (e.getSource() == delegado.btn_add) {
	        // Verifica si los campos de nombres, teléfono y email no están vacíos.
	        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
	            // Verifica si se ha seleccionado una categoría válida.
	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
	                // Crea un nuevo objeto persona con los datos ingresados y lo guarda.
	                persona = new persona(nombres, telefono, email, categoria, favorito);
	                new personaDAO(persona).escribirArchivo();
	                // Limpia los campos después de agregar el contacto.
	                limpiarCampos();
	                // Muestra un mensaje de éxito.
	                JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
	            } else {
	                // Muestra un mensaje de advertencia si no se ha seleccionado una categoría válida.
	                JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
	            }
	        } else {
	            // Muestra un mensaje de advertencia si algún campo está vacío.
	            JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
	        }
	    } else if (e.getSource() == delegado.btn_eliminar) {
	        // Lugar para implementar la funcionalidad de eliminar un contacto.
	    } else if (e.getSource() == delegado.btn_modificar) {
	        // Lugar para implementar la funcionalidad de modificar un contacto.
	    }else if (e.getSource() == delegado.btn_exportar) {
            exportarCSV();
        }
}

	// Método que maneja los eventos de selección en la lista de contactos.
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = delegado.tabla.getSelectedRow();

                    if (fila != -1) {
                        cargarContacto(fila);
                    }
                }
            }
            
            
        private void exportarCSV() {
               if (contactos == null || contactos.isEmpty()) {
                JOptionPane.showMessageDialog(delegado, "No hay contactos para exportar");
                return;
            }   
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {

                    FileWriter writer = new FileWriter("contactos.csv");

                    int total = contactos.size();

                    delegado.progressBar.setMaximum(total);
                    delegado.progressBar.setValue(0);

                    writer.write("Nombre,Telefono,Email,Categoria,Favorito\n");

                    for (int i = 0; i < total; i++) {
                        persona p = contactos.get(i);

                        writer.write(
                            p.getNombre() + "," +
                            p.getTelefono() + "," +
                            p.getEmail() + "," +
                            p.getCategoria() + "," +
                            p.isFavorito() + "\n"
                        );

                        publish(i + 1); // envía progreso
                    }

                    writer.close();
                    return null;
                }
                @Override
                protected void process(List<Integer> chunks) {
                    int valor = chunks.get(chunks.size() - 1);
                    delegado.progressBar.setValue(valor);
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(delegado, "Exportación completada");
                }
            };

            worker.execute();
        }

	// Método privado para cargar los datos del contacto seleccionado en los campos de la GUI.
	private void cargarContacto(int index) {
		// Establece el nombre del contacto en el campo de texto de nombres.
	    delegado.txt_nombres.setText(contactos.get(index).getNombre());
	    // Establece el teléfono del contacto en el campo de texto de teléfono.
	    delegado.txt_telefono.setText(contactos.get(index).getTelefono());
	    // Establece el correo electrónico del contacto en el campo de texto de correo electrónico.
	    delegado.txt_email.setText(contactos.get(index).getEmail());
	    // Establece el estado de favorito del contacto en el JCheckBox de favorito.
	    delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
	    // Establece la categoría del contacto en el JComboBox de categoría.
	    delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	// Método que maneja los eventos de cambio de estado en los componentes cmb_categoria y chb_favorito.
	@Override
        public void itemStateChanged(ItemEvent e) {

            if (e.getSource() == delegado.cmb_categoria) {

                Object selected = delegado.cmb_categoria.getSelectedItem();

                if (selected == null) return;

                categoria = selected.toString(); // 🔥 aquí guardas bien la variable

            } else if (e.getSource() == delegado.chb_favorito) {

                favorito = delegado.chb_favorito.isSelected();
            }
        }
}