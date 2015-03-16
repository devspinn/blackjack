import java.util.ArrayList;


public class Hand {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	public int bet;
	public boolean isActive;
	
	/*
	 * Constructor based on two cards and the player's bet. Set's isActive to true.
	 */
	public Hand(Card first, Card second, int bet){
		cards.add(first);
		cards.add(second);
		this.isActive = true;
		this.bet = bet;
	}
	
	/*
	 * Returns the string of the friendly name of the hand to print to the user.  
	 * This is a list of all the cards plus the bet on the hand. 
	 */
	public String getFriendlyName() {
		return this.getAllCardsList()+"($"+this.bet+") ";
	}
	
	/*
	 * Returns just a human readable list of the cards in the hand.
	 */
	public String getAllCardsList() {
		String res = "";
		for(Card c : cards) {
			res += c.getFriendlyName(false)+" ";
		}
		return res;
	}

	/*
	 * Returns a human readable list of the showing cards.
	 */
	public String getShowing() {
		String res = "";
		for(Card c : cards) {
			if(c.isShowing()){
				res += c.getFriendlyName(false)+" ";
			}
		}
		return res;
	}

	/*
	 * Returns the lost possible score of the current hand.
	 */
	public int getLowestScore() {
		int res = 0;
		for(Card c : cards) {
			res += c.getLowestScore();
		}
		return res;
	}
	
	/*
	 * Returns the best possible score of the hand (closest to 21).
	 */
	public int getBestScore() {
		int aces = 0;
		int score = 0;
		for(Card card : cards) {
			if(card.getRank() == 0) aces++; //ace
			score += card.getLowestScore();
		}
		while(aces > 0 && score <= 11 ) {
			score += 10;
			aces--;
		}
		return score;
	}
	
	/*
	 * Returns the score, counting aces as 11.
	 */
	public int getHighestScore() {
		int res = 0;
		for(Card c : cards) {
			res += c.getHighestScore();
		}
		return res;
	}
	
	/*
	 * Adds the card to the hand.
	 */
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/*
	 * Returns the card at the specified index.
	 */
	public Card getCardAt(int i) {
		return cards.get(i);
	}
	
	/*
	 * Returns a bool with whether the hand can be split or not.  Only hands with two cards of the same rank can be split.
	 */
	public boolean isSplittable() {
		return (cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank());
	}
	
	/*
	 * Sets all cards to be showing.
	 */
	public void showAll(boolean bool) {
		for(Card card : cards) {
			card.setShowing(true);
		}
	}
	
	/*
	 * returns true if the hand is an ace and a ten/face card.
	 */
	public boolean isBlackjack() {
		return this.cards.size() == 2 && this.getBestScore() == 21;
	}


}
