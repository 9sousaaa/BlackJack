package View.Strategies;

import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.TerminalSize;
import java.util.List;

public interface ViewStrategy {
    void draw(GameScreen screen, GameStateData state, List<String> buttons, int selectedButton);
}

