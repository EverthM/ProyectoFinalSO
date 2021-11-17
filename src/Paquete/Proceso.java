package Paquete;

public class Proceso {
	
int ID;
int Status;//Corriendo, parado..
int T_Llegada;// Tiempo de llegada*
int Pri_Inicial;//Prioridad inicial*
int Pri_Actual; //Prioridad actual
int T_Requerido;//Tiempo requerido*
int T_Restante;//Tiempo restante
int Memoria;//Memoria requerida*
String Ubicacion="";//Ubicacion en memoria
int ImpresorasSolicitadas;//*
int Impresoras;//Impresoras asignadas
int ScanneresSolicitado;//*
int Scanneres;//Scanneres asignados
int ModemsSolicitados;//*
int Modems;//Modems asignados
int CDSolicitados;//*
int CD;//CDS asignados
boolean ControlIngreso=true; // Control del ingreso
boolean Color=false; //Variable de color
boolean accesoTotal=false; //Variable de acceso total


	//Metodo constructor con todos sus parametros y sus inicializaciones
	Proceso(int ID,int T_Llegada, int Prioridad, int Requerido, int Memoria,  
			int CantImpre,int CantScann, int CantModem, int CantCD){
		
		this.ID=ID;
		this.T_Llegada=T_Llegada;
		this.Pri_Actual=this.Pri_Inicial=Prioridad;
		this.T_Restante=this.T_Requerido= Requerido;
		this.Memoria=Memoria;
		this.ImpresorasSolicitadas=CantImpre;
		this.ScanneresSolicitado=CantScann;
		this.ModemsSolicitados=CantModem;
		this.CDSolicitados=CantCD;
		
		
		
	}//Constructor
	
	
	//Metodo string que muestra todo los datos de los procesos en ejecucion
	public String mostrar() {
		return "ID: "+getID()+
	           "\nStatus: Corriendo"+
	           "\nTiempo de llegada: "+getT_Llegada()+
			   "\nPrioridad Inicial: "+getPri_Inicial()+
			   "\nPrioridad actual: " +getPri_Actual()+
			   "\nTiempo requerido: "+getT_Requerido()+
			   "\nTiempo restante: "+getT_Restante()+
			   "\nMemoria requerida: "+getMemoria()+
			   "\nUbicacion en memoria: "+getUbicacion()+
			   "\nImpresoras solicitadas: "+getImpresorasSolicitadas()+
			   "\nScanneres solicitados: "+getScanneresSolicitado()+
			   "\nModems solicitados: "+getModemsSolicitados()+
			   "\nCD solicitados: "+getCDSolicitados()+
			   "\nImpresoras otorgadas: "+getImpresoras()+
			   "\nScanneres otorgados: "+getScanneres()+
			   "\nModems otorgados: "+getModems()+
			   "\nCD otorgados: "+getCD();
	}// fin del metodo
	
	//Set y get de la clase
	public int getModemsSolicitados() { 
		return ModemsSolicitados;
	}

	public void setModemsSolicitados(int modemsSolicitados) {
		ModemsSolicitados = modemsSolicitados;
	}

	public int getModems() {
		return Modems;
	}

	public void setModems(int modems) {
		Modems = modems;
	}

	//GETS AND SETS
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getT_Llegada() {
		return T_Llegada;
	}

	public void setT_Llegada(int t_Llegada) {
		T_Llegada = t_Llegada;
	}

	public int getPri_Inicial() {
		return Pri_Inicial;
	}

	public void setPri_Inicial(int pri_Inicial) {
		Pri_Inicial = pri_Inicial;
	}

	public int getPri_Actual() {
		return Pri_Actual;
	}

	public void setPri_Actual(int pri_Actual) {
		Pri_Actual = pri_Actual;
	}

	public int getT_Requerido() {
		return T_Requerido;
	}

	public void setT_Requerido(int t_Requerido) {
		T_Requerido = t_Requerido;
	}

	public int getT_Restante() {
		return T_Restante;
	}

	public void setT_Restante(int t_Restante) {
		T_Restante = t_Restante;
	}

	public int getMemoria() {
		return Memoria;
	}

	public void setMemoria(int memoria) {
		Memoria = memoria;
	}

	public String getUbicacion() {
		return Ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		Ubicacion = ubicacion;
	}

	public int getImpresorasSolicitadas() {
		return ImpresorasSolicitadas;
	}

	public void setImpresorasSolicitadas(int impresorasSolicitadas) {
		ImpresorasSolicitadas = impresorasSolicitadas;
	}

	public int getImpresoras() {
		return Impresoras;
	}

	public void setImpresoras(int impresoras) {
		Impresoras = impresoras;
	}

	public int getScanneresSolicitado() {
		return ScanneresSolicitado;
	}

	public void setScanneresSolicitado(int scanneresSolicitado) {
		ScanneresSolicitado = scanneresSolicitado;
	}

	public int getScanneres() {
		return Scanneres;
	}

	public void setScanneres(int scanneres) {
		Scanneres = scanneres;
	}

	public int getCDSolicitados() {
		return CDSolicitados;
	}

	public void setCDSolicitados(int cDSolicitados) {
		CDSolicitados = cDSolicitados;
	}

	public int getCD() {
		return CD;
	}

	public void setCD(int cD) {
		CD = cD;
	}

	public boolean isControlIngreso() {
		return ControlIngreso;
	}

	public void setControlIngreso(boolean controlIngreso) {
		ControlIngreso = controlIngreso;
	}
	
}//Fin class

