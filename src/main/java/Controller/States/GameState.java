package Controller.States;

import Controller.GameContext;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;

public interface GameState {
    void handleInput(KeyStroke key, GameContext context) throws IOException;
    void draw(GameContext context) throws IOException;
}