package view;

import myLib.BelleStringhe;
import myLib.InputDati;

public class MessaggiSistemaView 
{
	private static final String MESSAGGIO_ADDIO = "\nGrazie per aver usato ArchivioMultimediale!";
	private static final String MESSAGGIO_PASSWORD = "Inserire la password per accedere all'area riservata agli operatori: ";	
	
	public static void stampaAddio()
	{
		System.out.println(BelleStringhe.incornicia(MESSAGGIO_ADDIO));
	}
	
	public static String chiediPasswordOperatore() 
	{
		return InputDati.leggiStringaNonVuota(MESSAGGIO_PASSWORD);	
	}

	public static void accessoEseguito() 
	{
		System.out.println("Accesso eseguito con successo!");		
	}

	public static void avvisoRimozioneRisorsa() 
	{
		System.out.println("ATTENZIONE! Se la risorsa che si desidera rimuovere ha copie attualmente in prestito, queste verranno sottratte ai fruitori");		
	}

	public static void cornice() 
	{
		System.out.println(BelleStringhe.CORNICE);	
	}
}
