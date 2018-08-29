package controller;

import java.util.Vector;

import handler.risorse.libri.FiltroLibriHandler;
import handler.risorse.libri.ScegliLibroHandler;
import model.Libri;
import model.Libro;
import model.Risorsa;
import myLib.MyMenu;
import view.LibriView;
import view.MessaggiSistemaView;

/**
 * CONTROLLER interagisce con view (quindi anche menu?) e modifica il MODEL
 * @author Stefano Prandini
 * @author Federico Landi
 */
public class LibriController 
{
	private Libri model;
	private int lastId;	
	
//	CATEGORIA � libro
	private static final String[] SOTTOCATEGORIE = {"Romanzo","Fumetto","Poesia"}; //le sottocategorie della categoria LIBRO (Romanzo, fumetto, poesia,...)
	private static final String[] GENERI = {"Fantascienza","Fantasy","Avventura","Horror","Giallo"};
	
	public LibriController(Libri libri)
	{
		this.model = libri;
		this.lastId = libri.getLastId();
	}

	public Libri getModel() 
	{
		return model;
	}
	
	public int getLastId() 
	{
		return lastId;
	}
	
	public void addLibro()
	{
		String sottoCategoria = menuScegliSottoCategoria();
//		se l'utente annulla la procedura
		if(sottoCategoria == "annulla")
		{
			return;
		}
		
		String genere = menuScegliGenere(sottoCategoria);
//				ScegliGenereLibroHandler.show(sottoCategoria);//se la sottocategoria ha generi disponibili
		String titolo = LibriView.chiediTitolo(Libro.class);
		int pagine = LibriView.chiediPagine();
		int annoPubblicazione = LibriView.chiediAnnoPubblicazione();
		String casaEditrice = LibriView.chiediCasaEditrice();
		String lingua = LibriView.chiediLingua();
		Vector<String> autori = new Vector<String>();
		do
		{
			String autore = LibriView.chiediAutore();
			autori.add(autore);
		} 
		while(LibriView.ciSonoAltriAutori());
		
		int nLicenze = LibriView.chiediNumeroLicenze();
		
		Libro l = new Libro("L"+lastId++, sottoCategoria, titolo, autori, pagine, annoPubblicazione, casaEditrice, lingua, genere, nLicenze);
		
		boolean aggiuntaRiuscita = model.addLibro(l);
		
		if(aggiuntaRiuscita)
		{
			LibriView.aggiuntaRiuscita(Libro.class);
		}
		else
		{
			LibriView.aggiuntaNonRiuscita(Libro.class);
		}
	}

	private String menuScegliSottoCategoria() 
	{
		MyMenu menu = new MyMenu("scegli la sottocategoria del libro: ", SOTTOCATEGORIE, true);
		try
		{
			return SOTTOCATEGORIE[menu.scegliBase() - 1];
		}
//		se l'utente selezione 0: ANNULLA -> eccezione
		catch(ArrayIndexOutOfBoundsException e)
		{
			return "annulla";
		}
	}
	
	private String menuScegliGenere(String sottoCategoria) 
	{
		if(sottoCategoria.equals("Romanzo") || sottoCategoria.equals("Fumetto")) //se si aggiunge un genere va aggiunto anche qui !
		{
			MyMenu menu = new MyMenu("scegli un genere: ", GENERI);
			return GENERI[menu.scegliNoIndietro() - 1];	
		}
		else
		{
			return "-";
		}
	}

	/**
	 * metodo per Test che consente di non chiedere in input all'utente i campi per creare la risorsa
	 */
	public void addLibro(String sottoCategoria, String titolo, Vector<String> autori, int pagine, int annoPubblicazione,
			String casaEditrice, String lingua, String genere, int nLicenze)
	{
		Libro l = new Libro("L"+lastId++, sottoCategoria, titolo, autori, pagine, annoPubblicazione, casaEditrice, lingua, genere, nLicenze);
		
		model.addLibro(l);
	}
	
