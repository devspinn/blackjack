
public class Card {
	private int suit;
	private int rank;
	private boolean showing;
	private String[] suitsShort = {"♥","♠","♦","♣"};
	private String[] suitsLong = {"hearts","spades","diamonds","clubs"};
	private String[] ranksShort = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
	private String[] ranksLong = {"ace","two","three","four","five","six","seven","eight","nine","ten","jack","queen","king"};
	
	/*
	 * Creates a new card with the card id mapping to the specific card. Defaults to showing.
	 */
	public Card(int cardId) {
		if(cardId<0 || cardId > 51)
			System.out.println("error this card was created with an invalid ID: "+cardId);
		suit = cardId / 13;
		rank = cardId % 13;
		showing = true;
	}
	
	/*
	 * Returns true if the card is face up.
	 */
	public boolean isShowing() {
		return showing;
	}
	
	/*
	 * Set the card face up.
	 */
	public void setShowing(boolean faceUp) {
		showing = faceUp;
	}
		
	/*
	 * use 'full' to get a result formatted as "ace of spades".  Otherwise, result will be formatted as "A♠".
	 */
	public String getFriendlyName(boolean full) {
		if(full) return ranksLong[rank]+" of "+suitsLong[suit];
		return ranksShort[rank]+suitsShort[suit];
	}

	/*
	 * Returns the score of the card, counting aces as 1.
	 */
	public int getLowestScore() {
		if(rank < 10) return rank+1; //not a face card
		return 10;		
	}

	/*
	 * Returns the score of the card, counting aces as 11.
	 */
	public int getHighestScore() {
		if(rank == 0) return 11;     //ace
		if(rank < 10) return rank+1; //not a face card
		return 10;
	}
	
	/* 
	 * Returns the rank of the card. Ace = 0, 2 = 1 ... K = 12
	 */
	public int getRank() {
		return this.rank;
	}

}
