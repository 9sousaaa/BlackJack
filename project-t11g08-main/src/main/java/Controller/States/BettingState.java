package Controller.States;

import Controller.DealerAIController;
import Controller.GameContext;
import Model.GameStateData;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import Controller.RoundController;
import Util.GameRules;

public class BettingState implements GameState{
    private final ViewStrategy viewStrategy = new View.Strategies.BettingStrategy();
    @Override
    public void handleInput(KeyStroke key, GameContext context) throws IOException {
        int balance = context.getData().getBalance();
        int bet = context.getData().getCurrentBet();

        if (key.getKeyType() == KeyType.ArrowUp) {

            if (bet + 10 <= balance) context.getData().setCurrentBet(bet + 10);

            else context.getData().setCurrentBet(10);
        }
        else if (key.getKeyType() == KeyType.ArrowDown){

            if (bet - 10 >= 10) context.getData().setCurrentBet(bet - 10);

            else {
                int maxBet = (balance / 10) * 10;
                if (maxBet == 0 && balance > 0) maxBet = balance;
                context.getData().setCurrentBet(maxBet);
            }
        }
        else if (key.getKeyType() == KeyType.Enter) {
            if (bet > 0 && bet <= balance) {
                context.getData().setBalance(balance - bet);
                context.getData().startRound();

                if (context.getData().getPlayerHand().calculateValue() == 21) {
                    context.setState(new DealerTurnState());
                } else {
                    context.setState(new PlayerTurnState());
                }
            } else {
                context.getData().setCurrentBet(balance);
            }
        }
    }
    @Override
    public void draw(GameContext context) throws IOException {
        // Usa o View para desenhar a tela de apostas
        context.getScreen().render(viewStrategy, context.getData(), null, 0);
    }
}