	/**
	 * permette la rimozione di un libro da parte dell'operatore: il libro selezionato verr� etichettato come "non Prestabile" ma rimarr� in archivio per 
	 * motivi storici
	 * @return l'id del libro rimosso
	 */
	public String removeLibro()
	{
		Vector<Libro> libri = model.getLibri();

		String idSelezionato;
		
		String titolo = LibriView.chiediRisorsaDaRimuovere(Libro.class);
		
		Vector<Integer> posizioniRicorrenze = new Vector<>();
		
		for (int i = 0; i < libri.size(); i++)
		{
			if(libri.get(i).isPrestabile() && libri.get(i).getTitolo().toLowerCase().equals(titolo.toLowerCase()))
			{
//				ogni volta che in libri trovo un libro con il nome inserito dall'operatore, aggiungo la sua posizione al vettore
				posizioniRicorrenze.add(i);
			}
		}
		if(posizioniRicorrenze.size()==0)
		{
			LibriView.risorsaNonPresente(Libro.class);
			idSelezionato = "-1";
		}
//		se nel vettore delle ricorrenze c'� solo una posizione, elimino l'elemento in quella posizioni in libri
		else if(posizioniRicorrenze.size()==1)
		{
			idSelezionato = libri.get((int)posizioniRicorrenze.get(0)).getId();
			libri.get((int)posizioniRicorrenze.get(0)).setPrestabile(false);
			LibriView.rimozioneAvvenuta();
		}
//		se ci sono pi� elementi nel vettore (pi� libri con il nome inserito dall'operatore) li stampo e chiedo di selezionare quale si vuole rimuovere:
//		l'utente inserisce quello che vuole rimuovere
		else
		{
			LibriView.pi�RisorseStessoTitolo(Libri.class, titolo);
			
			int pos = 0;
			for(Integer i : posizioniRicorrenze)
			{
				LibriView.numeroRicorrenza(pos);
				MessaggiSistemaView.cornice();
				stampaDatiLibro(libri.elementAt((int)i), false);
				MessaggiSistemaView.cornice();
			}
			
			int daRimuovere = LibriView.chiediRicorrenzaDaRimuovere(posizioniRicorrenze.size());
			
			if(daRimuovere > 0)
			{
				idSelezionato = libri.get((int)posizioniRicorrenze.get(daRimuovere-1)).getId();
				libri.get((int)posizioniRicorrenze.get(daRimuovere-1)).setPrestabile(false);;
				LibriView.rimozioneAvvenuta();
			}
			else//0: annulla
			{
				idSelezionato = "-1";
			}
		}
		return idSelezionato;
	}
	
	/**
	 * metodo di Test, per non dover chiedere all'utente l'input per il titolo della risorsa da eliminare
	 */
	public void removeLibro(String titolo)
	{
		Vector<Libro> libri = model.getLibri();
		
		Vector<Integer> posizioniRicorrenze = new Vector<>();
		
		for (int i = 0; i < libri.size(); i++)
		{
			if(libri.get(i).isPrestabile() && libri.get(i).getTitolo().toLowerCase().equals(titolo.toLowerCase()))
			{
//				ogni volta che in libri trovo un libro con il nome inserito dall'operatore, aggiungo la sua posizione al vettore
				posizioniRicorrenze.add(i);
			}
		}
		if(posizioniRicorrenze.size()==0)
		{
			LibriView.risorsaNonPresente(Libro.class);
		}
//		se nel vettore delle ricorrenze c'� solo una posizione, elimino l'elemento in quella posizioni in libri
		else if(posizioniRicorrenze.size()==1)
		{
			libri.get((int)posizioniRicorrenze.get(0)).setPrestabile(false);
		}
//		se ci sono pi� elementi nel vettore (pi� libri con il nome inserito dall'operatore) li stampo e chiedo di selezionare quale si vuole rimuovere:
//		l'utente inserisce quello che vuole rimuovere
		else
		{
			LibriView.pi�RisorseStessoTitolo(Libri.class, titolo);
			
			int pos = 0;
			for(Integer i : posizioniRicorrenze)
			{
				LibriView.numeroRicorrenza(pos);
				MessaggiSistemaView.cornice();
				stampaDatiLibro(libri.elementAt((int)i), false);
				MessaggiSistemaView.cornice();
			}
			
			int daRimuovere = LibriView.chiediRicorrenzaDaRimuovere(posizioniRicorrenze.size());
			
			if(daRimuovere > 0)
			{
				libri.get((int)posizioniRicorrenze.get(daRimuovere-1)).setPrestabile(false);;
				LibriView.rimozioneAvvenuta();
			}
		}
	}
	
	/**
	 * ne stampa uno a uno, chiamando il corrispettivo metodo nella view
	 * @param libro
	 * @param perPrestito
	 */
	public void stampaDatiLibro(Libro libro, boolean perPrestito)
	{
		LibriView.stampaDati(libro, perPrestito);
	}
	
