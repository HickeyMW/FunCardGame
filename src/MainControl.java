import java.util.ArrayList;
import java.util.Collections;

public class MainControl {
	
	private int playerId = 0;

	private ArrayList<ArrayList<Card>> playersHands = new ArrayList<>();
	private Card[] playedCards = new Card[3];
	private int[] playerScores = new int[3];
	private int[] tieBreakerScores = new int[3];
	private ArrayList<Card> myCards = new ArrayList<>();

	private NetworkDummy networkDummy;
	
	private int numberOfCardsPlayed = 0;
	private int currentPlayerTurn = 0;

	public MainControl() {
		for (int i = 0; i < 3; i++) {
			playersHands.add(new ArrayList<>());
		}
	}

	//Server methods
	public void HostGame() {
		networkDummy.hostGame();
		playerId = 1;
	}
	
	public void JoinGame(String ip) {
		networkDummy.joinGame(ip);
		networkDummy.listenForGameStart();
	}
	
	public void StartGame() {
		networkDummy.broadcastGameStart();
		ArrayList<Card> deck = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			for (int j = 2; j < 15; j++) {
				deck.add(new Card(j, i));
			}
		}
		Collections.shuffle(deck);
		for (int i = 0; i < 17; i++) {
			for (int j = 1; j < 4; j++) {
				Card nextCard = deck.get(0);
				deck.remove(0);
				playersHands.get(j - 1).add(nextCard);
				if (j == 1) {
					myCards.add(nextCard);
				} else {
					networkDummy.sendCardDealt(j, nextCard);
				}
			}
		}
	}
	
	public void NewGame() {
		
	}

	public void cardPlayedOnServer(int player, Card card) {
		Card hasCard = playerHasCard(playersHands.get(player - 1), card);
		if (hasCard != null) {
			networkDummy.broadcastCardPlayed(player, card);
			playersHands.get(player - 1).remove(hasCard);
			cardPlayedOnClient(player, hasCard);
		}
	}

	//Client methods

	//Called by GUI

	public void playCard(Card card) {
		networkDummy.playCard(card);
	}

	public void startRound() {
		networkDummy.startRound();
	}

	public void startGame() {
		networkDummy.startGame();
	}

	//Called by Network

	public void gameStartedOnClient(int startedByID) {
		networkDummy.listenForDealtCard();
	}

	public void cardDealtOnClient(Card card) {
		myCards.add(card);
		if (myCards.size() != 17) {
			networkDummy.listenForDealtCard();
		} else {
			networkDummy.listenForPlayedCard();
		}
	}
	
	//GameControl methods
	
	private Card playerHasCard(ArrayList<Card> playerCards, Card card) {
		for (Card pCard : playerCards) {
			if (pCard.getSuit() == card.getSuit() && pCard.getValue() == card.getValue()) {
				return pCard;
			}
		}
		return null;
	}


	//Shared methods

	public void cardPlayedOnClient(int player, Card card) {
		playedCards[player - 1] = card;
		numberOfCardsPlayed++;
		currentPlayerTurn = nextPlayerId();

		if (numberOfCardsPlayed == 3) {
			int winnerId = findWinner();
			calculateScoring(winnerId);
			currentPlayerTurn = winnerId;
			if (myCards.size() != 0) {
				if (playerId == 1) {
					networkDummy.listenForRoundStart(winnerId);
				} else {
					networkDummy.listenForRoundStart();
				}
			} else {
				if (playerId == 1) {
					networkDummy.listenForGameStart(findWinner());
				} else {
					networkDummy.listenForGameStart();
				}
			}

		} else if (playerId == 1) {
			networkDummy.listenForCardPlayed(currentPlayerTurn);
		} else {
			networkDummy.listenForPlayedCard();
		}
	}

	private int findWinner() {
		int winner = currentPlayerTurn;
		if (playedCards[currentPlayerTurn - 1].getSuit() == playedCards[nextPlayerId() - 1].getSuit() &&
				playedCards[currentPlayerTurn - 1].getValue() < playedCards[nextPlayerId() - 1].getValue()) {
			winner = nextPlayerId();
		}
		if (playedCards[winner - 1].getSuit() == playedCards[nextNextPlayerId() - 1].getSuit() &&
				playedCards[winner - 1].getValue() < playedCards[nextNextPlayerId() - 1].getValue()) {
			winner = nextPlayerId();
		}
		return winner;
	}

	private int gameWinner() {
		int winner = 1;
		if (playerScores[0] < playerScores[1]) {
			winner = 2;
		} else if (playerScores[0] == playerScores[1] && tieBreakerScores[0] < tieBreakerScores[1]) {
			winner = 2;
		}
		if (playerScores[winner - 1] < playerScores[2]) {
			winner = 3;
		} else if (playerScores[winner - 1] == playerScores[2] && tieBreakerScores[winner - 1] < tieBreakerScores[2]) {
			winner = 3;
		}
		return winner;
	}

	private void calculateScoring(int winnerId) {
		playerScores[winnerId - 1]++;
		for (int i = 0; i < 3; i++) {
			tieBreakerScores[i] += playedCards[i].getValue();
		}
	}

	private int nextPlayerId() {
		return ((currentPlayerTurn + 1) % 4) + 1;
	}

	private int nextNextPlayerId() {
		return ((currentPlayerTurn + 2) % 4) + 1;
	}
}
