package model;

import java.io.Serializable;
import java.util.Vector;
import menus.risorse.libri.MenuSottoCategoriaLibri;
import menus.risorse.libri.MenuFiltroLibri;
import menus.risorse.libri.MenuScegliGenere;
import menus.risorse.libri.MenuScegliLibro;
import model.Libro;
import myLib.MyMenu;
import view.LibriView;
import view.MessaggiSistemaView;
import myLib.InputDati;

/**
 * Classe che rappresenta l'insieme dei libri presenti in archivio
 * @author Prandini Stefano
 * @author Landi Federico
 */
public class Libri implements Serializable
{
	
	private static final String[] GENERI = {"Fantascienza","Fantasy","Avventura","Horror","Giallo"};
	private static final String TITOLO_MENU_FILTRO = "Scegli in base a cosa filtrare la ricerca: ";
	private static final String[] VOCI_TITOLO_MENU_FILTRO = {"Filtra per titolo", "Filtra per anno di pubblicazione", "Filtra per autore"};
	
	private static final long serialVersionUID = 1L;
	/**
	 * id incrementale univoco per ogni libro
	 */
	private int lastId;
	private Vector<Libro> libri;
	
	/**
	 * costruttore della classe: inizializza il Vector di libri
	 */
	public Libri()
	{
		this.libri = new Vector<Libro>();
		lastId = 0;
	}
	
	public Vector<Libro> getLibri() 
	{
		return libri;
	}
	public void setLibri(Vector<Libro> libri) 
	{
		this.libri = libri;
	}
	
	/**
	 * procedura per l'aggiunta di un libro alla raccolta: chiede all'utente di inserire 
	 * tutti i campi necessari, crea l'oggetto Libro e lo aggiunge al vector
	 */
	public void addLibro()
	{
		String sottoCategoria = this.scegliSottoCategoria();//la sottocategoria della categoria LIBRO (Romanzo, fumetto, poesia,...)
//		se l'utente annulla la procedura
		if(sottoCategoria == "annulla")
		{
			return;
		}
		String genere = this.scegliGenere(sottoCategoria);//se la sottocategoria ha generi disponibili
		
		String titolo = LibriView.chiediTitolo();
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
		
		if(!libroEsistente(l))
		{
			addPerSottoCategorie(l);
			LibriView.aggiuntaRiuscita();
		}
		
		else
		{
			LibriView.aggiuntaNonRiuscita();
		}
	}
	
	/**
	 * presenta all'utente la scelta della sottocategoria di Libro tra quelle presenti in elenco
	 * @return la scelta dell'utente
	 */
	private String scegliSottoCategoria()
	{
		String sottocategoria = MenuSottoCategoriaLibri.show();
		
		return sottocategoria;
	}
	
	/**
	 * inserisco i libri nel Vector in modo che siano ordinati per sottocategorie, cos�, quando vengono stampati, i generi sono in ordine
	 * (il metodo stampaLibri li raccoglier� per generi)
	 * (precondizione: l != null)
	 * @param l il libro da inserire
	 */
	private void addPerSottoCategorie(Libro l)
	{
		if(libri.isEmpty())
		{
			libri.add(l);
		}
		else
		{
			for(int i = 0; i < libri.size(); i++)
			{
				if(libri.get(i).getSottoCategoria().equals(l.getSottoCategoria()))
				{
					libri.add(i+1, l);
					return;
				}
			}
			libri.add(l);
		}
	}

