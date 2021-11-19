package ProyectoFinal;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Procesador extends SwingWorker<Object, Object> {
	private ModeloProceso actual;// Proceso a correr cada tick
	ArrayList<ModeloProceso> TiempoReal, usuario1, usuario2, usuario3;// Colas
	int Impresora = 2, Scanner = 1, Modem = 1, CD = 2;// Dispositivos E/S
	JFrameVentanaPrincipal vp;// Ventana principal
	boolean color = false;// Para poner color a la RAM
	boolean pass = true;// Para detener el while principal
	AtomicBoolean simulacionActiva;

	Procesador(ArrayList<ModeloProceso> TiempoReal, ArrayList<ModeloProceso> usuario1,
			ArrayList<ModeloProceso> usuario2, ArrayList<ModeloProceso> usuario3, JFrameVentanaPrincipal vp, AtomicBoolean simulacionActiva) {

		// Asignacion
		this.TiempoReal = TiempoReal;
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
		this.usuario3 = usuario3;
		this.vp = vp;// Asigna Ventana Principal
		this.simulacionActiva=simulacionActiva;

	}// Contructor

	@Override
	protected Object doInBackground() throws Exception {
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

					if (!getProceso().Color) {// Colorea la RAM
						asigna();
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

						usuario1.remove(i);// Se eliminar
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
						asigna();
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

						usuario2.remove(i);

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
					if (!getProceso().Color) {// Se colorea RAM
						asigna();
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

						usuario3.remove(i);

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
				if (TiempoReal.size() == 0 && usuario1.size() == 0 && usuario2.size() == 0 && usuario3.size() == 0) {
					simulacionActiva.set(false);
					vp.recargar.setEnabled(true);
					pass = false;
				}

			}
		} // Fin wile
		return null;

	} // Fin de run

	public void procesar() {
		// Metodo para procesar
		try {
			// Invoca y espera
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {

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

						// Acutaliza el registro
						vp.logg += getProceso().mostrar() + "\n-------------------------\n";

						// Actualiza la consola
						vp.consola.setText(getProceso().mostrar());

						for (int t = 0; t < vp.modelo.getRowCount(); t++) {

							if ((int) vp.modelo.getValueAt(t, 0) == getProceso().ID) {

								vp.modelo.setValueAt(getProceso().Pri_Actual, t, 4);
								vp.modelo.setValueAt(getProceso().T_Restante, t, 6);

								vp.modelo.setValueAt("Bloqueado", t, 1);
							}

						}

					} // Fin else

				}
			});
		} catch (InvocationTargetException | InterruptedException e) {

			e.printStackTrace();
		}

	}// Fin

	public void asigna() {
		// Metodo que colorea la RAM
		// Colores RGB aliatorios
		vp.rgb[0] = (int) Math.floor(Math.random() * (255) + (0));
		vp.rgb[1] = (int) Math.floor(Math.random() * (230) + (0));
		vp.rgb[2] = (int) Math.floor(Math.random() * (240) + (0));

		// Recorre la RAM
		for (int i = 2; i < 32; i++) {

			// Si el ID del proceso coincide con el ID del proceso que ocupa
			// la RAM colorea
			if (vp.Memoria[i].getIDOcupador() == getProceso().getID()) {
				vp.Memoria[i].Barra.setBackground(new Color(vp.rgb[0], vp.rgb[1], vp.rgb[2]));
			} /// Fin if
		} // Fin for
	}// Fin asigna

	// GETS AND SETS
	public ModeloProceso getProceso() {
		return actual;
	}

	public void setProceso(ModeloProceso actual) {
		this.actual = actual;
	}

}// Fin clase
