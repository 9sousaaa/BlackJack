package Controller.States;

import Controller.DealerAIController;
import Controller.GameContext;
import Controller.RoundController;
import Util.GameRules;
import Util.Sleeper;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import java.util.Arrays;

public class DealerTurnState implements GameState {
    private final ViewStrategy viewStrategy = new View.Strategies.GameStateStrategy();
    private boolean processed = false;
    private final Sleeper sleeper;

    public DealerTurnState() {
        this.sleeper = millis -> {
            try { Thread.sleep(millis); } catch (InterruptedException e) { throw new RuntimeException(e); }
        };
    }

    // Construtor de teste
    public DealerTurnState(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void handleInput(KeyStroke key, GameContext context) {
        //ignoramos o input do utilizador porque o dealer é automatico
    }
    @Override
    public void draw(GameContext context) throws IOException {
        // Se ainda não processámos a jogada do dealer
        if (!processed) {
            context.getData().setDealerHandVisible(true);
            context.getScreen().render(viewStrategy, context.getData(), java.util.Arrays.asList("DEALER TURN..."), -1);
            context.getScreen().getScreen().refresh();

            sleeper.sleep(1500);
            // Lógica do Dealer com animação (pausa entre cartas)
            // O Dealer pede cartas enquanto tiver menos de 17
            while (context.getData().getDealerHand().calculateValue() < 17) {
                // Dá carta ao dealer
                context.getData().getDealerHand().addCard(context.getData().getDeck().drawCard());

                // Força o desenho no ecrã para vermos a carta nova
                context.getScreen().render(viewStrategy, context.getData(), java.util.Arrays.asList("DEALER HITS..."), -1);
                context.getScreen().getScreen().refresh();

                sleeper.sleep(1500);
            }
            // Calcula o vencedor final
            DealerAIController dealerAI = new DealerAIController();
            RoundController roundCtrl = new RoundController(context.getData(), new GameRules(), dealerAI);
            roundCtrl.endRound();
            String finalMessage = "RESULT: " + context.getData().getMessage();
            context.getData().setMessage("");
            context.getScreen().render(viewStrategy, context.getData(), Arrays.asList(finalMessage), -1);
            context.getScreen().getScreen().refresh();

            // Pequena pausa para ver o resultado antes de mudar o menu
            sleeper.sleep(2000);
            processed = true;
            context.setState(new GameOverState());
        } else {
            // Caso já tenha sido processado, desenha apenas o estado atual
            context.getScreen().render(viewStrategy, context.getData(), java.util.Arrays.asList("..."), 0);
        }
    }
}