	/**
	 * stampa tutti i libri raggruppandoli per sottocategoria e genere, chiamando il corrispettivo metodo nella view
	 */
	public void stampaDatiLibriPrestabili()
	{
//		uso "libriDaStampare" cos� quando stampo un libro nella sua categoria posso eliminarlo e non stamparlo di nuovo dopo
		Vector<Libro> libriDaStampare = new Vector<>();
		for(Libro libro : model.getLibri())
		{
			if(libro.isPrestabile())
			{
				libriDaStampare.add(libro);
			}
		}
		LibriView.stampaDatiPerCategorie(libriDaStampare);
	}
	
	/**
	 * se i libri vengono filtrati per essere prenotati viene restituita la lista, se invece � solo per la ricerca vengono visualizzati e basta
	 * @param daPrenotare 
	 * @return la lista dei libri filtrati (if daPrenotare)
	 */
	public Vector<Libro> menuFiltraLibri(boolean daPrenotare) 
	{
		final String TITOLO_MENU_FILTRO = "Scegli in base a cosa filtrare la ricerca: ";
		final String[] VOCI_TITOLO_MENU_FILTRO = {"Filtra per titolo", "Filtra per anno di pubblicazione", "Filtra per autore"};
		
		MyMenu menuFiltro = new MyMenu(TITOLO_MENU_FILTRO, VOCI_TITOLO_MENU_FILTRO, true); 
		int scelta = menuFiltro.scegliBase();
		
		Vector<Libro> libriFiltrati = FiltroLibriHandler.filtraLibri(scelta, daPrenotare, this);
		
		if(daPrenotare)
		{
			return libriFiltrati;
		}
		else// !daPrenotare
		{
			return null;
		}
		
	}
	
	public Vector<Libro> filtraLibriPerTitolo(String titoloParziale)
	{
		return model.filtraLibriPerTitolo(titoloParziale);
	}
	
	
	public Vector<Libro> filtraLibriPerAnnoPubblicazione(int annoPubblicazione)
	{
		return model.filtraLibriPerAnnoPubblicazione(annoPubblicazione);
	}
	
	public Vector<Libro> filtraLibriPerAutori(String autore)
	{
		return model.filtraLibriPerAutori(autore);
	}
	
	public Vector<Libro> libriPrestabili()
	{
		Vector<Libro>libriPrestabili = new Vector<>();
		for(Libro libro : model.getLibri())
		{
			if(libro.isPrestabile())
			{
				libriPrestabili.add(libro);
			}
		}
		return libriPrestabili;
	}
	
	/**
	 * presenta all'utente il menu per decidere se scegliere un libro dall'archivio completo o filtrando la ricerca.
	 * informer� della scelta l'handler del caso d'uso (ScegliLibroHandler)
	 * @return
	 */
	public Risorsa menuScegliLibro() 
	{
		final String INTESTAZIONE_MENU = "\nScegli come visualizzare le risorse: ";
		final String[] SCELTE = new String[] {"Filtra ricerca", "Visualizza archivio"};
		
		MyMenu menuSceltaLibro = new MyMenu(INTESTAZIONE_MENU, SCELTE, true); 
		int scelta = menuSceltaLibro.scegliBase();
		
		return ScegliLibroHandler.scegliLibro(scelta, this);
	}	
	
	public Libro selezionaLibro(Vector<Libro> libri) 
	{
		if(libri.isEmpty())
		{
			LibriView.noRisorseDisponibili(Libri.class);
			return null;
		}
		else
		{
			for(int i = 0; i < libri.size(); i++)
			{
				MessaggiSistemaView.stampaPosizione(i);
				MessaggiSistemaView.cornice();
				stampaDatiLibro(libri.get(i), true);
				MessaggiSistemaView.cornice();
			}
			
			int selezione;
			do
			{
				MessaggiSistemaView.cornice();
				selezione = LibriView.selezionaRisorsa(libri.size(), Libro.class);
				if(selezione == 0)
				{
					return null;
				}
				else if(libri.get(selezione-1).getInPrestito() < libri.get(selezione-1).getNLicenze())
				{
					return libri.get(selezione-1);
				}
				else
				{
					LibriView.copieTutteInPrestito(libri.get(selezione-1).getTitolo());
				}
			}
			while(true);
		}		
	}
}
