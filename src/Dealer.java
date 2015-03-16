

public class Dealer {
	int age;
	String name;
	Deck deck;
	Hand hand;
//	boolean isActive;
	
	/*
	 * Creates a new dealer with a random name and a new deck of shuffled cards.
	 */
	public Dealer(){
		this.name = getRandomName();
		deck = new Deck();
	}
	
	/*
	 * Prints a welcome message to be polite to our players.
	 */
	public void sayHi(){
		println("Hi i'm "+name+"!  I've been dealing here for "+((int) (Math.random()*24)+1)+" years and I just love it.");
	}
	
	/*
	 * Gets a card from the deck.  If the deck is empty, get's a new deck first.
	 */
	public Card grabCard(){
		if(deck.getSize() == 0){
			deck = new Deck();
		}
		return deck.getCard();
	}

	/*
	 * Takes the bet from the player and gives her/him a new hand.
	 */
	public void deal(Player player) {
		player.addHand(getNewHand(player.makeBet()));
	}
	
	/*
	 * Get's the players choice.  Depending on the players hand, she can stay, hit, double down, or split.
	 */
	public void proposition(Player player) {
		int choice = player.getChoice();
		if(choice == 0) { //stay
			player.stay();
		} else if (choice == 1) { //hit
			player.hit(grabCard());
		} else if (choice == 2) { //double
			player.doubleDown(grabCard());
		} else if (choice == 3) { //split
			player.splitHand(grabCard(), grabCard());
		}
	}
	
	/*
	 * Returns the dealer's best score, which is closest to 21 without going over.
	 */
	public int getBestScore() {
		return hand.getBestScore();
	}
	
	/*
	 * Creates a new hand from the deck with the given bet.
	 */
	private Hand getNewHand(int bet) {
		return new Hand(grabCard(), grabCard(), bet);
	}
	
	/*
	 * Deal self one card down and one card up.
	 */
	public void dealSelf() {
		Card downCard = grabCard();
		downCard.setShowing(false);
		hand = new Hand(downCard, grabCard(), 0);
		this.hand.isActive = true;
	}
	
	/*
	 * Prints the dealer's hand.
	 */
	public void showHand() {
		println("has: "+hand.getFriendlyName()+" "+hand.getBestScore()+" points.");
	}
	
	/*
	 * Prints only the showing cards.
	 */
	public void showShowing() {
		println((this.hand.isActive ? "showing: " : "has: ")+hand.getShowing());
	}

	/*
	 * After the players have finished, dealer plays her/his turn.  Stays on soft 17.
	 */
	public void playTurn() {
		while(this.hand.isActive) {
			if(hand.getBestScore() > 16) {
				stay();
			} else {
				hit();
			}
		}
	}
	
	/*
	 * Shows all cards and ends hand.
	 */
	private void endHand(){
		this.hand.showAll(true);
		this.hand.isActive = false;
	}
	
	/*
	 * Takes an additional card, and ends the hand if goes over 21.
	 */
	private void hit() {
		hand.addCard(grabCard());
		println("hit and now has: "+hand.getAllCardsList()+"or "+hand.getBestScore()+" points.");
		if(hand.getLowestScore() > 21) {
			System.out.println("BUSTED with " + hand.getLowestScore() + "!");
			endHand();
		}
	}
	
	/*
	 * Prints this action, and ends the hand.
	 */
	private void stay() {
		println("staying with: "+hand.getAllCardsList()+"or "+hand.getBestScore()+" points.");
		endHand();
	}
	
	/*
	 * Helper function to print messages on the dealer's behalf.
	 */
	private void println(String s) {
		System.out.println("Dealer ("+name+"): "+s);
	}
	
	/*
	 * Helper function to get a name for our dealer.
	 */
	private String getRandomName() {
		String[] names = {"Bill","Mikey","Francis","Paul","Emily","Adam","Freddy"};
		return names[(int) (Math.random()*names.length)];
	}
	
}
