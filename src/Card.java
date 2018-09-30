
public class Card {

	private int suit;
	private int value;

	//Suit enums
	public static final int DIAMONDS = 0;
	public static final int CLUBS = 1;
	public static final int HEARTS = 2;
	public static final int SPADES = 3;

	//Suits array
	private static String[] suits = {
			"Diamonds",
			"Clubs",
			"Hearts",
			"Spades"
	};

	//Value enums
	public static final int ACE = 1;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;

	private static String[] values = {
			"Ace",
			"Jack",
			"Queen",
			"King",
	};

	//Card constructor
	Card( int startValue, int startSuite ){
		suit = startSuite;
		value = startValue;
	}

	//Returns the card's suit int
	public int getSuit() {
		return suit;
	}

	//Sets the card's suit int
	public void setSuit( int newSuit ) {
		suit = newSuit;
	}

	//Gets the card's suit name
	public String getSuitName() {
		return getSuitName( suit );
	}

	//Converts a value suit int into a string suit name
	public static String getSuitName( int suit ) {

		//Make sure the value passed in is a valid suit
		if( suit < 0 || suit >= suits.length ) {
			return "invalid suit";
		}

		//Look up the suit in the suits array
		return suits[ suit ];
	}

	//Sets the card's value
	public void setValue( int newValue ) {
		value = newValue;
	}

	//Gets the card's value
	public int getValue() {
		return value;
	}

	//Gets the value as a String and corrects for Ace, King, etc.
	public String getValueName() {
		return getValueName( value );
	}

	//Gets the card's value as a String
	public static String getValueName( int value ) {

		//Make sure the value passed is a valid value
		if( value > 13 || value < 1 ) {
			return "invalid value";
		}

		//Aces make it hard to use an array here
		if( value == 1 ) {
			return values[0];
		}

		//Make sure it needs to be converted
		if( value <= 10 ) {
			return Integer.toString( value );
		}

		//Otherwise look it up in the values array
		return values[ value - 10 ];
	}

	//Converts the card into a presentable format such as "Ace of Spades", "2 of Clubs", etc.
	public String toString() {
		return getValueName() + " of " + getSuitName();
	}

}
