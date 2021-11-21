package ProyectoFinal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingWorker;

public class Procesador extends SwingWorker<Object, Object> {
	private ModeloProceso actual;// Proceso a correr cada tick
	ArrayList<ModeloProceso> TiempoReal, usuario1, usuario2, usuario3, Todo;// Colas
	int Impresora = 2, Scanner = 1, Modem = 1, CD = 2;// Dispositivos E/S
	JFrameVentanaPrincipal vp;// Ventana principal
	boolean color = false;// Para poner color a la RAM
	boolean pass = true;// Para detener el while principal
	AtomicBoolean simulacionActiva;
	int contadorTodo;
	int limite;
	int procesosTerminados;

	Semaphore ram;

	Procesador(ArrayList<ModeloProceso> TiempoReal, ArrayList<ModeloProceso> usuario1,
			ArrayList<ModeloProceso> usuario2, ArrayList<ModeloProceso> usuario3, ArrayList<ModeloProceso> Todo, JFrameVentanaPrincipal vp, AtomicBoolean simulacionActiva, Semaphore ram, int limite) {

		// Asignacion
		this.TiempoReal = TiempoReal;
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.usuario3 = usuario3;
		this.Todo = Todo;
		this.vp = vp;// Asigna Ventana Principal
		this.simulacionActiva=simulacionActiva;
		contadorTodo=0;
		this.ram = ram;
		this.limite = limite;

	}// Contructor

	@Override
	protected Object doInBackground() throws Exception {

		for(int i=0; i<TiempoReal.size(); i++){
			for (int t = 0; t < vp.modelo.getRowCount(); t++) {

				if ((int) vp.modelo.getValueAt(t, 0) == TiempoReal.get(i).ID) {
					vp.modelo.setValueAt("Bloqueado", t, 1);
				}

			}
		 }

        //  Thread colorear = new Thread(){
		// 	 @Override
		// 	 public void run() {
		// 		 super.run();
        //         try{
		// 		 while(pass){
		// 			 for(int i=0; i<usuario1.size(); i++){
		// 				if (!usuario1.get(i).Color) {// Colorea la RAM
		// 					asigna(usuario1.get(i));
		// 					Thread.sleep(700);
		// 				}
		// 			 }
		// 			 for(int i=0; i<usuario2.size(); i++){
		// 				if (!usuario2.get(i).Color) {// Colorea la RAM
		// 					asigna(usuario2.get(i));
		// 					Thread.sleep(700);
		// 				}
		// 			 }
		// 			 for(int i=0; i<usuario3.size(); i++){
		// 				if (!usuario3.get(i).Color) {// Colorea la RAM
		// 					asigna(usuario3.get(i));
		// 					Thread.sleep(700);
		// 				}
		// 			 }
		// 		 }
		// 		}catch(Exception e){}

		// 	 }
		//  };
		//  colorear.start();

		// Metodo principal a correr

		// Bucle que se repite hasta acabar todos los procesos
		while (pass) {
            
			// Si la cola de tiempo real tiene datos entra
			if (0 != TiempoReal.size()) {

				// Recorriendo cola
				for (int i = 0; i < TiempoReal.size(); i++) {
					setProceso(TiempoReal.get(i));// Obtener el proceso de la cola
                    
					for (int t = 0; t < vp.modelo.getRowCount(); t++) {

						if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
							vp.modelo.setValueAt("Listo", t, 1);
							vp.modelo.setValueAt("R-0, R-1", t, 8);
							
							if(Todo.size() > contadorTodo){
							asigna(Todo.get(contadorTodo));
							contadorTodo++;
							}
						}
						
					}

					procesar();// Mandar a procesar

					// Si es diferente a 0
					if (TiempoReal.get(i).getT_Restante() != 0) {
						break;

						// Si ya es 0
					} else if (getProceso().getT_Restante() == 0) {

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
								vp.modelo.removeRow(t);
							}

						}
						// Aca adentro se elimina si el proceso finaliza
						TiempoReal.remove(i);
						procesosTerminados++;
						// vp.proF[0]++;
						// vp.procesosF[0].setText(vp.tit[0] + vp.proF[0]);
						setProceso(null);

						// libera RAM para el siguiente de Treal
						for (int k = 0; k < 2; k++) {
							vp.Memoria[k].setOcupado(false);
							vp.Memoria[k].setIDOcupador(0);
						}
						break;
					} // Fin else if
				} // Fin for

