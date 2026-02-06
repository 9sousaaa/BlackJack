package Controller;

import View.GameScreen;
import java.io.IOException;
import com.googlecode.lanterna.input.KeyStroke;

public class GameController {
    private GameScreen screen;
    private InputHandler inputHandler;
    private final GameContext context;
    private boolean isRunning;

    public GameController (GameScreen screen) {
        this.screen = screen;
        this.inputHandler = new InputHandler(screen.getScreen());
        this.context = new GameContext(screen);
        this.isRunning = true;
    }
    // Construtor para testes
    public GameController(GameScreen screen, InputHandler inputHandler, GameContext context) {
        this.screen = screen;
        this.inputHandler = inputHandler;
        this.context = context;
        this.isRunning = true;
    }
    public void run() throws IOException {
        while(isRunning) {

            KeyStroke key = inputHandler.getNextKey();

            context.update(key);
            context.render();

            // Verifica se o utilizador escolheu sair (podes definir balance = -1 para sair)
            if (context.getData().getBalance() == -1) {
                isRunning = false;
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        screen.getScreen().close();
    }
}
