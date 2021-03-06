package model;

import java.io.Serializable;
import java.util.Vector;

import com.sun.corba.se.impl.io.TypeMismatchException;

import interfaces.Risorsa;

/**
 * Classe che rappresenta la descrizione di una risorsa multimediale di tipo Libro
 * @author Prandini Stefano
 * @author Landi Federico
 */
public class Libro implements Risorsa, Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * utile per confrontare risorse con categoria selezionata da utente
	 */
	private static final String CATEGORIA = "Libri";

	/********************************************************************
	 * ogni categoria ha i suoi vincoli per quanto riguarda i PRESTITI: *
	 ********************************************************************/
	/**
	 * quanto tempo un Libro pu� restare in prestito ad un fruitore
	 */
	private static final int GIORNI_DURATA_PRESTITO = 20;
	/**
	 * quanto dura una proroga del prestito di un Libro
	 */
	private static final int GIORNI_DURATA_PROROGA = 20;
	/**
	 * quanti giorni prima della scadenza si pu� chiedere una proroga del prestito del Libro
	 */
	private static final int GIORNI_PRIMA_PER_PROROGA = -5;
	/**
	 * quanti Libri possono essere in prestito contemporaneamente allo stesso fruitore
	 */
	private static final int PRESTITI_MAX = 3;
	/**
	 * ID univoco del libro
	 */
	private String id;
	/**
	 * sottocategorie della categoria LIBRO: Romanzo, Fumetto, Poesia...
	 */
	private String sottoCategoria;
	private String titolo;
	private Vector<String> autori = new Vector<>();
	private int pagine;
	private int annoDiUscita;
	private String casaEditrice;
	private String lingua;
	private String genere;
	private int nLicenze;
	private boolean prestabile;
	/**
	 * quante copie di questo libro sono gi� in prestito (<= nLicenze)
	 */
	private int inPrestito;
	
	/**
	 * Costuttore della classe libro
	 * @param id l'id univoco del libro
	 * @param sottoCategoria la sottocategoria della categoria LIBRO (es. Romanzo, Fumetto, Poesia...)
	 * @param titolo il titolo del libro
	 * @param autori il vector degli autori del libro
	 * @param pagine il numero di pagine
	 * @param annoPubblicazione l'anno di pubblicazione
	 * @param casaEditrice la casa editrice
	 * @param lingua la lingua del testo
	 * @param genere il genere del libro ( "-" se il genere non ha sottogeneri)
	 * @param nLicenze il numero di licenze disponibili
	 */
	public Libro(String id, String sottoCategoria, String titolo, Vector<String> autori, int pagine, int getAnnoDiUscita, String casaEditrice,
			String lingua, String genere, int nLicenze) 
	{
		this.id = (id);
		this.sottoCategoria = (sottoCategoria);
		this.titolo = (titolo);
		this.autori = (autori);
		this.pagine = (pagine);
		this.annoDiUscita = (getAnnoDiUscita);
		this.casaEditrice = (casaEditrice);
		this.lingua = (lingua);
		this.genere = (genere);
		this.nLicenze = (nLicenze);
		this.setInPrestito(0);
		this.prestabile = true;
	}
	
	public boolean equals(Risorsa r)
	{
		if(this.id.equals(r.getId()))
		{
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean stessiAttributi(Risorsa r) 
	{
		if(r instanceof Libro)
		{
//			� istanza di Libro, quindi posso fare il casting per poter usare i metodi di Libro
			Libro l=(Libro)r;
			
			if(l.getTitolo().equals(titolo) && l.getAutori().equals(autori) && l.getAnnoDiUscita()==annoDiUscita
					&& l.getCasaEditrice().equals(casaEditrice) && l.getGenere().equals(genere) && l.getLingua().equals(lingua)
					&& l.getSottoCategoria().equals(sottoCategoria) && l.getPagine()==pagine && prestabile)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new TypeMismatchException();
		}
	}
	
	@Override
	public String toString(boolean perPrestito) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Categoria-----------------: Libro\n");
		sb.append("Sottocategoria------------: " + sottoCategoria + "\n");
		sb.append("Titolo--------------------: " + titolo + "\n");
		sb.append("Autori--------------------:");
		for(int i = 0; i < autori.size(); i++)
		{
			sb.append(" " + autori.elementAt(i));
			if(i < autori.size()-1)
			{
				sb.append(",");
			}
			else sb.append("\n");
		}
		if(!genere.equals("-"))
		{
			sb.append("Genere--------------------: " + genere + "\n");
		}
		sb.append("Numero pagine-------------: " + pagine + "\n");
		sb.append("Anno di pubblicazione-----: " + annoDiUscita + "\n");
		sb.append("Casa editrice-------------: " + casaEditrice + "\n");
		sb.append("Lingua--------------------: " + lingua + "\n");
		if(!perPrestito)//dati utili all'operatore
		{
			sb.append("Numero licenze------------: " + nLicenze + "\n");
			sb.append("In prestito---------------: " + inPrestito + "\n");
		}
		else//dati utili al fruitore
		{
			sb.append("Copie disponibili---------: " + (nLicenze - inPrestito) + "\n");
		}

		return sb.toString();
	}
	
	public String getCategoria() 
	{
		return CATEGORIA;
	}
	public String getId() 
	{
		return id;
	}
	public String getTitolo()
	{
		return titolo;
	}
	public Vector<String> getAutori() 
	{
		return autori;
	}
	public int getPagine()
	{
		return pagine;
	}
	public int getAnnoDiUscita() 
	{
		return annoDiUscita;
	}
	public String getCasaEditrice() 
	{
		return casaEditrice;
	}
	public String getLingua() 
	{
		return lingua;
	}
	public String getSottoCategoria() 
	{
		return sottoCategoria;
	}
	public String getGenere() 
	{
		return genere;
	}
	public int getNLicenze() 
	{
		return nLicenze;
	}
	public int getInPrestito() 
	{
		return inPrestito;
	}
	public void setInPrestito(int inPrestito)
	{
		this.inPrestito = inPrestito;
	}
	public boolean isPrestabile() 
	{
		return prestabile;
	}
	public void setPrestabile(boolean prestabile) 
	{
		this.prestabile = prestabile;
	}
	public void mandaInPrestito() 
	{
		inPrestito++;
	}
		public void tornaDalPrestito()
	{
		inPrestito--;
	}
	public int getGiorniDurataPrestito() 
	{
		return Libro.GIORNI_DURATA_PRESTITO;
	}
	public int getGiorniDurataProroga() 
	{
		return Libro.GIORNI_DURATA_PROROGA;
	}
	public int getGiorniPrimaPerProroga() 
	{
		return Libro.GIORNI_PRIMA_PER_PROROGA;
	}
	public int getPrestitiMax() 
	{
		return Libro.PRESTITI_MAX;
	}
}