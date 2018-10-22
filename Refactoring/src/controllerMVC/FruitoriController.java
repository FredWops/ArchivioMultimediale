package controllerMVC;

import java.util.GregorianCalendar;
import java.util.Vector;
import model.Fruitore;
import model.Fruitori;
import myLib.GestioneDate;
import view.FruitoriView;

public class FruitoriController 
{
	private Fruitori model;
	
	public FruitoriController(Fruitori fruitori)
	{
		model = fruitori;
	}
	
	/**
	 * Crea e aggiunge un Fruitore, se maggiorenne al vettore "fruitori"
	 */
	public void addFruitore()
	{
		String nome = FruitoriView.chiediNome();
		String cognome = FruitoriView.chiediCognome();
		GregorianCalendar dataNascita = FruitoriView.chiediDataNascita();
		
		//controllo che l'utente sia maggiorenne
		if(GestioneDate.differenzaAnniDaOggi(dataNascita) < 18)
		{
			FruitoriView.messaggioUtenteMinorenne();
			return;
		}
		
		String user;
		do
		{
			user = FruitoriView.chiediUsername();
			if(!model.usernameDisponibile(user))
			{
				FruitoriView.UsernameNonDisponibile();
			}
		}
		while(!model.usernameDisponibile(user));
				
		String password1;
		String password2;
		boolean corretta = false;
		do
		{
			password1 = FruitoriView.chiediPassword();
			password2 = FruitoriView.confermaPassword();
			
			if(password1.equals(password2)) 
			{
				corretta = true;
			}
			else
			{
				FruitoriView.passwordNonCoincidono();
			}
		}
		while(!corretta);
		
		GregorianCalendar dataIscrizione = GestioneDate.DATA_CORRENTE;

		if(FruitoriView.confermaDati())
		{
//			creo il nuovo fruitore
			Fruitore f = new Fruitore(nome, cognome, dataNascita, dataIscrizione, user, password1); 
//			aggiungo al vector fruitori il nuovo fruitore
			model.addFruitore(f);
			FruitoriView.confermaIscrizione();
		}
		else
		{
			FruitoriView.nonConfermaIscrizione();
		}
	}
	
	/**
	 * Controllo se sono passati 5 anni dala data di iscrizione. Se sono passati i 5 anni assegna al fruitore lo status di "decaduto"
	 * @return un vettore contenente gli utenti eliminati: verr� poi utilizzato per rimuovere i prestiti di questi utenti decaduti
	 */
	public Vector<Fruitore> controlloIscrizioni()
	{
		Vector<Fruitore>utentiRimossi = new Vector<>();
		int rimossi = 0;
		for(Fruitore fruitore : model.getFruitori()) 
		{
			if((!fruitore.isDecaduto()) && fruitore.getDataScadenza().compareTo(GestioneDate.DATA_CORRENTE) < 0)//se dataScadenza � precedente a oggi ritorna -1
			{
				fruitore.setDecaduto(true);
				utentiRimossi.add(fruitore);
				rimossi++;
			}
		}
		FruitoriView.utentiRimossi(rimossi);
		return utentiRimossi;
	}
	
	/**
	 * metodo per il test, cos� non stampa in console
	 * @param test se true non stampa in console
	 */
	public Vector<Fruitore> controlloIscrizioni(boolean test)
	{
		Vector<Fruitore>utentiRimossi = new Vector<>();
		int rimossi = 0;
		for(Fruitore fruitore : model.getFruitori()) 
		{
			if((!fruitore.isDecaduto()) && fruitore.getDataScadenza().compareTo(GestioneDate.DATA_CORRENTE) < 0)//se dataScadenza � precedente a oggi ritorna -1
			{
				fruitore.setDecaduto(true);
				utentiRimossi.add(fruitore);
				rimossi++;
			}
		}
		if(!test)
		{
			FruitoriView.utentiRimossi(rimossi);
		}
		return utentiRimossi;
	}
	
	/**
	 * interagisce con l'utente chiedendogli le credenziali.
	 * @return true se il login � andato a buon fine
	 */
	public Fruitore login() 
	{
		String user = FruitoriView.chiediUsername();
		String password = FruitoriView.chiediPassword();
		
		Fruitore utenteLoggato = model.trovaUtente(user, password);
		
		if(utenteLoggato == null)
		{
			FruitoriView.utenteNonTrovato();
			return null;
		}
		else // -> utente trovato
		{
			FruitoriView.benvenuto(utenteLoggato.getNome());
			return utenteLoggato;
		}	
	}
	
	public void stampaDatiFruitori()
	{
		FruitoriView.stampaDati(model.getFruitori());
	}
	
	public void stampaDatiFruitore(Fruitore f)
	{
		FruitoriView.stampaDati(f);
	}
	
	public void rinnovo(Fruitore fruitore)
	{
		boolean riuscito = fruitore.rinnovo();
		if(riuscito)
		{
			FruitoriView.iscrizioneRinnovata();
		}
		else
		{
			FruitoriView.iscrizioneNonRinnovata(fruitore.getDataInizioRinnovo(), fruitore.getDataScadenza());
		}
	}	
}