	/**
	 * indica se il libro � gi� presente in archivio
	 * (precondizione: l != null)
	 * @param l il libro da cercare
	 * @return true se il libro � presente in archivio
	 */
	private boolean libroEsistente(Libro l) 
	{
		for(Libro libro : libri)
		{
			if(l.getTitolo().equals(libro.getTitolo()) && l.getAutori().equals(libro.getAutori()) && l.getAnnoPubblicazione()==libro.getAnnoPubblicazione()
					&& l.getCasaEditrice().equals(libro.getCasaEditrice()) && l.getGenere().equals(libro.getGenere()) && l.getLingua().equals(libro.getLingua())
					&& l.getSottoCategoria().equals(libro.getSottoCategoria()) && l.getPagine()==libro.getPagine()
					&& libro.isPrestabile())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * permette la rimozione di un libro da parte dell'operatore: il libro selezionato verr� etichettato come "non Prestabile" ma rimarr� in archivio per 
	 * motivi storici
	 * @return l'id del libro rimosso
	 */
	public String removeLibro()
	{
		String idSelezionato;
		
		String titolo = LibriView.chiediLibroDaRimuovere();
		
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
			LibriView.libroNonPresente();
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
			LibriView.pi�LibriStessoTitolo(titolo);
			
			int pos = 0;
			for(Integer i : posizioniRicorrenze)
			{
				LibriView.numeroRicorrenza(pos);
				MessaggiSistemaView.cornice();
				libri.elementAt((int)i).stampaDati(false);
				MessaggiSistemaView.cornice();
			}
			
			int daRimuovere = LibriView.chiediRicorrenzaDaRimuovere(posizioniRicorrenze);
			
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
	 * stampa i dati dei libri corrispondenti ai parametri di ricerca specificati dall'utente
	 */
	public void cercaLibro()
	{
		MenuFiltroLibri.show(libri,true);
	}
	/**
	 * stampa tutti i libri raggruppandoli per sottocategoria e genere
	 */
	public void stampaLibri()
	{
//		uso "libriDaStampare" cos� quando stampo un libro nella sua categoria posso eliminarlo e non stamparlo di nuovo dopo
		Vector<Libro> libriDaStampare = new Vector<>();
		for(Libro libro : libri)
		{
			if(libro.isPrestabile())
			{
				libriDaStampare.add(libro);
			}
		}
		if(libriDaStampare.size() == 0)
		{
			LibriView.noLibriDisponibili();;
			return;
		}
		
		if(libriDaStampare.size() == 1)
		{
			LibriView.unoLibriInArchivio();
		}
		else//piu di un libro prestabile in archivio
		{
			LibriView.numeroLibriInArchivio(libri);
		}
		for(int j = 0; j < libriDaStampare.size(); j++)
		{				
			MessaggiSistemaView.cornice();
			if(!libriDaStampare.get(j).getGenere().equals("-"))
			{
				LibriView.sottocategoria(libriDaStampare.get(j));
				LibriView.genere(libriDaStampare.get(j));
				MessaggiSistemaView.cornice();
				LibriView.titolo(libriDaStampare.get(j));
//				conteggio al contrario cos� quando elimino un elemento non salto il successivo
				for(int i = libriDaStampare.size()-1; i >= j+1; i--) 
				{
					if(libriDaStampare.get(j).getSottoCategoria().equals(libriDaStampare.get(i).getSottoCategoria()))
					{
						if(libriDaStampare.get(j).getGenere().equals(libriDaStampare.get(i).getGenere()))
						{
							LibriView.titolo(libriDaStampare.get(j));
							libriDaStampare.remove(i);
						}
					}
				}
			}
			else
			{
				LibriView.sottocategoria(libriDaStampare.get(j));
				MessaggiSistemaView.cornice();
				LibriView.titolo(libriDaStampare.get(j));
//				conteggio al contrario cos� quando elimino un elemento non salto il successivo
				for(int i = libriDaStampare.size()-1; i >= j+1; i--)
				{
					if(libriDaStampare.get(j).getGenere().equals(libriDaStampare.get(i).getGenere()))
					{
						LibriView.titolo(libriDaStampare.get(j));
						libriDaStampare.remove(i);
					}
				}
			}
		}
	}
	
	/**
	 * se la sottocategoria di libro ne prevede, presenta all'utente la scelta del genere del libro tra quelli presenti in elenco.
	 * se la sottocategoria non ne prevede restituisce un simbolo di default
	 * (precondizione: sottoCategoria != null)
	 * @param sottoCategoria la sottocategoria di libro che l'utente sta inserendo
	 * @return la scelta dell'utente o "-" se la sottocategoria non prevede generi
	 */
	private String scegliGenere(String sottoCategoria)
	{
		return MenuScegliGenere.show(sottoCategoria);
	}

	/**
	 * Consente all'utente di selezionare un libro in base a dei criteri di ricerca
	 * @return il libro corrispondente ali criteri inseriti dall'utente
	 */
	public Libro scegliLibro() 
	{
		Libro libroSelezionato = MenuScegliLibro.show(libri);
		return libroSelezionato;
	}

}
