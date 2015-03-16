import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;


public class Deck {
	
	Stack<Card> cards = new Stack<Card>();
	
	/*
	 * Creates a new deck with all 52 cards, shuffled.
	 */
	public Deck(){
		for(int i = 0; i < 52; i++) {
			cards.push(new Card(i));
		}
		Collections.shuffle(cards);
	}
	
	/*
	 * Returns the card on top of the deck. Throws an exception if the deck is empty.
	 */
	public Card getCard() {
		try{
			return cards.pop();
		} catch (EmptyStackException ese) {
			System.out.println("deck is complete!!");
			return null;
		}
	}
	/*
	 * Returns the number of cards left in the deck.
	 */
	public int getSize(){
		return cards.size();
	}

}
