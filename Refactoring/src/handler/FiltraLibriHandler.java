package handler;

import java.util.Vector;
import controllerMVC.LibriController;
import interfaces.Risorsa;
import myLib.MyMenu;

public class FiltraLibriHandler 
{
	private LibriController libriController;
	
	public FiltraLibriHandler(LibriController libriController)
	{
		this.libriController = libriController;
	}
	
	/**
	 * se i libri vengono filtrati per essere prenotati viene restituita la lista, se invece � solo per la ricerca vengono visualizzati e basta
	 * @param daPrenotare 
	 * @return la lista dei libri filtrati (if daPrenotare)
	 */
	public Vector<Risorsa> menuFiltraLibri(boolean daPrenotare) 
	{
		final String TITOLO_MENU_FILTRO = "Scegli in base a cosa filtrare la ricerca: ";
		final String[] VOCI_TITOLO_MENU_FILTRO = {"Filtra per titolo", "Filtra per anno di pubblicazione", "Filtra per autore"};
		
		MyMenu menuFiltro = new MyMenu(TITOLO_MENU_FILTRO, VOCI_TITOLO_MENU_FILTRO, true); 
		int scelta = menuFiltro.scegliBase();
		
		Vector<Risorsa> libriFiltrati = filtraLibri(scelta, daPrenotare);
		
		if(daPrenotare)
		{
			return libriFiltrati;
		}
		else// !daPrenotare
		{
			return null;
		}
	}
	
	public Vector<Risorsa> filtraLibri(int scelta, boolean daPrenotare) 
	{
		Vector<Risorsa> libriFiltrati = null;
		String titoloParziale = null;
		int annoPubblicazione = 0;
		String nomeAutore = null;
		
		switch(scelta) 
		{
			case 0:	//INDIETRO
			{
				return null;
			}
			case 1: //FILTRA PER TITOLO
			{
				titoloParziale = libriController.getLibriView().chiediTitolo();
				libriFiltrati = filtraLibriPerTitolo(titoloParziale);
				break;
			}
			case 2:	//FILTRA PER ANNO DI PUBBLICAZIONE
			{
				annoPubblicazione = libriController.getLibriView().chiediAnnoPubblicazione();
				libriFiltrati = filtraLibriPerAnnoPubblicazione(annoPubblicazione);
				break;
			}
			case 3: //FILTRA PER AUTORE
			{
				nomeAutore = libriController.getLibriView().chiediAutore();
				libriFiltrati = filtraLibriPerAutori(nomeAutore);
				break;
			}
		}
		if (daPrenotare == false)
		{
			if(scelta == 1 && libriFiltrati.isEmpty()) 
			{
				libriController.getLibriView().risorsaNonPresente("libri",titoloParziale);
				return null;
			}
			if(scelta == 2 && libriFiltrati.isEmpty())
			{
				libriController.getLibriView().annoNonPresente(annoPubblicazione);
				return null;
			}
			if(scelta == 3 && libriFiltrati.isEmpty())
			{
				libriController.getLibriView().autoreNonPresente(nomeAutore);
				return null;
			}
			
			for (int i=0; i <libriFiltrati.size(); i++) 
			{
				libriController.getLibriView().getMessaggiSistemaView().cornice(true,false);
				libriController.getLibriView().stampaDati(libriFiltrati.get(i), false);
			}
		}
		
		else if (daPrenotare == true)
		{
			return libriFiltrati;
		}
		//se non sono da prenotare (quindi solo da visualizzare) non deve ritornare nulla
		return null;
	}
	
	public Vector<Risorsa> filtraLibriPerTitolo(String titoloParziale)
	{
		Vector<Risorsa> result = new Vector<>();
		Vector<Risorsa> filtrati = libriController.getModel().filtraRisorsePerTitolo("Libri",titoloParziale);
		for(Risorsa risorsa : filtrati)
		{
			if(risorsa.getCategoria().equals("Libri"))
			{
				result.addElement(risorsa);
			}
		}
		return result;
	}
	
	public Vector<Risorsa> filtraLibriPerAnnoPubblicazione(int annoPubblicazione)
	{
		Vector<Risorsa> result = new Vector<>();
		Vector<Risorsa> filtrati = libriController.getModel().filtraRisorsePerUscita("Libri", annoPubblicazione);
		for(Risorsa risorsa : filtrati)
		{
			if(risorsa.getCategoria().equals("Libri"))
			{
				result.addElement(risorsa);
			}
		}
		return result;
	}
	
	public Vector<Risorsa> filtraLibriPerAutori(String autore)
	{
		Vector<Risorsa> result = new Vector<>();
		Vector<Risorsa> filtrati = libriController.getModel().filtraLibriPerAutori("Libri", autore);
		for(Risorsa risorsa : filtrati)
		{
			if(risorsa.getCategoria().equals("Libri"))
			{
				result.addElement(risorsa);
			}
		}
		return result;
	}
}
