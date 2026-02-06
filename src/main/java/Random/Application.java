package Random;

import java.io.IOException;
import View.GameScreen;
import View.Menu;
import Util.MusicPlayer;

public class Application {
    @SuppressWarnings("CatchAndPrintStackTrace")
    public static void main(String[] args) {
        GameScreen screen = null;
        try {
            // Toca a música de fundo antes do menu
            MusicPlayer.playBackground("background.wav");

            // Inicializa a tela base
            screen = new GameScreen();

            // Menu
            Menu menu = new Menu(screen);
            menu.run();
        } catch (IOException e) {
            throw new RuntimeException("Erro no jogo", e);
        } finally {
            // Fecha a tela se necessário
            if (screen != null) {
                try {
                    screen.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Garante que a música de fundo é parada
            MusicPlayer.stopBackground();
        }
    }
}
