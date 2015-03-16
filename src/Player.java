import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Player{
	public String name = null;
	public int playerNum;
	private int money = 1000;
	private ArrayList<Hand> hands;
	private Hand curHand;
	public boolean isAlive;
	
	/*
	 * Creates a new BlackJack player.
	 */
	public Player(String name, int playerNum) {
		this.name = name;
		this.playerNum = playerNum;
		this.isAlive = true;
		this.hands = new ArrayList<Hand>();
	}
	
	/*
	 * Adds the input Hand to the players hands
	 */
	public void addHand(Hand hand) {
		if(hand.getBestScore() == 21){
			println("Woo! Hit BlackJack with "+hand.getFriendlyName()+"!");
			hand.isActive = false;
		}
		this.hands.add(hand);
		this.updateCurHand();
	}

	/*
	 * Returns true if the player still has one or more active hands, and this is still playing the current round.
	 */
	public boolean hasActiveHands() {
		for(Hand hand : hands) {
			if(hand.isActive) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Prints the players hands.
	 */
	public void showHands() {
		for(Hand hand : hands) {
			println("has: "+hand.getFriendlyName());
		}
	}
	
	/*
	 * Returns true if the players play choice is valid.  Checks against invalid input and illegal plays.
	 */
	private boolean choiceIsValid(String input) {
		if(	input.equals("h") || input.equals("hit") ||
			input.equals("s") || input.equals("stay") ) {
			return true;
		}
		else if (input.equals("d") || input.equals("double down")) {
			if(curHand.bet > this.money) {
				println("You only have $"+this.money+", you're too poor to double down. ");
				return false;
			}
			return true;
		}
		else if (input.equals("sp") || input.equals("split")) {
			if(curHand.bet > this.money) {
				print("You only have $"+this.money+", you're too poor to split. ");
				return false;
			}
			if(!curHand.isSplittable()) {
				println("You can only split when you have two cards of the same rank.");
				return false;
			}

			return true;
		}
		return false;
	}

	/*
	 * Queries the user for her/his play choice.
	 * 0: Stay
	 * 1: Hit
	 * 2: Double down
	 * 3: Split
	 */
	public int getChoice() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		print(this.curHand.getFriendlyName() + ": (h)it, (s)tay, (d)ouble down"+ (this.curHand.isSplittable() ? ", (sp)lit? ... " : "? ... "));
		String input = null;
		try {
			input = br.readLine().toLowerCase();
			while(!choiceIsValid(input)){
				print("Say 'hit' or 'h' to hit, 'stay' or 's' to stay, 'd' or 'double down' to double down"+ (this.curHand.isSplittable() ? ", 'sp' or 'split' to split ... " : " ... "));
				input = br.readLine();
			}
			
			//now we know if they want to hit, double down, or stay
			if(input.equals("s") || input.equals("stay")) { //theyre staying
				return 0;
			} else if(input.startsWith("h")) { //hitting
				return 1;
			} else if(input.startsWith("d")) { //doubling
				return 2;
			} else { //splitting
				return 3;
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return -1;
		}	
	}
	
	/*
	 * Adds the card to the players current hand, prints the result to the user, and ends the hand if player busts or hits 21.
	 */
	public void hit(Card card) {
		println("Adding: "+card.getFriendlyName(false)+" to "+curHand.getFriendlyName());
		curHand.addCard(card);
		if(curHand.getLowestScore() > 21) {
			println("BUSTED with a score of "+curHand.getLowestScore());
			this.endCurrentHand();
		} else if(curHand.getBestScore() == 21) {
			println("21! Nice!");
			this.endCurrentHand();
		}
	}

	/*
	 * Ends the current hand for the player.
	 */
	public void stay() {
		println("Staying with: "+curHand.getFriendlyName()+"and "+curHand.getBestScore()+" points." );
		this.endCurrentHand();
	}
	
	/*
	 * Doubles the bet on the current hand and takes one more card.
	 */
	public void doubleDown(Card card) {
		println("Chose to double down!: "+curHand.getFriendlyName()+" and "+curHand.getHighestScore()+" points." );
		this.money -= curHand.bet;
		curHand.bet *= 2;
		println("Now has a $"+curHand.bet+" hand and $"+this.money+" left over." );
		this.hit(card);
		this.endCurrentHand();
	}
	
	/*
	 * splits the hand into two hands and hits on both.
	 */
	public void splitHand(Card card1, Card card2) {
		Hand newHand1 = new Hand(this.curHand.getCardAt(0), card1, curHand.bet);
		Hand newHand2 = new Hand(this.curHand.getCardAt(1), card2, curHand.bet);
		println("Chose to split! Had "+curHand.getFriendlyName()+"and now has " +newHand1.getFriendlyName()+" and "+newHand2.getFriendlyName());
		this.money -= curHand.bet;
		hands.remove(this.curHand);
		this.addHand(newHand1);
		this.addHand(newHand2);
		updateCurHand();
	}
	
	/*
	 * Queries the user for the bet, takes the bet from the players chips, and returns the bet.
	 */
	public int makeBet() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int betNumber = -1;
		try {
			print("What's your bet? (You have $"+this.money+") ... ");
			String betString = br.readLine();
			betNumber = Blackjack.convertStringNumToInt(betString);

			//make sure we get a valid number here
			while(betNumber < 1 || betNumber > this.money) {
				System.out.print("No, that's not right. Make sure your bet is bigger than 0 but not bigger than "+this.money+" ... ");
				betString = br.readLine();
				betNumber = Blackjack.convertStringNumToInt(betString);
			}
			
		} catch (IOException ioe) {
			System.out.println("There was an error get the bet!!");
			System.exit(1);
		}
		this.money -= betNumber;
		return betNumber;
	}
	
	/*
	 * Called at the end of the round.  Iterates through all hands and settles bets against the dealer's best score.
	 */
	public void settleHands(int dealersBest) {
		for(Hand hand : hands) {
			if(hand.getLowestScore()>21) {
				// i busted :(
				println("busted and lost "+hand.bet+" with a score of "+hand.getLowestScore());
			} else if(dealersBest > 21 || dealersBest < hand.getBestScore()) {  
				//dealer busted or did worse than me :)
				if(hand.isBlackjack()) {
					println("got BlackJack and won $"+((6*hand.bet)/5)+"!");
					this.money += (11 * hand.bet)/5;
				} else {
					println("beat the dealer and won "+hand.bet);
					this.money += 2 * hand.bet;
				}
			} else if(dealersBest == hand.getBestScore()){ //push :|
				println("push with the dealer and got back your "+hand.bet);
				this.money += hand.bet;
			} else {
				//I lost :(
				println("lost "+hand.bet+" bucks :(");
			}
		}
		if(this.money == 0) {
			this.quit();
		}
		hands = new ArrayList<Hand>();
	}
	
	/*
	 * Updates the current hand to be the first active hand, or null if there are no active hands.
	 */
	private void updateCurHand() {
		this.curHand = null;
		for(Hand hand : hands) {
			if(hand.isActive) {
				this.curHand = hand;
			}
		}
	}

	/*
	 * Updates and returns the current hand.
	 */
	public Hand getCurrentHand() {
		this.updateCurHand();
		return this.curHand;
	}
	
	/*
	 * Sets the current hand to inactive and updates the current hand.
	 */
	private void endCurrentHand() {
		if(this.curHand == null) {
			return;
		}
		curHand.isActive = false;
		this.updateCurHand();
	}

	/*
	 * Prints a goodbye message and sets the player inactive from all future rounds.
	 */
	private void quit() {
		println("Bye "+this.name+"!!!!");
		this.isAlive = false;
	}
	
	/*
	 *	Prints the input message on behalf of the player. 
	 */
	private void print(String s) {
		System.out.print("Player "+(playerNum+1)+" ("+name+"): "+s);
	}
	
	/*
	 * Prints the input message on behalf of the player, followed by a newline character.
	 */
	private void println(String s){
		print(s+"\n");
	}
}
