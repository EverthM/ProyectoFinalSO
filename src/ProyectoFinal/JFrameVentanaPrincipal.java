package ProyectoFinal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JFrameVentanaPrincipal extends JFrame {// CLASE VENTANA

	private static final long serialVersionUID = 1L;

	
	JButton iniciar, log, recargar, selectFile;// BOTONES PARA LA INTERFAZ
	JTextArea consola;// AREA DE TEXTO PARA LA VISUALIZACION DE LOS PROCESOS
	String logg = "";
	JLabel msg;

	DefaultTableModel modelo;
	JTable tabla;

	JDialog MemoriaRAM;
	int[] rgb;// ARREGLO PARA LOS COLORES DE LA MEMORIA
	MemoriaRAM[] Memoria;// ARREGLO DE LA CLASE RAM
	JPanel panelRAM, RAM;// PANELES DE LA INTERFAZ

	JLabel cantidadPrinter, cantidadScanner, cantidadModem, cantidadCD;
	

	public JFrameVentanaPrincipal(MemoriaRAM[] ram) {// CONSTRUCTOR DE LA CLASE

		// INICIALIZAMOS
		rgb = new int[3];
		Memoria = ram;
		setSize(1500, 915);// Screen size
		setTitle("SSOP - Sistemas operativos");
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.pink);
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

		initComp();// LLAMAMOS AL METODO

		setVisible(true);
	}

	public void initComp() { // METODO DONDE SE CREA LA INTERFAZ



		// CREAMOS UN PANEL PRINCIPAL
		JPanel dispositivos = new JPanel();
		dispositivos.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
		dispositivos.setPreferredSize(new Dimension(800, 160));
		//dispositivos.setLayout(new GridLayout(2,4));
		dispositivos.setOpaque(false);

		JLabel printer = new JLabel(new ImageIcon("iconos/printer.png"));
		printer.setPreferredSize(new Dimension(200, 100));
		JLabel scanner = new JLabel(new ImageIcon("iconos/scanner.png"));
		scanner.setPreferredSize(new Dimension(200, 100));
		JLabel modem = new JLabel(new ImageIcon("iconos/modem.png"));
		modem.setPreferredSize(new Dimension(200, 100));
		JLabel cd = new JLabel(new ImageIcon("iconos/cd.png"));
		cd.setPreferredSize(new Dimension(200, 100));

		cantidadPrinter = new JLabel("Disponible: 2");
		cantidadPrinter.setHorizontalAlignment(SwingConstants.CENTER);
		cantidadPrinter.setPreferredSize(new Dimension(200, 20));
		cantidadPrinter.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));
		cantidadScanner = new JLabel("Disponible: 1");
		cantidadScanner.setHorizontalAlignment(SwingConstants.CENTER);
		cantidadScanner.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));
		cantidadScanner.setPreferredSize(new Dimension(200, 20));
		cantidadModem = new JLabel("Disponible: 1");
		cantidadModem.setHorizontalAlignment(SwingConstants.CENTER);
		cantidadModem.setPreferredSize(new Dimension(200, 20));
		cantidadModem.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));
		cantidadCD = new JLabel("Disponible: 2");
		cantidadCD.setHorizontalAlignment(SwingConstants.CENTER);
		cantidadCD.setPreferredSize(new Dimension(200, 20));
		cantidadCD.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));

		dispositivos.add(printer);
		dispositivos.add(scanner);
		dispositivos.add(modem);
		dispositivos.add(cd);
		dispositivos.add(cantidadPrinter);
		dispositivos.add(cantidadScanner);
		dispositivos.add(cantidadModem);
		dispositivos.add(cantidadCD);
		
		// PANEL PARA LA BARRA MEMORIA RAM
		panelRAM = new JPanel();
		panelRAM.setLayout(new FlowLayout());
		panelRAM.setPreferredSize(new Dimension(300, 300));
		panelRAM.setBackground(Color.pink);

		// PANEL DONDE IRA LA BARRA DE LA MEMORIA RAM
		RAM = new JPanel();
		RAM.setPreferredSize(new Dimension(246, 245));
		RAM.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		RAM.setBorder(BorderFactory.createEtchedBorder());
		RAM.setBackground(Color.pink);
		// BOTON INICIAR SIMULAICON
		iniciar = new JButton("Iniciar simulacion");
		iniciar.setPreferredSize(new Dimension(200,40));
		iniciar.setEnabled(false);
		iniciar.setFont(new Font("Arial", Font.CENTER_BASELINE, 13));
		iniciar.setBackground(Color.green);

		// BOTON SELECIONAR ARCHIVO
		selectFile = new JButton("Selecione el archivo");
		selectFile.setBackground(Color.red.darker());
		selectFile.setForeground(Color.white);
		selectFile.setPreferredSize(new Dimension(200,40));
		selectFile.setFont(new Font("Arial", Font.CENTER_BASELINE, 13));

		// CREAMOS OTRO PANEL LLAMADO INFO
		JPanel panelControl = new JPanel();
		panelControl.setPreferredSize(new Dimension(1400, 40));
		panelControl.setLayout(new GridLayout(1, 4, 40, 0));
		panelControl.setOpaque(false);
		panelControl.setBorder(BorderFactory.createEtchedBorder());
	

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
		recargar.setPreferredSize(new Dimension(200,40));
		recargar.setEnabled(false);
		recargar.setFont(new Font("Arial", Font.CENTER_BASELINE, 13));
		recargar.setBackground(Color.orange);

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
		scrTabla.setPreferredSize(new Dimension(this.getWidth(), 610));

		

        msg= new JLabel("CARGUE E INICIE LOS PROCESOS");
		msg.setHorizontalAlignment(SwingConstants.CENTER);
		msg.setFont(new Font("Arial", Font.BOLD, 16));
		msg.setLocation(300, 500);
		

		panelControl.add(msg);
        panelControl.add(selectFile);
		panelControl.add(iniciar);
		panelControl.add(recargar);
		
		add(panelControl);
		add(dispositivos);
		add(scrTabla);

		inicializarRAM();// LLAMAMOS AL METODO INICIALIZAR RAM

		JDialog dialog = new JDialog();
		dialog.setLayout(new FlowLayout());
		dialog.setSize(new Dimension(500, 600));
		dialog.add(scr);
		
		//dialog.setVisible(true);

        
		MemoriaRAM = new JDialog();
		MemoriaRAM.setTitle("RAM");
		MemoriaRAM.setSize(new Dimension(290, 292));
		MemoriaRAM.setLayout(new BorderLayout());
		MemoriaRAM.setLocation(1320, 0);
		
		panelRAM.add(RAM);
		MemoriaRAM.add(panelRAM, BorderLayout.CENTER);

		
	}// Fin
