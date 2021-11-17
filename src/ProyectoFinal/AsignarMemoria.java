package ProyectoFinal;

public class AsignarMemoria extends Thread {// CLASE ASIGNAR RAM

	PrincipalPrograma pro;// OBJETO PROGRAMA
	ModeloProceso proceso;// OBJETO PROCESO

	public AsignarMemoria(PrincipalPrograma pro, ModeloProceso proceso) {// CONSTRUCTOR DE LA CLASE

		// INSTANCIAMOS LOS OBJETOS
		this.pro = pro;
		this.proceso = proceso;
	}

	public void run() {// METODO RUN
		inicializadorProceso(proceso);
	}

	public void inicializadorProceso(ModeloProceso pr) {// METODO INICIAR PROCESO

		ModeloProceso p = pr;// CREACION DE OBJETO PROCESO

		switch (p.getPri_Inicial()) {// SWITCH PARA SABER LA PRIORIDAD DE LOS PROCESOS

		case 0: {// CASO TIEMPO REAL

			// DIVISION PARA SABER CUANTO ESPACIO EN MEMORIA NECESITA
			float cuantos = (float) (p.getMemoria() / 32.0f);
			cuantos = (float) Math.ceil(cuantos);

			
			while (cuantos != 0) {

				for (int i = 0; i < 2; i++) {//FOR PARA UTILIZAR LA MEMORIA RESERVADA 

					if (!pro.Memoria[i].getIcupado()) {//IF PARA SABER SI NO ESTA OCUPADA  LA MEMORIA
						
						//SI NO ESTA OCUPADA, LE ASIGNAMOS
						pro.Memoria[i].setIDOcupador(p.ID);
						pro.Memoria[i].setOcupado(true);

						if (p.getUbicacion().isEmpty()) {// IF PARA SABER SI LA UBICACION ESTA VACIO
							
							p.setUbicacion(i + "");
							
						} else {
							p.setUbicacion(p.getUbicacion() + ", " + i);//SE ASIGNA LA UBICACION EN MEMORIA
						}
						cuantos--;//REDUCCION DE CUANTOS
					} /// Fin if

				} // Fin for
			} // Fin while
			
			
			if (cuantos == 0) {
				pro.TiempoReal.add(p);
				break;
			} // Fin if
			break;

			// pro.TiempoReal.add(p);
			// break;

		}
		case 1: {// CASO USUARIO 1

			
			// DIVISION PARA SABER CUANTO ESPACIO EN MEMORIA NECESITA
			float cuantos = (float) (p.getMemoria() / 32.0f);
			cuantos = (float) Math.ceil(cuantos);

			while (cuantos != 0) {

				for (int i = 2; i < 32; i++) {//FOR PARA UTILIZAR LA MEMORIA MENOS LA RESERVADA

					if (!pro.Memoria[i].getIcupado()) {//IF PARA SABER SI NO ESTA OCUPADA  LA MEMORIA
						
						//SI NO ESTA OCUPADA, LE ASIGNAMOS
						pro.Memoria[i].setIDOcupador(p.ID);
						pro.Memoria[i].setOcupado(true);

						if (p.getUbicacion().isEmpty()) {// IF PARA SABER SI LA UBICACION ESTA VACIO Y ASIGNARLE UNA UBICACION
							p.setUbicacion(i + "");
						} else {
							p.setUbicacion(p.getUbicacion() + ", " + i);//SI NO ESTA VACIO SE ASIGNA UNA UBICACION MAS EN MEMORIA
						}
						cuantos--;//REDUCCION DE CUANTOS
					} /// Fin if

					
					
					if (cuantos == 0) {
						pro.Usuario1.add(p);
						break;
					} // Fin if

				} // Fin for
			} // Fin while
			break;
		}
		case 2: {// CASO USUARIO 2
			
			// DIVISION PARA SABER CUANTO ESPACIO EN MEMORIA NECESITA
			float cuantos = (float) (p.getMemoria() / 32.0f);
			cuantos = (float) Math.ceil(cuantos);

			while (cuantos != 0) {

				for (int i = 2; i < 32; i++) {//FOR PARA UTILIZAR LA MEMORIA MENOS LA RESERVADA

					if (!pro.Memoria[i].getIcupado()) {//IF PARA SABER SI NO ESTA OCUPADA  LA MEMORIA
						
						//SI NO ESTA OCUPADA, LE ASIGNAMOS
						pro.Memoria[i].setIDOcupador(p.ID);
						pro.Memoria[i].setOcupado(true);

						if (p.getUbicacion().isEmpty()) {// IF PARA SABER SI LA UBICACION ESTA VACIO Y ASIGNARLE UNA UBICACION
							p.setUbicacion(i + "");
						} else {
							p.setUbicacion(p.getUbicacion() + ", " + i);//SI NO ESTA VACIO SE ASIGNA UNA UBICACION MAS EN MEMORIA
						}
						cuantos--;//REDUCCION DE CUANTOS
					} /// Fin if

					if (cuantos == 0) {
						pro.Usuario2.add(p);
						break;
					} // Fin if

				} // Fin for

			} // Fin while

			break;
		}
		case 3: {// CASO USUARIO 2

			// DIVISION PARA SABER CUANTO ESPACIO EN MEMORIA NECESITA
			float cuantos = (float) (p.getMemoria() / 32.0f);
			cuantos = (float) Math.ceil(cuantos);

			while (cuantos != 0) {

				for (int i = 2; i < 32; i++) {//FOR PARA UTILIZAR LA MEMORIA MENOS LA RESERVADA

					if (!pro.Memoria[i].getIcupado()) {//IF PARA SABER SI NO ESTA OCUPADA  LA MEMORIA
						
						//SI NO ESTA OCUPADA, LE ASIGNAMOS
						pro.Memoria[i].setIDOcupador(p.ID);
						pro.Memoria[i].setOcupado(true);

						if (p.getUbicacion().isEmpty()) {// IF PARA SABER SI LA UBICACION ESTA VACIO Y ASIGNARLE UNA UBICACION
							p.setUbicacion(i + "");
						} else {
							p.setUbicacion(p.getUbicacion() + ", " + i);//SI NO ESTA VACIO SE ASIGNA UNA UBICACION MAS EN MEMORIA
						}
						cuantos--;
					} /// Fin if

					if (cuantos == 0) {
						pro.Usuario3.add(p);
						break;
					} // Fin if

				} // Fin for

			} // Fin while

			break;
		}

		}// Fin switch

	}// Fin

}// Fin class
