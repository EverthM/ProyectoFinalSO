package ProyectoFinal;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

//Clase programa en donde se manipula los procesos
public class PrincipalPrograma {

	JFrameVentanaPrincipal vp; // variable de la ventana pricipal
	Procesador procesador; // variable del procesador
	ArrayList<ModeloProceso> TiempoReal, Usuario1, Usuario2, Usuario3; // Arraylist que almacenan los procesos y tambien
																		// las prioridades de estos
	ArrayList<String> Todo; // Arralist de control de en la entrada de los procesos
	MemoriaRAM Memoria[]; // vector de memoria
	int IDS = 1; // Variable del IDS
	int limite = 0; // Variable del limite
	int Contador = 0; // variable del contadorç
	File archivo = null;

	AtomicBoolean simulacionActiva;

	// Metodo constructor de la clase con sus respectivas inicializaciones
	public PrincipalPrograma() {
		Memoria = new MemoriaRAM[32];
		TiempoReal = new ArrayList<ModeloProceso>();
		Usuario1 = new ArrayList<ModeloProceso>();
		Usuario2 = new ArrayList<ModeloProceso>();
		Usuario3 = new ArrayList<ModeloProceso>();
		Todo = new ArrayList<String>();

		simulacionActiva = new AtomicBoolean(false);

		acciones();

	}// Constructor

	// Metodo de de acciones que se muestran en la ventana grafica
	public void acciones() {
		vp = new JFrameVentanaPrincipal(Memoria); // Muestra en ventana la memoria

		vp.iniciar.addActionListener(ActionEvent -> { // Para motrar la inicializacion de los procesos
          if(simulacionActiva.get()){
			  JOptionPane.showMessageDialog(null, "Espere a que la simulacion termine o cierre el programa");
		  }else{

			Contador = 0;
			limite = 0;

			procesador = new Procesador(TiempoReal, Usuario1, Usuario2, Usuario3, vp, simulacionActiva);
			procesador.execute();

			vp.iniciar.setEnabled(false);

			// inicializadorProceso();
		  }
		});// FIN ACCION

		// Muestra los datos de los procesos en la consola
		vp.log.addActionListener(ActionEvent -> {
			vp.consola.setText(vp.logg);

		});
		vp.recargar.addActionListener(ActionEvent -> {
			if (TiempoReal.size() == 0 && Usuario1.size() == 0 && Usuario2.size() == 0 && Usuario3.size() == 0) {

				if (archivo != null) {
					IDS = 1;
					Contador = 0;
					limite = 0;

					inicializadorProceso();
					vp.iniciar.setEnabled(true);
					vp.recargar.setEnabled(false);
				} else
					JOptionPane.showMessageDialog(null, "Primero seleccione un archivo TXT");
			}else {
				JOptionPane.showMessageDialog(null, "Espere a que la simulacion termine");
			}

		});

		vp.selectFile.addActionListener(ActionEvent -> {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
			fileChooser.setFileFilter(filter);
			int seleccion = fileChooser.showOpenDialog(vp.selectFile);

			if (seleccion == JFileChooser.APPROVE_OPTION) {
				archivo = fileChooser.getSelectedFile();
				vp.selectFile.setBackground(Color.green.darker());
				vp.selectFile.setText(archivo.getName());

				if (TiempoReal.size() == 0 && Usuario1.size() == 0 && Usuario2.size() == 0 && Usuario3.size() == 0){
					inicializadorProceso();
					vp.iniciar.setEnabled(true);
					vp.recargar.setEnabled(false);
				}
					
			}
		});
	}// Fin

	public void contar() { // METODO QUE CUENTA LOS PROCESOS DEL TXT
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(archivo); // LECTURA DEL TXT
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		br = new BufferedReader(fr);

		try {
			while (br.readLine() != null || limite == 999) { // CONTADOR PARA PONER UN LIMITE A LA HORA DE LOS PROCESOS
																// TOTALES QUE SE VAN A EJECUTAR
				limite++;
			}
		} catch (IOException e) { // EXCEPTION DEL TRY CATCH

			e.printStackTrace();
		}

	}// Fin contar

	public String abrirProceso() { // METODO PARA ABRIR EL PROCESO DEL TXT

		String linea = "";
		try {

			if (Contador < limite) { // CONDICOON PARA SABER SI EL CONTADOR HA LLEGADO AL LIMITE PERMITIDO DE
										// PROCESOS QUE LEEER
				linea = Files.readAllLines(Paths.get(archivo.getAbsolutePath())).get(Contador); // LECTURA LAS LINEAS DE
																								// PROCESOS
				Contador++; // CONTADOR QUE AUMENTA
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return linea; // RETORNA LINEA

	}// Fin

	public ModeloProceso crearProceso(String datos) { // METODO QUE LEE LAS LINEAS DE LA MANERA DEBIDA PARA CARGAR LOS
														// PROCESOS DEL TXT

		boolean add = true; // VARIABLE BOOLEANA PARA A�ADIR

		// ARRAYLIST PARA ALMACENAR LOS PROCESOS
		ArrayList<String> resStr = new ArrayList<String>();
		ArrayList<Integer> res = new ArrayList<Integer>();
		String aux = "";

		for (int i = 0, o = 0; i < datos.length(); i++) { // FOR PARA LEER LAS LINEAS

			if (Character.isDigit(datos.charAt(i))) { // IF PARA DIFERENCIAR LOS NUMEROS QUE ESTAN DIVIDIDOS POR COMAS

				aux += datos.charAt(i);

				if (add) { // CONDICIONANTE PARA APLICAR A QUE ARRALIST DEBE IR
					resStr.add("");
					add = false;
				} // Fin if

				resStr.set(o, aux);

			} else {
				o++;
				add = true;
				aux = "";
			} // fin else

		} // Fin for

		for (int i = 0; i < resStr.size(); i++) { // FOR PARA RECORRER LA LISTA QUE YA FUERON LEIDAS Y PREPARADAS
			res.add(Integer.parseInt(resStr.get(i)));
		}
		ModeloProceso p = null;
		try {
			p = new ModeloProceso(IDS, res.get(0), res.get(1), res.get(2), res.get(3), res.get(4), res.get(5),
					res.get(6), res.get(7));// Crea el objeto Proceso
			IDS++;

			vp.modelo.addRow(new Object[] { p.ID, "Detenido", p.T_Llegada, p.Pri_Inicial, p.Pri_Actual, p.T_Requerido,
					p.T_Restante, p.Memoria, p.Ubicacion, p.ImpresorasSolicitadas, p.Impresoras, p.ScanneresSolicitado,
					p.Scanneres, p.ModemsSolicitados, p.Modems, p.CDSolicitados, p.CD });

		} catch (Exception e) {
		}

		return p;

	}// Fin

	public void inicializadorProceso() { // METODO QUE INCIALIZA EL PROCESO

		contar(); // LLAMA AL METODO CONTAR

		HiloRevisar r = new HiloRevisar(this); // CREA EL OBJETO DE LA CLASE
		r.start(); // COMIENZA EL METODO STAR

	}
}// Fin clase
