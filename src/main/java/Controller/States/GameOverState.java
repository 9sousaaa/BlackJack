package Controller.States;

import Controller.GameContext;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import Util.*;

public class GameOverState implements GameState {
    private final ViewStrategy viewStrategy = new View.Strategies.GameStateStrategy();
    private final List<String> options = Arrays.asList("PLAY AGAIN", "EXIT");
    private int selectedOption = 0;

    @Override
    public void handleInput(KeyStroke key, GameContext context) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            selectedOption = 0;
        }
        else if (key.getKeyType() == KeyType.ArrowDown) {
            selectedOption = 1;
        }
        else if (key.getKeyType() == KeyType.Enter) {
            if (selectedOption == 0) {
                // Se tiver saldo, joga de novo. Se for 0, dá reset ao dinheiro inicial.
                if (context.getData().getBalance() <= 0) {
                    context.getData().setBalance(1000); // Reset para 1000 se perdeu tudo
                }

                MusicPlayer.playBackground("background.wav");
                context.resetForNewRound();
                context.setState(new BettingState());

            } else {
                MusicPlayer.stopBackground();
                context.getData().setBalance(-1);
            }
        }
    }
    @Override
    public void draw(GameContext context) throws IOException {
        context.getData().setDealerHandVisible(true);

        context.getData().setMessage("");

        context.getScreen().render(viewStrategy, context.getData(), options, selectedOption);
    }
}