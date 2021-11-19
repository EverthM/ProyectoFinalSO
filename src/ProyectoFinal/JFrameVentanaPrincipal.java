package ProyectoFinal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JFrameVentanaPrincipal extends JFrame {// CLASE VENTANA

	private static final long serialVersionUID = 1L;

	int[] rgb;// ARREGLO PARA LOS COLORES DE LA MEMORIA
	JButton iniciar, log, recargar, selectFile;// BOTONES PARA LA INTERFAZ
	JPanel leyenda, panelRAM, RAM;// PANELES DE LA INTERFAZ
	Color color[] = { Color.RED, Color.white };// COLORES FIJOS DE LA INTERFAZ
	MemoriaRAM[] Memoria;// ARREGLO DE LA CLASE RAM
	JTextArea consola;// AREA DE TEXTO PARA LA VISUALIZACION DE LOS PROCESOS
	String logg = "";
	JLabel msg;
	DefaultTableModel modelo;
	JTable tabla;

	public JFrameVentanaPrincipal(MemoriaRAM[] ram) {// CONSTRUCTOR DE LA CLASE

		// INICIALIZAMOS
		rgb = new int[3];
		Memoria = ram;
		setSize(1500, 900);// DAMOS TAMA�O DE LA VENTANA
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new FlowLayout());

		initComp();// LLAMAMOS AL METODO

		setVisible(true);
	}

	public void initComp() { // METODO DONDE SE CREA LA INTERFAZ
		// CREAMOS UN PANEL PRINCIPAL
		JPanel panelConsola = new JPanel();
		panelConsola.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		panelConsola.setPreferredSize(new Dimension(400, 600));
		// getContentPane().setBackground(Color.black);
		// PANEL PARA LA BARRA MEMORIA RAM
		panelRAM = new JPanel();
		panelRAM.setLayout(new FlowLayout());
		panelRAM.setPreferredSize(new Dimension(this.getSize().width, 100));

		// PANEL PARA VER LA LEYENDA DE PROCESOS
		leyenda = new JPanel();
		leyenda.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
		leyenda.setPreferredSize(new Dimension(300, 45));

		// PANEL DONDE IRA LA BARRA DE LA MEMORIA RAM
		RAM = new JPanel();
		RAM.setPreferredSize(new Dimension(this.getWidth() - 20, 40));
		RAM.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		RAM.setBorder(BorderFactory.createTitledBorder("Memoria RAM"));

		// BOTON INICIAR SIMULAICON
		iniciar = new JButton("Iniciar simulacion");
		iniciar.setEnabled(false);

		// BOTON SELECIONAR ARCHIVO
		selectFile = new JButton("Selecione el archivo");
		selectFile.setBackground(Color.red.darker());
		selectFile.setForeground(Color.white);

		// ETIQUETAS PARA LOS CUADRITOS DONDE INDICA LA MEMORIA LIBRE Y RESERVADA
		JLabel cuadritos[];
		cuadritos = new JLabel[2];
		String datos[] = { "Memoria reservada", "Memoria Libre" };

		for (int i = 0; i < 2; i++) {
			cuadritos[i] = new JLabel(new ImageIcon("iconos/cuadro.png"));// CUADRO COLOR ROJO
			cuadritos[i].setBackground(color[i]);// CUADRO COLOR BLANCO
			cuadritos[i].setOpaque(true);// SE HACE OPACO

			JLabel j = new JLabel(datos[i]);
			leyenda.add(cuadritos[i]);
			leyenda.add(j);
		} // Fin for

		// CREAMOS OTRO PANEL LLAMADO INFO
		JPanel panelControl = new JPanel();
		panelControl.setPreferredSize(new Dimension(600, 30));
		panelControl.setLayout(new GridLayout(1, 4, 40, 0));

		panelControl.setBorder(BorderFactory.createEtchedBorder());

		panelControl.add(selectFile);

		/// CREAMOS UN AREA DE TEXTO
		consola = new JTextArea();
		consola.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		consola.setLineWrap(true);

		JScrollPane scr = new JScrollPane(consola);
		scr.setPreferredSize(new Dimension(250, 450));

		// BOTON REGISTRO
		log = new JButton("Registro");
		// BOTON PARA LIMPIAR Todo
		recargar = new JButton("Recargar procesos");
		recargar.setEnabled(false);
		

		modelo = new DefaultTableModel();
		

		String[] NombresDeColumnas = { "ID", "Estatus", "T. Llegada", "Prioridad inicial", "Prioridad", "T. Requerido", "T. Restante",
				"RAM", "Ubicacion", "Impresoras pedidos", "Impresoras dados", "Escaneres pedidos",
				"Escaneres dados", "Modens pedidos", "Modens dados", "CDs pedidos", "CDs dados" };

		for (int i = 0; i < 17; i++) {
			modelo.addColumn(NombresDeColumnas[i]);
		}

		tabla = new JTable(modelo);
		tabla.getColumn("ID").setPreferredWidth(15);   
		tabla.getColumn("Ubicacion").setPreferredWidth(45);
		tabla.getColumn("Estatus").setPreferredWidth(45);
		tabla.getColumn("Prioridad inicial").setPreferredWidth(55);
		tabla.getColumn("Prioridad").setPreferredWidth(30);
		tabla.getColumn("CDs pedidos").setPreferredWidth(50);
		tabla.getColumn("CDs dados").setPreferredWidth(40);
		tabla.getColumn("RAM").setPreferredWidth(15);
		tabla.getColumn("Modens pedidos").setPreferredWidth(60);
		tabla.getColumn("Modens dados").setPreferredWidth(54);
		tabla.getColumn("T. Llegada").setPreferredWidth(54);
		tabla.getColumn("T. Restante").setPreferredWidth(54);
		tabla.getColumn("T. Requerido").setPreferredWidth(54);
		
		JScrollPane scrTabla = new JScrollPane(tabla);
		scrTabla.setPreferredSize(new Dimension(1400, 600));

        msg= new JLabel("CARGUE E INICIE LOS PROCESOS");
		msg.setLocation(300, 500);

		add(msg);

		panelConsola.add(scr);
		panelControl.add(iniciar);
		// panelControl.add(log);
		panelControl.add(recargar);
		panelRAM.add(RAM);
		panelRAM.add(leyenda);
		add(panelRAM);
		add(panelControl);
		add(scrTabla);

		inicializarRAM();// LLAMAMOS AL METODO INICIALIZAR RAM

		JDialog dialog = new JDialog();
		dialog.setLayout(new FlowLayout());
		dialog.setSize(new Dimension(500, 600));
		dialog.add(scr);
		dialog.setVisible(true);
	}// Fin

	public void inicializarRAM() {// METODO INICIARLIZAR RAM

		// FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA
		// PROCESOS DE TIEMPO REAL
		for (int i = 0; i < 2; i++) {
			Memoria[i] = new MemoriaRAM(false, 0, i);
			Memoria[i].Barra.setBackground(Color.red);
			Memoria[i].Barra.setOpaque(true);
			Memoria[i].Barra.setText("" + i);
			Memoria[i].Barra.setPreferredSize(new Dimension(30, 15));
			RAM.add(Memoria[i].Barra);
		}

		// FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA
		// PROCESOS DE USUARIO
		for (int i = 2; i < 32; i++) {

			Memoria[i] = new MemoriaRAM(false, 0, i);
			Memoria[i].Barra.setBackground(Color.white);
			Memoria[i].Barra.setOpaque(true);
			Memoria[i].Barra.setText("" + i);
			Memoria[i].Barra.setPreferredSize(new Dimension(30, 15));
			RAM.add(Memoria[i].Barra);

		} // Fin for
	}// Fin inicializarRAM

}// Fin clas
