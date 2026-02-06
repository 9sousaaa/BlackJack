package Controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;

public class InputHandler {
    private final Screen screen;

    public InputHandler(Screen screen) {
        this.screen = screen;
    }

    // Método para ler input sem bloquear (usado no loop do jogo)
    public KeyStroke getNextKey() throws IOException {
        return screen.pollInput();
    }

    // Método para ler input bloqueante (usado no menu e apostas)
    public KeyStroke readKey() throws IOException {
        return screen.readInput();
    }
}