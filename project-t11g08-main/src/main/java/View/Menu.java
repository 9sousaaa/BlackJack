package View;

import Controller.InputHandler;
import Controller.GameController;
import View.Strategies.MenuStrategy;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Menu {
    private final ViewStrategy viewStrategy = new View.Strategies.MenuStrategy();
    private final GameScreen gameScreen;
    private InputHandler inputHandler;
    // Opções do menu
    private final List<String> options = Arrays.asList("START GAME", "EXIT");
    // Qual a opção atualmente selecionada (começa na 0)
    private int selectedOption = 0;

    public Menu(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.inputHandler = new InputHandler(gameScreen.getScreen());
    }
    public void run() {
        try {
            while (true) {
                gameScreen.getScreen().doResizeIfNecessary();
                gameScreen.render(viewStrategy, null, options, selectedOption);
                // Espera pelo input
                KeyStroke key = inputHandler.getNextKey();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (key == null) {
                    continue;
                }
                if (key.getKeyType() == KeyType.ArrowUp) {
                    selectedOption--;
                    if (selectedOption < 0) {
                        selectedOption = options.size() - 1;
                    }
                }
                else if (key.getKeyType() == KeyType.ArrowDown) {
                    // Desce na lista (se estiver no fundo, volta ao topo)
                    selectedOption++;
                    if (selectedOption >= options.size()) {
                        selectedOption = 0;
                    }
                }
                else if (key.getKeyType() == KeyType.Enter) {
                    // Executa a ação do botão selecionado
                    if (selectedOption == 0) {
                        startGame();
                    } else if (selectedOption == 1) {
                        exitGame();
                    }
                }
                else if (key.getKeyType() == KeyType.EOF) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Métodos para testes
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // Começar o jogo
    protected void startGame() throws IOException {
        GameController game = new GameController(gameScreen);
        game.run();
    }

    // Sair do jogo
    protected void exitGame() throws IOException {
        gameScreen.getScreen().close();
        System.exit(0);
    }
}