				// Si cola de Usuario1 tiene procesos
			} else if (0 != usuario1.size()) {
				// Recorre
				for (int i = 0; i < usuario1.size(); i++) {

					setProceso(usuario1.get(i));// Establece proceso

				    if (!getProceso().Color) {// Colorea ram
						asigna(getProceso());
						getProceso().Color = true;
					}

					while (true) {// Pide Dispositivos E/S

						// Pide CD
						if (getProceso().getCDSolicitados() <= CD && CD > 0) {

							for (int u = 0; u < getProceso().getCDSolicitados(); u++) {
								int aux = 0;
								CD--;
								aux++;

								getProceso().setCD(getProceso().getCD() + aux);

								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getCD(), t, 16);
										vp.cantidadCD.setText("Disponible: "+CD);
										
									}

								}

							} // Fin for
						} // Fin if

						// Pide impresoras
						if (getProceso().getImpresorasSolicitadas() <= Impresora && Impresora > 0) {
							for (int u = 0; u < getProceso().getImpresorasSolicitadas(); u++) {
								int aux = 0;
								Impresora--;
								aux++;

								getProceso().setImpresoras(getProceso().getImpresoras() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getImpresoras(), t, 10);
										vp.cantidadPrinter.setText("Disponible: "+Impresora);
										
									}

								}

							} // Fin for
						} // Fin if

						// Pide modem
						if (getProceso().getModemsSolicitados() <= Modem && Modem > 0) {
							for (int u = 0; u < getProceso().getModemsSolicitados(); u++) {
								int aux = 0;
								Modem--;
								aux++;

								getProceso().setModems(getProceso().getModems() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getModems(), t, 14);
										vp.cantidadModem.setText("Disponible: "+Modem);
										
									}

								}

							} // Fin for
						} // Fin if

						// Pide scanners
						if (getProceso().getScanneresSolicitado() <= Scanner && Scanner > 0) {
							for (int u = 0; u < getProceso().getScanneresSolicitado(); u++) {
								int aux = 0;
								Scanner--;
								aux++;

								getProceso().setScanneres(getProceso().getScanneres() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getScanneres(), t, 12);
										vp.cantidadScanner.setText("Disponible: "+Scanner);
									}

								}

							} // Fin for
						} // Fin if

						// Si hubieron Dispositivos E/S disponibles para sustentar el proceso
						if (getProceso().getCDSolicitados() == getProceso().getCD()
								&& getProceso().getImpresorasSolicitadas() == getProceso().getImpresoras()
								&& getProceso().getModemsSolicitados() == getProceso().getModems()
								&& getProceso().getScanneresSolicitado() == getProceso().getScanneres()) {

							for (int t = 0; t < vp.modelo.getRowCount(); t++) {

								if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
									vp.modelo.setValueAt("Listo", t, 1);
									Thread.sleep(200);
								}

							}

							procesar();// Se manda a procesar
							break;

						}
					} // Fin while

					// Tiempo restante diferente de 0 para no destruirlo
					// Y mandarlo a la siguiente cola
					if (usuario1.get(i).getT_Restante() != 0) {
						// Se liberan dispositivos E/S
						CD += usuario1.get(i).getCD();
						usuario1.get(i).setCD(0);

						Impresora += usuario1.get(i).getImpresoras();
						usuario1.get(i).setImpresoras(0);

						Modem += usuario1.get(i).getModems();
						usuario1.get(i).setModems(0);

						Scanner += usuario1.get(i).getScanneres();
						usuario1.get(i).setScanneres(0);

						usuario2.add(usuario1.get(i));

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario1.get(i).ID) {
								vp.modelo.setValueAt(usuario1.get(i).getCD(), t, 16);// CD
								vp.modelo.setValueAt(usuario1.get(i).getImpresoras(), t, 10);// Impresoa
								vp.modelo.setValueAt(usuario1.get(i).getModems(), t, 14);// Moden
								vp.modelo.setValueAt(usuario1.get(i).getScanneres(), t, 12);// Escaner
							}

						}

						usuario1.remove(i);

						// Elimina el proceso y libera los D_E/S del proceso
					} else {
						CD += usuario1.get(i).getCD();
						usuario1.get(i).setCD(0);

						Impresora += usuario1.get(i).getImpresoras();
						usuario1.get(i).setImpresoras(0);

						Modem += usuario1.get(i).getModems();
						usuario1.get(i).setModems(0);

						Scanner += usuario1.get(i).getScanneres();
						usuario1.get(i).setScanneres(0);

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario1.get(i).ID) {
								vp.modelo.setValueAt(usuario1.get(i).getCD(), t, 16);// CD
								vp.modelo.setValueAt(usuario1.get(i).getImpresoras(), t, 10);// Impresoa
								vp.modelo.setValueAt(usuario1.get(i).getModems(), t, 14);// Moden
								vp.modelo.setValueAt(usuario1.get(i).getScanneres(), t, 12);// Escaner
							}

						}

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario1.get(i).ID) {
								vp.modelo.removeRow(t);
							}

						}
						int resu = 0;// VARIABLE PARA SABER CUANTOS ESPACIOS NECESITA EN MEMORIA

				// DIVISION PARA Y REDONDEO AL NUMERO MAYOR PARA ASIGNAR LA CANTIDAD NECESARIA
				resu = (int) Math.ceil(usuario1.get(i).getMemoria() / 32.0f);
                        ram.release(resu);
						usuario1.remove(i);// Se eliminar
						procesosTerminados++;
						// vp.procesosF[1].setText(vp.tit[1] + vp.proF[1]);

						// Se libera RAM
						for (int o = 2; o < 32; o++) {

							if (vp.Memoria[o].getIDOcupador() == getProceso().getID()) {

								vp.Memoria[o].Barra.setBackground(Color.white);
								vp.Memoria[o].setOcupado(false);
								vp.Memoria[o].setIDOcupador(0);

							} /// Fin if
						} // Fin for
					}

					setProceso(null);// Se elimina el proceso

					if (TiempoReal.size() != 0) {
						break;// Si hay un proceso nuevo en la cola de tiempo real
					} // Fin if

				} // Fin for

				// Cola Usuario 2
			} else if (0 != usuario2.size()) {

				// Se recorre
				for (int i = 0; i < usuario2.size(); i++) {

					setProceso(usuario2.get(i));// Se establece el proceso
				    if (!getProceso().Color) {// Colorea ram
						asigna(getProceso());
						getProceso().Color = true;
					}

					// Pide D_E/S
					while (true) {

						if (getProceso().getCDSolicitados() <= CD && CD > 0) {

							for (int u = 0; u < getProceso().getCDSolicitados(); u++) {
								int aux = 0;
								CD--;
								aux++;

								getProceso().setCD(getProceso().getCD() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getCD(), t, 16);// CD
										vp.cantidadCD.setText("Disponible: "+CD);
										
									}

								}

							} // Fin for
						} // Fin if

						if (getProceso().getImpresorasSolicitadas() <= Impresora && Impresora > 0) {
							for (int u = 0; u < getProceso().getImpresorasSolicitadas(); u++) {

								int aux = 0;
								Impresora--;
								aux++;

								getProceso().setImpresoras(getProceso().getImpresoras() + aux);

								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getImpresoras(), t, 10);// Impresoa
										
										vp.cantidadPrinter.setText("Disponible: "+Impresora);
										
									}

								}

							} // Fin for
						} // Fin if

						if (getProceso().getModemsSolicitados() <= Modem && Modem > 0) {
							for (int u = 0; u < getProceso().getModemsSolicitados(); u++) {

								int aux = 0;
								Modem--;
								aux++;

								getProceso().setModems(getProceso().getModems() + aux);

								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getModems(), t, 14);// Moden
									
										vp.cantidadModem.setText("Disponible: "+Modem);
									}

								}

							} // Fin for
						} // Fin if

						if (getProceso().getScanneresSolicitado() <= Scanner && Scanner > 0) {
							for (int u = 0; u < getProceso().getScanneresSolicitado(); u++) {

								int aux = 0;
								Scanner--;
								aux++;
								getProceso().setScanneres(getProceso().getScanneres() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getScanneres(), t, 12);// Escaner
										
										vp.cantidadScanner.setText("Disponible: "+Scanner);
									}

								}

							} // Fin for
						} // Fin if

						// Obtuvo todo los necesarios
						if (getProceso().getCDSolicitados() == getProceso().getCD()
								&& getProceso().getImpresorasSolicitadas() == getProceso().getImpresoras()
								&& getProceso().getModemsSolicitados() == getProceso().getModems()
								&& getProceso().getScanneresSolicitado() == getProceso().getScanneres()) {

							for (int t = 0; t < vp.modelo.getRowCount(); t++) {

								if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
									vp.modelo.setValueAt("Listo", t, 1);
									Thread.sleep(200);
								}

							}

							procesar();// Se procesa

							break;

						}
					} // Fin while
						// Se liberan recursos y se manda a la cola 3
					if (usuario2.get(i).getT_Restante() != 0) {
						CD += usuario2.get(i).getCD();
						usuario2.get(i).setCD(0);

						Impresora += usuario2.get(i).getImpresoras();
						usuario2.get(i).setImpresoras(0);

						Modem += usuario2.get(i).getModems();
						usuario2.get(i).setModems(0);

						Scanner += usuario2.get(i).getScanneres();
						usuario2.get(i).setScanneres(0);

						usuario3.add(usuario2.get(i));

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario2.get(i).ID) {
								vp.modelo.setValueAt(usuario2.get(i).getCD(), t, 16);// CD
								vp.modelo.setValueAt(usuario2.get(i).getImpresoras(), t, 10);// Impresoa
								vp.modelo.setValueAt(usuario2.get(i).getModems(), t, 14);// Moden
								vp.modelo.setValueAt(usuario2.get(i).getScanneres(), t, 12);// Escaner
							}

						}

						usuario2.remove(i);
						// Libera y elimina el proceso
					} else {

						CD += usuario2.get(i).getCD();
						usuario2.get(i).setCD(0);

						Impresora += usuario2.get(i).getImpresoras();
						usuario2.get(i).setImpresoras(0);

						Modem += usuario2.get(i).getModems();
						usuario2.get(i).setModems(0);

						Scanner += usuario2.get(i).getScanneres();
						usuario2.get(i).setScanneres(0);

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario2.get(i).ID) {
								vp.modelo.setValueAt(usuario2.get(i).getCD(), t, 16);// CD
								vp.modelo.setValueAt(usuario2.get(i).getImpresoras(), t, 10);// Impresoa
								vp.modelo.setValueAt(usuario2.get(i).getModems(), t, 14);// Moden
								vp.modelo.setValueAt(usuario2.get(i).getScanneres(), t, 12);// Escaner
							}

						}

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario2.get(i).ID) {
								vp.modelo.removeRow(t);
							}

						}
						int resu = 0;// VARIABLE PARA SABER CUANTOS ESPACIOS NECESITA EN MEMORIA

						// DIVISION PARA Y REDONDEO AL NUMERO MAYOR PARA ASIGNAR LA CANTIDAD NECESARIA
						resu = (int) Math.ceil(usuario2.get(i).getMemoria() / 32.0f);
						ram.release(resu);
						usuario2.remove(i);
                        procesosTerminados++;
						for (int o = 2; o < 32; o++) {

							if (vp.Memoria[o].getIDOcupador() == getProceso().getID()) {

								vp.Memoria[o].Barra.setBackground(Color.white);
								vp.Memoria[o].setOcupado(false);
								vp.Memoria[o].setIDOcupador(0);

							} /// Fin if
						} // Fin for
					} // Fin else

					setProceso(null);

					if (TiempoReal.size() != 0 || usuario1.size() != 0) {
						break;// Si hay un proceso nuevo en la cola de tiempo real
					} // Fin if
				} // Fin for

				// Tercer cola
			} else if (0 != usuario3.size()) {
				// Se recorre
				for (int i = 0; i < usuario3.size(); i++) {

					setProceso(usuario3.get(i));// Se esteblece
					if (!getProceso().Color) {// Colorea ram
						asigna(getProceso());
						getProceso().Color = true;
					}

					while (true) {// Pide Dispositivos E/S

						if (getProceso().getCDSolicitados() <= CD && CD > 0) {

							for (int u = 0; u < getProceso().getCDSolicitados(); u++) {
								int aux = 0;
								CD--;
								aux++;

								getProceso().setCD(getProceso().getCD() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getCD(), t, 16);// CD
									
										vp.cantidadCD.setText("Disponible: "+CD);
										
										
									}
	
								}
								

							} // Fin for
						} // Fin if
						if (getProceso().getImpresorasSolicitadas() <= Impresora && Impresora > 0) {
							for (int u = 0; u < getProceso().getImpresorasSolicitadas(); u++) {
								int aux = 0;
								Impresora--;
								aux++;

								getProceso().setImpresoras(getProceso().getImpresoras() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getImpresoras(),t, 10);// Impresoa
										vp.cantidadPrinter.setText("Disponible: "+Impresora);
										vp.repaint();
									}
	
								}
								

							} // Fin for
						} // Fin if

						if (getProceso().getModemsSolicitados() <= Modem && Modem > 0) {
							for (int u = 0; u < getProceso().getModemsSolicitados(); u++) {
								int aux = 0;
								Modem--;
								aux++;

								getProceso().setModems(getProceso().getModems() + aux);
								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getModems(), t, 14);// Moden
										vp.cantidadModem.setText("Disponible: "+Modem);
										vp.repaint();
									}
	
								}
								

							} // Fin for
						} // Fin if

						if (getProceso().getScanneresSolicitado() <= Scanner && Scanner > 0) {
							for (int u = 0; u < getProceso().getScanneresSolicitado(); u++) {
								int aux = 0;
								Scanner--;
								aux++;
								getProceso().setScanneres(getProceso().getScanneres() + aux);

								for (int t = 0; t < vp.modelo.getRowCount(); t++) {

									if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
										vp.modelo.setValueAt(getProceso().getScanneres(), t, 12);// Escaner
										vp.cantidadScanner.setText("Disponible: "+Scanner);
										vp.repaint();
									}
	
								}
								

							} // Fin for
						} // Fin if

						// Si obtuvo los D-E/S necesarios
						if (getProceso().getCDSolicitados() == getProceso().getCD()
								&& getProceso().getImpresorasSolicitadas() == getProceso().getImpresoras()
								&& getProceso().getModemsSolicitados() == getProceso().getModems()
								&& getProceso().getScanneresSolicitado() == getProceso().getScanneres()) {

							for (int t = 0; t < vp.modelo.getRowCount(); t++) {

								if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
									vp.modelo.setValueAt("Listo", t, 1);
									Thread.sleep(200);
								}

							}
							procesar();// Procesa
							break;

						}
					} // Fin while
						// Libera dispositivos y RAM
					if (usuario3.get(i).getT_Restante() != 0) {

						CD += usuario3.get(i).getCD();
						usuario3.get(i).setCD(0);

						Impresora += usuario3.get(i).getImpresoras();
						usuario3.get(i).setImpresoras(0);

						Modem += usuario3.get(i).getModems();
						usuario3.get(i).setModems(0);

						Scanner += usuario3.get(i).getScanneres();
						usuario3.get(i).setScanneres(0);

						usuario3.add(usuario3.get(i));

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario3.get(i).ID) {
								vp.modelo.setValueAt(usuario3.get(i).getCD(), t, 16);// CD
						vp.modelo.setValueAt(usuario3.get(i).getImpresoras(), t, 10);// Impresoa
						vp.modelo.setValueAt(usuario3.get(i).getModems(), t, 14);// Moden
						vp.modelo.setValueAt(usuario3.get(i).getScanneres(), t, 12);// Escaner
							}

						}
						
						usuario3.remove(i);
						// Libera y elimina el Proceso
					} else {

						CD += usuario3.get(i).getCD();
						usuario3.get(i).setCD(0);

						Impresora += usuario3.get(i).getImpresoras();
						usuario3.get(i).setImpresoras(0);

						Modem += usuario3.get(i).getModems();
						usuario3.get(i).setModems(0);

						Scanner += usuario3.get(i).getScanneres();
						usuario3.get(i).setScanneres(0);

						vp.cantidadCD.setText("Disponible: "+CD);
						vp.cantidadPrinter.setText("Disponible: "+Impresora);
						vp.cantidadScanner.setText("Disponible: "+Scanner);
						vp.cantidadModem.setText("Disponible: "+Modem);

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario3.get(i).ID) {
								vp.modelo.setValueAt(usuario3.get(i).getCD(), t, 16);// CD
						vp.modelo.setValueAt(usuario3.get(i).getImpresoras(), t, 10);// Impresoa
						vp.modelo.setValueAt(usuario3.get(i).getModems(), t, 14);// Moden
						vp.modelo.setValueAt(usuario3.get(i).getScanneres(), t, 12);// Escaner3
							}

						}
						

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == usuario3.get(i).ID) {
								vp.modelo.removeRow(t);
							}

						}
						int resu = 0;// VARIABLE PARA SABER CUANTOS ESPACIOS NECESITA EN MEMORIA

						// DIVISION PARA Y REDONDEO AL NUMERO MAYOR PARA ASIGNAR LA CANTIDAD NECESARIA
						resu = (int) Math.ceil(usuario3.get(i).getMemoria() / 32.0f);
								ram.release(resu);
						usuario3.remove(i);
						procesosTerminados++;
						for (int o = 2; o < 32; o++) {

							if (vp.Memoria[o].getIDOcupador() == getProceso().getID()) {

								vp.Memoria[o].Barra.setBackground(Color.white);

								vp.Memoria[o].setOcupado(false);
								vp.Memoria[o].setIDOcupador(0);

							} /// Fin if
						} // Fin for

					} // Fin else if

					setProceso(null);

					if (TiempoReal.size() != 0 || usuario1.size() != 0 || usuario2.size() != 0) {
						break;// Si hay un proceso nuevo en la cola de tiempo real
					} // Fin if

				} // Fin for
				if(limite == procesosTerminados){
				if (TiempoReal.size() == 0 && usuario1.size() == 0 && usuario2.size() == 0 && usuario3.size() == 0) {
					simulacionActiva.set(false);
					vp.recargar.setEnabled(true);
					vp.MemoriaRAM.dispose();
					Todo.removeAll(Todo);
					pass = false;
				}
			}

			}
		} // Fin wile
		return null;

	} // Fin de run

	public void procesar() {
		// Metodo para procesar
	

					// Si es un proceso bueno
					if (getProceso() != null) {

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {
								vp.modelo.setValueAt("Corriendo", t, 1);
							}

						}

						try {

							Thread.sleep(1200);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
						getProceso().setT_Restante(getProceso().getT_Restante() - 1);

						if (getProceso().getPri_Actual() > 0 && getProceso().getPri_Actual() < 3) {
							getProceso().setPri_Actual(getProceso().getPri_Actual() + 1);
						}

						
						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {

								vp.modelo.setValueAt(getProceso().Pri_Actual, t, 4);
								vp.modelo.setValueAt(getProceso().T_Restante, t, 6);

								vp.modelo.setValueAt("Bloqueado", t, 1);
							}

						}

					} // Fin else

			

	}// Fin

	public void asigna(ModeloProceso proceso) {
		synchronized(proceso){
		if(!proceso.Color){
		// Metodo que colorea la RAM
		// Colores RGB aliatorios
		vp.rgb[0] = (int) Math.floor(Math.random() * (255) + (0));
		vp.rgb[1] = (int) Math.floor(Math.random() * (230) + (0));
		vp.rgb[2] = (int) Math.floor(Math.random() * (240) + (0));

		// Recorre la RAM
		for (int i = 2; i < 32; i++) {

			// Si el ID del proceso coincide con el ID del proceso que ocupa
			// la RAM colorea
			if (vp.Memoria[i].getIDOcupador() == proceso.getID()) {
				vp.Memoria[i].Barra.setBackground(new Color(vp.rgb[0], vp.rgb[1], vp.rgb[2]));
				proceso.Color = true;

				for (int t = 0; t < vp.modelo.getRowCount(); t++) {

					if ((int) vp.modelo.getValueAt(t, 0) == proceso.getID()) {
						vp.modelo.setValueAt("Bloqueado", t, 1);
						vp.modelo.setValueAt(proceso.getUbicacion(), t, 8);
					}

				}

			} /// Fin if
		} // Fin for
	}//Fin if
    }
	}// Fin asigna

	// GETS AND SETS
	public ModeloProceso getProceso() {
		return actual;
	}

	public void setProceso(ModeloProceso actual) {
		this.actual = actual;
	}

}// Fin clase
