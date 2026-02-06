package Controller.States;

import Controller.DealerAIController;
import Controller.GameContext;
import Controller.RoundController;
import Util.GameRules;
import Util.Sleeper;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class PlayerTurnState implements GameState {
    private final ViewStrategy viewStrategy = new View.Strategies.GameStateStrategy();
    private final List<String> buttons = Arrays.asList("HIT", "STAND");
    private int selectedOption = 0;
    private final Sleeper sleeper;

    public PlayerTurnState() {
        this.sleeper = millis -> {
            try { Thread.sleep(millis); } catch (InterruptedException e) { throw new RuntimeException(e); }
        };
    }

    // Construtor de Teste
    public PlayerTurnState(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void handleInput(KeyStroke key, GameContext context) throws IOException {
        DealerAIController dealerAI = new DealerAIController();
        GameRules rules = new GameRules();
        RoundController rc = new RoundController(context.getData(), rules, dealerAI);
        // Sai do método para não processar mais input
        if (context.getData().getPlayerHand().calculateValue() == 21) {
            context.setState(new DealerTurnState());
            return;
        }
        if (key.getKeyType() == KeyType.ArrowUp) {
            selectedOption = 0; // Seleciona HIT
        }
        else if (key.getKeyType() == KeyType.ArrowDown) {
            selectedOption = 1; // Seleciona STAND
        }

        if (key.getKeyType() == KeyType.Enter) {
            if (selectedOption == 0) {
                rc.processHit();

                if (context.getData().isGameOver()) {
                    String finalMessage = "RESULT: " + context.getData().getMessage();
                    context.getData().setMessage("");
                    context.getScreen().render(viewStrategy, context.getData(), Arrays.asList(finalMessage), -1);
                    context.getScreen().getScreen().refresh();

                    sleeper.sleep(1000);

                    context.setState(new GameOverState());
                }
                else if (context.getData().getPlayerHand().calculateValue() == 21) {
                    // Mostra a carta que fez o 21
                    context.render();

                    sleeper.sleep(1000);

                    context.setState(new DealerTurnState());
                }
            } else {
                context.setState(new DealerTurnState());
            }
        }
    }

    @Override
    public void draw(GameContext context) throws IOException {
        // Passa a lista de botões e o selecionado para a View
        context.getScreen().render(viewStrategy, context.getData(), buttons, selectedOption);
    }
}