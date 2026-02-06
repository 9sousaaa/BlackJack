package View.Strategies;

import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.io.IOException;
import java.util.List;

public class MenuStrategy implements ViewStrategy{
    @Override
    public void draw(GameScreen screen, GameStateData data, List<String> buttons, int selectedButton) {
        TerminalSize size = screen.getScreen().getTerminalSize();
        TextGraphics tg = screen.getScreen().newTextGraphics();
        int centerX = size.getColumns() / 2;
        int centerY = size.getRows() / 2;
        //Desenho do Título
        String[] title = {
                "██ ███    ██ ███████  █████  ███    ██ ███████ ",
                "██ ████   ██ ██      ██   ██ ████   ██ ██      ",
                "██ ██ ██  ██ ███████ ███████ ██ ██  ██ █████   ",
                "██ ██  ██ ██      ██ ██   ██ ██  ██ ██ ██      ",
                "██ ██   ████ ███████ ██   ██ ██   ████ ███████ ",
                "                                               ",
                "                                                                        ",
                "██████  ██       █████   ██████ ██   ██      ██  █████   ██████ ██   ██ ",
                "██   ██ ██      ██   ██ ██      ██  ██       ██ ██   ██ ██      ██  ██  ",
                "██████  ██      ███████ ██      █████        ██ ███████ ██      █████   ",
                "██   ██ ██      ██   ██ ██      ██  ██  ██   ██ ██   ██ ██      ██  ██  ",
                "██████  ███████ ██   ██  ██████ ██   ██  █████  ██   ██  ██████ ██   ██ ",
                "                                                                        "
        };
        int titleStartY = centerY - 10;
        tg.setForegroundColor(new TextColor.RGB(0, 0, 0));
        tg.setBackgroundColor(GameScreen.COR_FUNDO);
        for (int i = 0; i < title.length; i++) {
            String line = title[i];
            // Desenha centrado horizontalmente
            tg.putString(centerX - (line.length() / 2), titleStartY + i, line);
        }
        for (int i = 0; i < buttons.size(); i++) {
            String text = buttons.get(i);
            int posY = centerY + (i * 2) + 4;
            if (i == selectedButton) {
                tg.setForegroundColor(new TextColor.RGB(235, 9, 9));      // Texto Vermelho
                tg.setBackgroundColor(new TextColor.RGB(252, 250, 250));    // Fundo Branco
                tg.putString(centerX - (text.length() / 2) - 2, posY, "> " + text + " <");
            } else {
                tg.setForegroundColor(new TextColor.RGB(252, 250, 250));
                tg.setBackgroundColor(GameScreen.COR_FUNDO);
                tg.putString(centerX - (text.length() / 2), posY, text);
            }
        }
    }
}
