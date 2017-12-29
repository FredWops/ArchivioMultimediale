package parte2;
import java.io.Serializable;
import java.util.Vector;

import myLib.InputDati;
import myLib.MyMenu;

public class Libri implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final String[] GENERI = {"Romanzo","Fumetto","Poesia"}; 
	private static final String[] SOTTOGENERI = {"Fantascienza","Fantasy","Avventura","Horror","Giallo"};
	
	private Vector<Libro> libri;
	
	public Libri()
	{
		this.libri = new Vector<Libro>();
	}
	
	public Vector<Libro> getLibri() 
	{
		return libri;
	}
	public void setLibri(Vector<Libro> libri) 
	{
		this.libri = libri;
	}

	public void addLibro()
	{
		String titolo = InputDati.leggiStringaNonVuota("Inserisci il nome del titolo: ");
		Vector<String> autori = new Vector<String>();
		do
		{
			String autore = InputDati.leggiStringaNonVuota("Inserisci l'autore: ");
			autori.add(autore);
		} while(InputDati.yesOrNo("ci sono altri autori? "));
		
		int pagine = InputDati.leggiInteroPositivo("Inserisci il numero delle pagine: ");
		int annoPubblicazione = InputDati.leggiInteroPositivo("Inserisci l'anno di pubblicazione: ");
		String casaEditrice = InputDati.leggiStringaNonVuota("Inserisci la casa editrice: ");
		String lingua = InputDati.leggiStringaNonVuota("Inserisci la lingua del testo: ");
		String genere = this.scegliGenere();
		String sottoGenere = this.scegliSottoGenere(genere);
		int nLicenze = InputDati.leggiInteroPositivo("Inserisci il numero di licenze disponibili: ");
		
		Libro l = new Libro(titolo, autori, pagine, annoPubblicazione, casaEditrice, lingua, genere, sottoGenere, nLicenze);
		libri.add(l);
		System.out.println("Libro aggiunto con successo!");
	}
	
	public void removeLibro()
	{
		String titolo = InputDati.leggiStringaNonVuota("Inserisci il titolo del libro da rimuovere: ");
		for (int i=0; i<libri.size(); i++) 
		{
			if(libri.get(i).getNome().equals(titolo))
			{
				libri.remove(i);
				return;
			}
		}
		System.out.println("Siamo spiacenti, il libro non � presente nell'archivio");
	}
	
	/**
	 * stampa tutti i libri raggruppandoli per genere e sottogenere
	 */
	public void stampaLibri()
	{
		if(libri.isEmpty())
		{
			System.out.println("Nessun libro presente");
			return;
		}
		
		Vector<Libro> listaLibri = libri;
		
		for(int j = 0; j < listaLibri.size(); j++)
		{
			System.out.println("");
			System.out.println("---------------------- ");
			
			if(! listaLibri.get(j).getSottoGenere().equals("-"))
			{
				System.out.println("Genere: " + listaLibri.get(j).getGenere() + ", Sottogenere: "  +
						listaLibri.get(j).getSottoGenere() + "\n");
				System.out.println("titolo: " + listaLibri.get(j).getNome());
				
				for (int i = j+1; i < listaLibri.size(); i++) 
				{
					if(listaLibri.get(j).getGenere().equals(listaLibri.get(i).getGenere()))
					{
						if(listaLibri.get(j).getSottoGenere().equals(listaLibri.get(i).getSottoGenere()))
						{
							System.out.println("titolo: " + listaLibri.get(i).getNome());
							listaLibri.remove(i);
						}
					}
				}
			}
			else
			{
				System.out.println("Genere: " + listaLibri.get(j).getGenere());
				System.out.println("titolo: " + listaLibri.get(j).getNome());
				for (int i=j+1;i<listaLibri.size();i++) 
				{
					if(listaLibri.get(j).getGenere().equals(listaLibri.get(i).getGenere()))
					{
							System.out.println("titolo: " + listaLibri.get(i).getNome());
							listaLibri.remove(i);
					}
				}
			}
		}
	}
	private String scegliGenere()
	{
		MyMenu menu = new MyMenu("scegli un genere", GENERI);
		return GENERI[menu.scegliBase("") - 1];	//stampa il menu (partendo da 1 e non da 0) con i generi e ritorna quello selezionato
	}
	
	private String scegliSottoGenere(String genere)
	{
		if(genere.equals("Romanzo") || genere.equals("Fumetto")) //se si aggiunge un genere va aggiunto anche qui !
		{
			MyMenu menu = new MyMenu("scegli un sotto-genere", SOTTOGENERI);
			return SOTTOGENERI[menu.scegliBase("") - 1];	
		}
		else
		{
			return "-";
		}
	}
}
