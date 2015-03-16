import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Table {

	String welcomeMessage = "*************************************************\n"
			  			  + "*************************************************\n"
  			              + "******                                    *******\n"
 			              + "****** WELCOME TO BLACKJACK ULTIMATE 9000 *******\n"
 			              + "******                                    *******\n"
 		             	  + "*************************************************\n"
 		             	  + "*************************************************\n\n";
	Player[] players;
	int playerNum = 0;
	final int MAX_PLAYERS = 8;
	Dealer dealer;
	
	
	/*
	 * The table is the logical master of the blackjack game.  The highest level decisions and functions are controlled by the table.
	 */
	public Table() {
		setupTable();
		play();
	}
	
	/*
	 * Queries the user to get all players set up and ready to play.
	 */
	private void setupTable() {
				
		//Say hi
		System.out.print(welcomeMessage);
		
		//Get the number of players
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inputPlayerNum = null;
		try {
			System.out.print("How many players today? (1-"+MAX_PLAYERS+") ... ");
			inputPlayerNum = br.readLine();
			playerNum = Blackjack.convertStringNumToInt(inputPlayerNum);

			//make sure we get a valid number here
			while(playerNum < 1 || playerNum > MAX_PLAYERS) {
				System.out.print("Just give me a number between 1 and 8..");
				inputPlayerNum = br.readLine();
				playerNum = Blackjack.convertStringNumToInt(inputPlayerNum);
			}		
		} catch (IOException ioe) {
			System.out.println("There was an error trying to understand the number of players!!");
			System.exit(1);
		}
		System.out.println("Ok so "+playerNum+" players.  Cool.");
		
		//Get the player's names
		players = new Player[playerNum];
		try{
			String playerName;
			for(int i = 0; i < players.length; i++) {
				
				System.out.print("What is player "+(i+1)+"'s name? ... ");
				playerName = br.readLine();
				while(playerName.equals("")){
					System.out.print("Give me a name ... ");
					playerName = br.readLine();
				}
				players[i] = new Player(playerName, i);
			}
			if(players.length == 1) {
				System.out.println("Okay, so it's just you.  Hi "+players[0].name+"!");
			} else {
				System.out.print("Okay so here are our players: ");

				for(int i = 0; i < players.length; i++) {
					if(i == players.length-1){
						System.out.print("and "+players[i].name+".\n");
					}
					else {
						System.out.print(players[i].name+", ");
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("There was an error trying to understand the players' names!!");
			System.exit(1);
		}
		
		System.out.println("Let's find a dealer.");
		
		dealer = new Dealer();
		dealer.sayHi();
	}
	
	/*
	 * Begins by asking player if she/he is ready to play, and plays consecutive rounds until everyone is out of money.
	 */
	private void play() {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Are you ready to play? (y/n) ... ");
		try {
			String response = br.readLine();
			while(!inputIsAffirmative(response)) {
				System.out.print("Are you ready... now? (y/n) ... ");
				response = br.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		System.out.println("Okay let's start!");
		
		while(numPlayersAlive() > 0) {
			playHand();
		}
		
		System.out.println(	"***********************\n"+
							"****** GAME OVER ******\n"+
							"***********************\n");
	}
	
	/*
	 * Plays one round of blackjack, beginning by dealing everyone, and then guiding the players through 
	 * their turns before ending the hand and settling bets.
	 */
	private void playHand() {
				
		for(int i = 0; i < players.length; i++) {
			Player p = players[i];
			if(p.isAlive) {
				dealer.deal(p);
			}
		}
		
		dealer.dealSelf();
		showAll();
		
		while(numPlayersActive() > 0) {
			
			for(int i = 0; i < players.length; i++) {
				while(players[i].hasActiveHands()) {
					dealer.proposition(players[i]);
				}
			}
			dealer.playTurn();			
			showAll();

		}
		
		this.clearTable();
		System.out.println(	"\n"+
							"**********************\n"+
							"***** ROUND OVER *****\n"+
							"**********************\n");

	}
	
	/*
	 * Returns how many players still have chips left and are being dealt every hand.
	 */
	private int numPlayersAlive() {
		int num = 0;
		for(int i = 0; i < players.length; i++) {
			if(players[i].isAlive) num++;
		}
		return num;
	}
	
	/*
	 * Returns the number of players still playing the current hand.
	 */
	private int numPlayersActive() {
		int num = 0;
		for(int i = 0; i < players.length; i++) {
			if(players[i].hasActiveHands() ) num++;
		}
		return num;
	}

	/*
	 * Settles all the bets for all the players and clears the hands.
	 */
	private void clearTable() {
		for(Player p : players) {
			if(p.isAlive) {
				p.settleHands(dealer.getBestScore());
			}
		}
	}
	
	/*
	 * This is a status update for the user, prints all the showing cards on the table.
	 */
	private void showAll() {
		System.out.println("\n****************************");
		for(int i = 0; i < players.length; i++) {
			players[i].showHands();
		}
		dealer.showShowing();
		System.out.println("****************************\n");
	}
	
	
	/*
	 * HELPERS
	 */
	
	/*
	 * Validates the answer to a yes or no question.  Returns true if yes, otherwise returns no.
	 */
	private boolean inputIsAffirmative(String s) {
		return s.toLowerCase().equals("y") || s.toLowerCase().equals("yes");
	}

}
