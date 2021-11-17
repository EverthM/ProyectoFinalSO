package ProyectoFinal;

import javax.swing.JLabel;

public class MemoriaRAM { //CLASE RAM 
  boolean ocupado; //ESPACIO OCUPADO
  int IDOcupador;//ID DEL PROCESO OCUPADOR
  int No_Espacio; //NUMERO DE ESPACIO
  JLabel Barra; //BARRA QUE SE MESUTRA EN LA INTERFAZ
	
  
  //CONSTURCTOR CONS SUS PARAMETROS E INICIALIZACIONES
  MemoriaRAM(boolean ocupado, int IDOcupador, int Numero){
	  this.ocupado=ocupado;
	  this.IDOcupador=IDOcupador;
	  this.No_Espacio=Numero;
	  
	  Barra= new JLabel(); //BARRA QUE SE MUESTRA EN LA INTERFAZ GRAFICA
	
  }//Constructor
  
  //SET Y GET DE LA CLASE
public int getIDOcupador() {
	return IDOcupador;
}

public void setIDOcupador(int iDOcupador) {
	IDOcupador = iDOcupador;
}

public boolean getIcupado() {
	return ocupado;
}
public void setOcupado(boolean ocupado) {
	this.ocupado = ocupado;
}
  
}//Fin clase
