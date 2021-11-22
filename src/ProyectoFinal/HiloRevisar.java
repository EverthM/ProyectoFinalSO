package ProyectoFinal;

public class HiloRevisar extends Thread {// CLASE REVISAR

	ModeloProceso pr;// OBJETO PROCESO
	PrincipalPrograma pro;// OBJETO PROGRAMA
	int count = 0;

	HiloRevisar(PrincipalPrograma pro) {// CONSTRUCTOR DE LA CLASE

		// INSTRANCIAMOS EL OBJETO
		this.pro = pro;

	}

	@Override
	public void run() {// METODO RUN
      int limite = pro.contar();
		while (count < pro.Procesos.size()) {// MIENTRAS EL CONTADOR SEA MENOR AL LIMITE

			//ModeloProceso pr = pro.crearProceso(pro.abrirProceso());// ABRIMOS EL ARCHIVO DONDE ESTAN LOS PROCESOS
            ModeloProceso pr = pro.Procesos.get(count);


			
			Thread busqueda = new Thread(){
				@Override
				public synchronized void run() {
					
					super.run();

			
			for (int t = 0; t < pro.vp.modelo.getRowCount(); t++) {

				if ((int) pro.vp.modelo.getValueAt(t, 0) == pr.getID()) {
					pro.vp.modelo.setValueAt("Bloqueado", t, 1);
					pro.vp.modelo.setValueAt(pr.getUbicacion(), t, 8);
				}

			}

			if (pr.getPri_Actual() == 0) {// IF PARA SABER LA PRIORIDAD----- TIEMPO REAL

				pro.TiempoReal.add(pr);
				

			} else {// SI NO ES DE TIEMPO REAL
                
				int resu = 0;// VARIABLE PARA SABER CUANTOS ESPACIOS NECESITA EN MEMORIA
                resu = (int) Math.ceil(pr.getMemoria() / 32.0f);
				
				while(true){
                if(pro.ram.availablePermits() > resu){
                 
				try {
					pro.ram.acquire(resu);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				

				// DIVISION PARA Y REDONDEO AL NUMERO MAYOR PARA ASIGNAR LA CANTIDAD NECESARIA
				
					AsignarMemoria aram = new AsignarMemoria(pro, pr);// ASIGNAMOS LA RAM
				// SI ES TRUE, INICIAMOS EL HILO
						aram.start();
						break;
			}
			}
				// int contador = 0;
				// for (int i = 2; i < 32; i++) {
				// 	if (!pro.Memoria[i].getIcupado()) {// IF PARA SABER SI NO ESTA OCUPADO EL ESPACIO EN MEMORIA
				// 		contador++;// INCREMENTAMOS CONTADOR
				// 	}

				// }

				

			

				// while (true) {

				// 	if (resu <= contador) {// IF PARA SABER SI LA VARIABLE RESU ES MENOR O IGUAL A CONTADOR
                
				

						
						

				// 		break;

				// 	} else {// SI ES FALSO
						

				// 		// LUEGO DE PAUSAR EL HILO, VERIFICAMOS SI HAY ESPACIO EN MEMORIA
				// 		contador = 0;
				// 		for (int i = 2; i < 32; i++) {
				// 			if (!pro.Memoria[i].getIcupado()) {
				// 				contador++;
				// 			} // FIN IF
				// 		}

				// 	}

				// 	try {
				// 		Thread.sleep(2000);
				// 	} catch (InterruptedException e) {
						
				// 		e.printStackTrace();
				// 	}

				// } // Fin while
			} // FIN ELSE

		}
	};
	busqueda.start();
	count++;
		} // While

	}// Run

}// Fin Thread
