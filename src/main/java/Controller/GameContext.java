package Controller;

import Controller.States.GameState;
import Controller.States.BettingState;
import Model.GameStateData;
import View.GameScreen;

public class GameContext {
    private GameState currentState;
    private GameStateData data;
    private GameScreen screen;

    public GameContext(GameScreen screen) {
        this.screen = screen;
        this.data = new GameStateData();
        this.currentState = new BettingState();
    }

    public void setState(GameState state) { this.currentState = state; }
    public GameStateData getData() { return data; }
    public GameScreen getScreen() { return screen; }
    public GameState getState() {return currentState;}

    public void update(com.googlecode.lanterna.input.KeyStroke key) throws java.io.IOException {
        if (key != null) currentState.handleInput(key, this);
    }

    public void render() throws java.io.IOException {
        currentState.draw(this);
    }

    public void resetForNewRound() {
        int currentBalance = this.data.getBalance();
        this.data = new Model.GameStateData();
        this.data.setBalance(currentBalance);
    }
}