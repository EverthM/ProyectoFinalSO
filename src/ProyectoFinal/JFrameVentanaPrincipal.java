package ProyectoFinal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class JFrameVentanaPrincipal extends JFrame {//CLASE VENTANA

	private static final long serialVersionUID = 1L;

	int[] rgb;//ARREGLO PARA LOS COLORES DE LA MEMORIA
	JButton iniciar, log, limpiar, selectFile;//BOTONES PARA LA INTERFAZ
	JPanel leyenda, panelRAM, RAM;//PANELES DE LA INTERFAZ
	Color color[] = { Color.RED, Color.white };//COLORES FIJOS DE LA INTERFAZ
	MemoriaRAM[] Memoria;//ARREGLO DE LA CLASE RAM
	JTextArea consola;//AREA DE TEXTO PARA LA VISUALIZACION DE LOS PROCESOS
	String logg = "";
	JLabel procesosF[] = new JLabel[4];//ARREGLO DE ETIQUETAS
	int[] proF = { 0, 0, 0, 0 };//ARREGLO PARA VISUALIZAR NUMERO DE PROCESOS TERMNINADOS
	String[] tit = { "Tiempo real: ", "Usuario 1: ", "Usuario 2: ", "Usuario 3: " };//ARREGLO DE STRING PARA VISUALIZAR QUE PROCESO TERMINO

	public JFrameVentanaPrincipal(MemoriaRAM[] ram) {//CONSTRUCTOR DE LA CLASE
		
		//INICIALIZAMOS 
		rgb = new int[3];
		Memoria = ram;
		setSize(1000, 730);//DAMOS TAMA�O DE LA VENTANA
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new FlowLayout());

		initComp();//LLAMAMOS AL METODO

		setVisible(true);
	}

	
	
	public void initComp() { //METODO DONDE SE CREA LA INTERFAZ
		//CREAMOS UN PANEL PRINCIPAL
		JPanel panelConsola = new JPanel();
		panelConsola.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		panelConsola.setPreferredSize(new Dimension(400, 600));
		//getContentPane().setBackground(Color.black);
		//PANEL PARA LA BARRA MEMORIA RAM
		panelRAM = new JPanel();
		panelRAM.setLayout(new FlowLayout());
		panelRAM.setPreferredSize(new Dimension(this.getSize().width, 100));

		
		//PANEL PARA VER LA LEYENDA DE PROCESOS
		leyenda = new JPanel();
		leyenda.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
		leyenda.setPreferredSize(new Dimension(300, 45));

		
		//PANEL DONDE IRA LA BARRA DE LA MEMORIA RAM
		RAM = new JPanel();
		RAM.setPreferredSize(new Dimension(this.getWidth() - 20, 40));
		RAM.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		RAM.setBorder(BorderFactory.createTitledBorder("Memoria RAM"));

		//BOTON INICIAR SIMULAICON
		iniciar = new JButton("Iniciar simulacion");

		//BOTON SELECIONAR ARCHIVO
		selectFile = new JButton("Selecione el archivo");
		selectFile.setBackground(Color.red.darker());
		selectFile.setForeground(Color.white);

		//ETIQUETAS PARA LOS CUADRITOS DONDE INDICA LA MEMORIA LIBRE Y RESERVADA
		JLabel cuadritos[];
		cuadritos = new JLabel[2];
		String datos[] = { "Memoria reservada", "Memoria Libre" };

		
		for (int i = 0; i < 2; i++) {
			cuadritos[i] = new JLabel(new ImageIcon("iconos/cuadro.png"));//CUADRO COLOR ROJO
			cuadritos[i].setBackground(color[i]);//CUADRO COLOR BLANCO
			cuadritos[i].setOpaque(true);//SE HACE OPACO

			
			JLabel j = new JLabel(datos[i]);
			leyenda.add(cuadritos[i]);
			leyenda.add(j);
		} // Fin for

		
		//CREAMOS OTRO PANEL LLAMADO INFO
		JPanel info = new JPanel();
		info.setPreferredSize(new Dimension(600, 30));
		info.setLayout(new GridLayout(1, 4, 40, 0));
		
		info.setBorder(BorderFactory.createEtchedBorder());
		for (int i = 0; i < 4; i++) {
			procesosF[i] = new JLabel();
			procesosF[i].setText(tit[i] + proF[i]);
			info.add(procesosF[i]);
		} // Fin

		///CREAMOS UN AREA DE TEXTO 
		consola = new JTextArea();
		consola.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		consola.setLineWrap(true);
		
		JScrollPane scr = new JScrollPane(consola);
		scr.setPreferredSize(new Dimension(250, 450));
	

		//BOTON REGISTRO
		log = new JButton("Registro");
		//BOTON PARA LIMPIAR Todo
		limpiar = new JButton("Limpiar todo");

		JLabel comp = new JLabel("Procesos completados >");//ETIQUETA QUE INDICATIVA

		panelConsola.add(scr);
		panelConsola.add(iniciar);
		panelConsola.add(log);
		panelConsola.add(limpiar);
		panelRAM.add(RAM);
		panelRAM.add(leyenda);
		panelConsola.setBorder(BorderFactory.createEtchedBorder());
		add(panelRAM);
		add(comp);
		add(info);
		add(panelConsola);
		add(selectFile);

		inicializarRAM();//LLAMAMOS AL METODO INICIALIZAR RAM
	}// Fin

	public void inicializarRAM() {//METODO INICIARLIZAR RAM

		
		//FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA PROCESOS DE TIEMPO REAL
		for (int i = 0; i < 2; i++) {
			Memoria[i] = new MemoriaRAM(false, 0, i);
			Memoria[i].Barra.setBackground(Color.red);
			Memoria[i].Barra.setOpaque(true);
			Memoria[i].Barra.setText("" + i);
			Memoria[i].Barra.setPreferredSize(new Dimension(30, 15));
			RAM.add(Memoria[i].Barra);
		}

		//FOR PARA ASIGNAR LOS COLORES DE LA RAM Y EN QUE POSICION ESTA OCUPADA PARA PROCESOS DE USUARIO
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
