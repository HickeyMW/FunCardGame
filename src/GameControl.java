import java.util.ArrayList;

public class GameControl {
	
	private ArrayList<Card> myCards = new ArrayList<>();
	
	private int cardsPlayed = 0;
	private int currentPlayersTurn = 0;
	
	private int player1Score = 0;
	private int player2Score = 0;
	private int player3Score = 0;
	
	private int player1TieBreaker = 0;
	private int player2TieBreaker = 0;
	private int player3TieBreaker = 0;
	
	
	private Card cardPlayedBy1;
	private Card cardPlayedBy2;
	private Card cardPlayedBy3;
	
	public void addCard() {
		
	}
	
	public void nextRound() {
		
	}
	
	public void playedCard(int player, Card card) {
		GUIDummy.playedCard(player, card);
		cardsPlayed++;
		currentPlayersTurn = (currentPlayersTurn + 1) % 3;
	}

}