//
	public void inicializarRAM() {// METODO INICIARLIZAR RAM

		// FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA
		// PROCESOS DE TIEMPO REAL
		for (int i = 0; i < 2; i++) {
			Memoria[i] = new MemoriaRAM(false, 0, i);
			Memoria[i].Barra.setBackground(Color.black);
			Memoria[i].Barra.setForeground(Color.white);
			Memoria[i].Barra.setOpaque(true);
			Memoria[i].Barra.setText("R-" + i);
			Memoria[i].Barra.setHorizontalAlignment(SwingConstants.CENTER);
			Memoria[i].Barra.setPreferredSize(new Dimension(40, 40));
			RAM.add(Memoria[i].Barra);
		}
//
		// FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA
		// PROCESOS DE USUARIO
		for (int i = 2; i < 32; i++) {

			Memoria[i] = new MemoriaRAM(false, 0, i);
			Memoria[i].Barra.setBackground(Color.white);
			Memoria[i].Barra.setOpaque(true);
			Memoria[i].Barra.setText("" + i);
			Memoria[i].Barra.setHorizontalAlignment(SwingConstants.CENTER);
			Memoria[i].Barra.setPreferredSize(new Dimension(40, 40));
			RAM.add(Memoria[i].Barra);

		} // Fin for
	}// Fin inicializarRAM

}// Fin clas
