package Paquete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

	//Clase programa en donde se manipula los procesos
public class Programa { 

	VentanaPrincipal vp; //variable de la ventana pricipal
	Procesador procesador; //variable del procesador
	ArrayList<Proceso> TiempoReal, Usuario1, Usuario2, Usuario3; // Arraylist que almacenan los procesos y tambien las prioridades de estos
	ArrayList<String> Todo; //Arralist de control de en la entrada de los procesos
	RAM Memoria[]; //vector de memoria
	int IDS = 1; //Variable del IDS
	int limite=0; //Variable del limite
	int Contador=0; // variable del contador
	
	//Metodo constructor de la clase con sus respectivas inicializaciones
	public Programa() {
		Memoria = new RAM[32];
		TiempoReal = new ArrayList<Proceso>();
		Usuario1 = new ArrayList<Proceso>();
		Usuario2 = new ArrayList<Proceso>();
		Usuario3 = new ArrayList<Proceso>();
		Todo = new ArrayList<String>();
       
		acciones();

	}// Constructor
	
	
	//Metodo de de acciones que se muestran en la ventana grafica
	public void acciones() {
		vp = new VentanaPrincipal(Memoria); //Muestra en ventana la memoria
		

		vp.iniciar.addActionListener(ActionEvent -> { // Para motrar la inicializacion de los procesos
			
			Contador=0;
			limite=0;
			
			procesador = new Procesador(TiempoReal, Usuario1, Usuario2, Usuario3, vp);
			procesador.execute();
			inicializadorProceso();

		});// FIN ACCION
		
		//Muestra los datos de los procesos en la consola
		vp.log.addActionListener(ActionEvent->{
			vp.consola.setText(vp.logg);
			
		});
		vp.limpiar.addActionListener(ActionEvent->{
			vp.consola.setText("");
			for(int i=0; i<4; i++) {
			vp.proF[i]=0;
			vp.procesosF[i].setText(vp.tit[i]+vp.proF[i]);
			}
			
		});
	}// Fin

	public void contar() { // METODO QUE CUENTA LOS PROCESOS DEL TXT
		File archivo = new File("procesos.txt"); //ARCHIVO TXT AL QUE HACE REFERENCIA PARA TRABAJAR 
		FileReader fr = null;
		BufferedReader br=null;
		
		 try {
				fr = new FileReader(archivo); //LECTURA DEL TXT
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        br = new BufferedReader(fr);
	        
	        try {
				while(br.readLine()!=null||limite==999) { //CONTADOR PARA PONER UN LIMITE A LA HORA DE LOS PROCESOS TOTALES QUE SE VAN A EJECUTAR
					limite++;
				}
			} catch (IOException e) { //EXCEPTION DEL TRY CATCH
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	}//Fin contar
	
	
	public String abrirProceso() { //METODO PARA ABRIR EL PROCESO DEL TXT
		
		String linea = "";
		try {
		
			if(Contador<limite) {  //CONDICOON PARA SABER SI EL CONTADOR HA LLEGADO AL LIMITE PERMITIDO DE PROCESOS QUE LEEER
			linea = Files.readAllLines(Paths.get("procesos.txt")).get(Contador); // LECTURA LAS LINEAS DE PROCESOS
				Contador++; //CONTADOR QUE AUMENTA
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return linea; //RETORNA LINEA

	}//Fin 

	public Proceso crearProceso(String datos) { //METODO QUE LEE LAS LINEAS DE LA MANERA DEBIDA PARA CARGAR LOS PROCESOS DEL TXT

		boolean add = true; //VARIABLE BOOLEANA PARA AÑADIR
		
		//ARRAYLIST PARA ALMACENAR LOS PROCESOS
		ArrayList<String> resStr = new ArrayList<String>();
		ArrayList<Integer> res = new ArrayList<Integer>();
		String aux = "";

		for (int i = 0, o = 0; i < datos.length(); i++) { //FOR PARA LEER LAS LINEAS

			if (Character.isDigit(datos.charAt(i))) { //IF PARA DIFERENCIAR LOS NUMEROS QUE ESTAN DIVIDIDOS POR COMAS

				aux += datos.charAt(i);

				if (add) { //CONDICIONANTE PARA APLICAR A QUE ARRALIST DEBE IR
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

		for (int i = 0; i < resStr.size(); i++) { //FOR PARA RECORRER LA LISTA QUE YA FUERON LEIDAS Y PREPARADAS
			res.add(Integer.parseInt(resStr.get(i)));
		}
		Proceso p=null;
try {
		p= new Proceso(IDS, res.get(0), res.get(1), res.get(2)
				, res.get(3), res.get(4), res.get(5), res.get(6),
				res.get(7));// Crea el objeto Proceso
		IDS++;

		
}catch(Exception e) {}

return p;
	
	}// Fin

	public void inicializadorProceso() {  //METODO QUE INCIALIZA EL PROCESO
       
            contar(); //LLAMA AL METODO CONTAR
		
			Revisar r = new Revisar(this); //CREA EL OBJETO DE LA CLASE 
			r.start(); //COMIENZA EL METODO STAR
	         
	
	}
}// Fin clase
