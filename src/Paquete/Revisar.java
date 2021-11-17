package Paquete;

public class Revisar extends Thread {// CLASE REVISAR

	Proceso pr;// OBJETO PROCESO
	Programa pro;// OBJETO PROGRAMA

	Revisar(Programa pro) {// CONSTRUCTOR DE LA CLASE

		// INSTRANCIAMOS EL OBJETO
		this.pro = pro;

	}

	@Override
	public void run() {// METODO RUN

		while (pro.Contador < pro.limite) {// MIENTRAS EL CONTADOR SEA MENOR AL LIMITE

			Proceso pr = pro.crearProceso(pro.abrirProceso());// ABRIMOS EL ARCHIVO DONDE ESTAN LOS PROCESOS

			if (pr.getPri_Actual() == 0) {// IF PARA SABER LA PRIORIDAD----- TIEMPO REAL

				pro.TiempoReal.add(pr);

			} else {// SI NO ES DE TIEMPO REAL

				int contador = 0;
				for (int i = 2; i < 32; i++) {
					if (!pro.Memoria[i].getIcupado()) {// IF PARA SABER SI NO ESTA OCUPADO EL ESPACIO EN MEMORIA
						contador++;// INCREMENTAMOS CONTADOR
					}

				}

				int resu = 0;// VARIABLE PARA SABER CUANTOS ESPACIOS NECESITA EN MEMORIA

				// DIVISION PARA Y REDONDEO AL NUMERO MAYOR PARA ASIGNAR LA CANTIDAD NECESARIA
				resu = (int) Math.ceil(pr.getMemoria() / 32.0f);
				AsignarMemoria aram = new AsignarMemoria(pro, pr);// ASIGNAMOS LA RAM

				while (true) {

					if (resu <= contador) {// IF PARA SABER SI LA VARIABLE RESU ES MENOR O IGUAL A CONTADOR


						// SI ES TRUE, INICIAMOS EL HILO
						aram.start();

						break;

					} else {// SI ES FALSO
						try {
							// DORMIMOS EL HILO POR UN SEGUNDO
							Thread.sleep(1000);

						} catch (InterruptedException e) {// CAPTURA DE EXCEPCIONES
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// LUEGO DE PAUSAR EL HILO, VERIFICAMOS SI HAY ESPACIO EN MEMORIA
						contador = 0;
						for (int i = 2; i < 32; i++) {
							if (!pro.Memoria[i].getIcupado()) {
								contador++;
							} // FIN IF
						}

					}

				} // Fin while
			} // FIN ELSE
		} // While

	}// Run

}// Fin Thread
