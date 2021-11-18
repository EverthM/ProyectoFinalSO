package ProyectoFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;

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
	File archivo=null;

	// Metodo constructor de la clase con sus respectivas inicializaciones
	public PrincipalPrograma() {
		Memoria = new MemoriaRAM[32];
		TiempoReal = new ArrayList<ModeloProceso>();
		Usuario1 = new ArrayList<ModeloProceso>();
		Usuario2 = new ArrayList<ModeloProceso>();
		Usuario3 = new ArrayList<ModeloProceso>();
		Todo = new ArrayList<String>();

		acciones();

	}// Constructor

	// Metodo de de acciones que se muestran en la ventana grafica
	public void acciones() {
		vp = new JFrameVentanaPrincipal(Memoria); // Muestra en ventana la memoria

		vp.iniciar.addActionListener(ActionEvent -> { // Para motrar la inicializacion de los procesos

			Contador = 0;
			limite = 0;

			procesador = new Procesador(TiempoReal, Usuario1, Usuario2, Usuario3, vp);
			procesador.execute();
			inicializadorProceso();

		});// FIN ACCION

		// Muestra los datos de los procesos en la consola
		vp.log.addActionListener(ActionEvent -> {
			vp.consola.setText(vp.logg);

		});
		vp.limpiar.addActionListener(ActionEvent -> {
			vp.consola.setText("");
			for (int i = 0; i < 4; i++) {
				vp.proF[i] = 0;
				vp.procesosF[i].setText(vp.tit[i] + vp.proF[i]);
			}

		});

		vp.selectFile.addActionListener(ActionEvent -> {
			JFileChooser fileChooser = new JFileChooser();
			int seleccion = fileChooser.showOpenDialog(vp.selectFile);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				archivo = fileChooser.getSelectedFile();
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
				linea = Files.readAllLines(Paths.get(archivo.getAbsolutePath())).get(Contador); // LECTURA LAS LINEAS DE PROCESOS
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
