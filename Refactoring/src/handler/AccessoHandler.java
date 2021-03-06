package handler;

import controllerMVC.RisorseController;
import controllerMVC.FruitoriController;
import controllerMVC.PrestitiController;
import controllerMVC.StoricoController;
import command.AccessoFruitoreCommand;
import command.AccessoOperatoreCommand;
import command.ICommand;
import interfaces.ISavesManager;
import view.MessaggiSistemaView;
import viewInterfaces.IMessaggiSistemaView;
/**
 * Handler che gestisce le operazioni di sistema, delegando alle opportune classi, in base alla scelta dell'utente all'interno di un menu.
 * qui in particolare vengono gestite le opzioni del menu in cui si seleziona il tipo di utente
 * in questa classe non � pi� richiesta l'interazione con l'utente
 * @author Stefano Prandini
 * @author Federico Landi
 *
 */
public class AccessoHandler 
{
	private FruitoreHandler fruitoreHandler;
	private OperatoreHandler operatoreHandler;
	private IMessaggiSistemaView messaggiSistemaView;
	
//	PATTERN COMMAND: sono i comandi del menu
	ICommand accessoFruitore;
	ICommand accessoOperatore;
		
	public AccessoHandler(ISavesManager gestoreSalvataggi, RisorseController risorseController,
			FruitoriController fruitoriController, StoricoController storicoController, PrestitiController prestitiController)
	{
		fruitoreHandler = new FruitoreHandler(fruitoriController, risorseController, prestitiController, gestoreSalvataggi);
		operatoreHandler = new OperatoreHandler(fruitoriController, risorseController, prestitiController, storicoController, gestoreSalvataggi);
		messaggiSistemaView = MessaggiSistemaView.getInstance();
		
//		PATTERN COMMAND: sono i comandi del menu
		accessoFruitore = new AccessoFruitoreCommand(fruitoreHandler);
		accessoOperatore = new AccessoOperatoreCommand(operatoreHandler);
	}
	
	/**
	 * menu iniziale: si sceglie se si vuole accedere come fruitore (1) o come operatore (2)
	 * @param scelta la scelta selezionata dall'utente
	 */
	public boolean gestisciAccesso(int scelta) 
	{
		boolean terminato = false;

		switch(scelta)
		{
			case 0://EXIT
			{
				messaggiSistemaView.stampaAddio();
				terminato = true;
				break;
			}
			case 1://accesso FRUITORE
			{			
				accessoFruitore.gestisciAccesso();
				
				terminato = false;
				break;
			}
			case 2://accesso OPERATORE
			{
				accessoOperatore.gestisciAccesso();
				
				terminato = false;
				break;
			}
			default:
			{
				terminato = true;
			}
		}
		return terminato;
	}
}