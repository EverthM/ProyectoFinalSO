package ProyectoFinal;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {//CLASE PRINCIPAL DEL PROGRAMA

	public static void main(String[] args) {//METODO MAIN 
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
				new PrincipalPrograma();//CREAMOS OBJETO DE LA CLASE PROGRAMA
		
	}//Main
}//Class
 