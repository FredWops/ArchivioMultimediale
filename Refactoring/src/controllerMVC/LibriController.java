package controllerMVC;

import java.util.Vector;
import handler.ManageRisorseHandler;
import interfaces.Risorsa;
import model.Libro;
import model.Risorse;
import myLib.MyMenu;
import view.LibriView;
import viewInterfaces.ILibriView;

/**
 * CONTROLLER interagisce con view e modifica il MODEL
 * @author Stefano Prandini
 * @author Federico Landi
 */
public class LibriController 
{
	private Risorse model;
	private ILibriView libriView;

	private ManageRisorseHandler manageRisorseHandler;
//	CATEGORIA � Libro
	private static final String[] SOTTOCATEGORIE = {"Romanzo","Fumetto","Poesia"}; //le sottocategorie della categoria LIBRO (Romanzo, fumetto, poesia,...)
	private static final String[] GENERI = {"Fantascienza","Fantasy","Avventura","Horror","Giallo"};
	private static final String IDENTIFIER = "L";
	
	public LibriController(Risorse risorse, ManageRisorseHandler manageRisorseHandler)
	{
		this.model = risorse;
		this.libriView = new LibriView();
		this.manageRisorseHandler = manageRisorseHandler;
	}

	public ILibriView getLibriView() 
	{
		return libriView;
	}
	
	public Risorse getModel() 
	{
		return model;
	}
	
	public void addLibro()
	{
		String sottoCategoria = scegliSottoCategoria();
//		se l'utente annulla la procedura
		if(sottoCategoria == "annulla")
		{
			return;
		}
		
		String genere = scegliGenere(sottoCategoria);
		String titolo = libriView.chiediTitolo();
		int pagine = libriView.chiediPagine();
		int annoPubblicazione = libriView.chiediAnnoPubblicazione();
		String casaEditrice = libriView.chiediCasaEditrice();
		String lingua = libriView.chiediLingua();
		Vector<String> autori = new Vector<String>();
		do
		{
			String autore = libriView.chiediAutore();
			autori.add(autore);
		} 
		while(libriView.ciSonoAltriAutori());
		
		int nLicenze = libriView.chiediNumeroLicenze();
		
		Libro l = new Libro(IDENTIFIER + model.getLastId(), sottoCategoria, titolo, autori, pagine, annoPubblicazione, casaEditrice, lingua, genere, nLicenze);
		
		boolean aggiuntaRiuscita = model.addRisorsa(l);
		
		if(aggiuntaRiuscita)
		{
			libriView.aggiuntaRiuscita(Libro.class);
		}
		else
		{
			libriView.aggiuntaNonRiuscita(Libro.class);
		}
	}
	
	/**
	 * metodo per Test che consente di non chiedere in input all'utente i campi per creare la risorsa
	 */
	public void addLibro(String sottoCategoria, String titolo, Vector<String> autori, int pagine, int annoPubblicazione,
			String casaEditrice, String lingua, String genere, int nLicenze)
	{
		Libro l = new Libro("L"+ model.getLastId(), sottoCategoria, titolo, autori, pagine, annoPubblicazione, casaEditrice, lingua, genere, nLicenze);
		
		model.addRisorsa(l);
	}

	private String scegliSottoCategoria() 
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
	
	private String scegliGenere(String sottoCategoria) 
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
	
	public Vector<Risorsa> libriPrestabili()
	{
		Vector<Risorsa>libriPrestabili = new Vector<>();
		for(Risorsa risorsa : model.getRisorse())
		{
			if(risorsa.getId().charAt(0)=='L' && risorsa.isPrestabile())
			{
				libriPrestabili.add(risorsa);
			}
		}
		return libriPrestabili;
	}
	
	public Libro selezionaLibro(Vector<Risorsa> libriFiltrati) 
	{
		if(libriFiltrati.isEmpty())
		{
			libriView.noRisorseDisponibili("libri");
			return null;
		}
		else
		{
			for(int i = 0; i < libriFiltrati.size(); i++)
			{
				libriView.getMessaggiSistemaView().stampaPosizione(i);
				libriView.getMessaggiSistemaView().cornice();
				libriView.stampaDati(libriFiltrati.get(i), true);
				libriView.getMessaggiSistemaView().cornice();
			}
			
			int selezione;
			do
			{
				libriView.getMessaggiSistemaView().cornice();
				selezione = libriView.selezionaRisorsa(libriFiltrati.size(), Libro.class);
				if(selezione == 0)
				{
					return null;
				}
				else if(libriFiltrati.get(selezione-1).getInPrestito() < libriFiltrati.get(selezione-1).getNLicenze())
				{
					return (Libro) libriFiltrati.get(selezione-1);
				}
				else
				{
					libriView.copieTutteInPrestito(libriFiltrati.get(selezione-1).getTitolo());
				}
			}
			while(true);
		}		
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
		
		return manageRisorseHandler.scegliLibro(scelta);
	}	
}
