package Model;

public class GameStateData {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private boolean playerTurn;
    private boolean gameOver;
    private String message;
    private int balance;
    private int currentBet;
    private boolean dealerHandVisible;
    public GameStateData () {
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.playerTurn = true;
        this.gameOver = false;
        this.dealerHandVisible = false;
        this.balance = 1000;
        this.currentBet = 0;
    }
    // Dar cartas iniciais logo ao criar o jogo
    public void startRound() {
        this.dealerHandVisible = false;
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
    }

    public int getBalance() {return balance; }
    public int getCurrentBet() {return currentBet; }
    public void setBalance(int balance) { this.balance = balance; }
    public void setCurrentBet(int currentBet) { this.currentBet = currentBet; }
    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public Deck getDeck() { return deck; }
    public boolean isPlayerTurn() { return playerTurn; }
    public void setPlayerTurn(boolean playerTurn) { this.playerTurn = playerTurn; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isDealerHandVisible() { return dealerHandVisible; }
    public void setDealerHandVisible(boolean visible) { this.dealerHandVisible = visible; }
}