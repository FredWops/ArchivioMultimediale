package menus.prestiti;

import controller.ArchivioController;
import controller.PrestitiController;
import model.Main;
import model.Prestiti;
import myLib.MyMenu;
import model.Fruitori;
import model.GestoreSalvataggi;

public class MenuTerminaPrestiti 
{
	static String[] scelte = new String[] {"tutti","solo uno"};
	static String messaggioEliminaPrestiti = "Vuoi eliminare tutti i prestiti o solo uno?";

	
	public static void show(Prestiti prestiti, ArchivioController mainController,PrestitiController pc) 
	{
		
		
		MyMenu menuPrestiti = new MyMenu(messaggioEliminaPrestiti, scelte, true);
		
		switch (menuPrestiti.scegliBase()) 
		{
		case 0://indietro
		{
			break;
		}
		case 1://elimina tutti i prestiti
		{
			prestiti.terminaTuttiPrestitiDi(Main.getUtenteLoggato());
			
			GestoreSalvataggi.salvaPrestiti(prestiti);
			Main.salvaArchivio();
//			GestoreSalvataggi.salvaArchivio(archivio);
			
			break;
		}
		case 2://elimina un solo prestito (sceglie l'utente)
		{
			pc.terminaPrestitoDi(Main.getUtenteLoggato());
			
			GestoreSalvataggi.salvaPrestiti(prestiti);
			Main.salvaArchivio();
//			GestoreSalvataggi.salvaArchivio(archivio);
			
			break;
		}
		}		
	}
	

}
