package View.Strategies;

import View.GameScreen;
import View.Strategies.ViewStrategy;
import Model.GameStateData;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.List;

public class BettingStrategy implements ViewStrategy {
    @Override
    public void draw(GameScreen screen, GameStateData state, List<String> buttons, int selectedButton) {
        TerminalSize size = screen.getScreen().getTerminalSize();
        TextGraphics tg = screen.getScreen().newTextGraphics();
        int centerX = size.getColumns() / 2;
        int centerY = size.getRows() / 2;

        tg.setBackgroundColor(GameScreen.COR_FUNDO);
        tg.setForegroundColor(TextColor.ANSI.BLACK);
        String[] title = {
                "██████   █████   █████   █████      █████      █████  ██   ██  █████      █████  ██████   █████   ██████  ████████  █████ ",
                "██      ██   ██ ██      ██   ██    ██   ██    ██      ██   ██ ██   ██    ██   ██ ██   ██ ██   ██ ██          ██    ██   ██",
                "█████   ███████ ██      ███████    ███████     █████  ██   ██ ███████    ███████ ██████  ██   ██  █████      ██    ███████",
                "██      ██   ██ ██      ██   ██    ██   ██         ██ ██   ██ ██   ██    ██   ██ ██      ██   ██      ██     ██    ██   ██",
                "██      ██   ██  █████  ██   ██    ██   ██     █████   █████  ██   ██    ██   ██ ██       █████  ██████      ██    ██   ██",
                "                  ██                                                                                                      ",
                "                 ██                                                                                                       "
        };

        int titleStartY = centerY - 9;
        for (int i = 0; i < title.length; i++) {
            String line = title[i];
            tg.putString(centerX - (line.length() / 2), titleStartY + i, line);
        }
        tg.setForegroundColor(TextColor.ANSI.BLACK);
        String saldoTxt = "Saldo Disponível: $" + state.getBalance();
        tg.putString(centerX - (saldoTxt.length() / 2) - 1, centerY, saldoTxt);

        // Desenha a aposta com setas
        tg.setForegroundColor(new TextColor.RGB(235, 9, 9)); // Vermelho
        String betTxt = "▲  $" + state.getCurrentBet() + "  ▼";
        tg.putString(centerX - (betTxt.length() / 2), centerY + 2, betTxt);

        tg.setForegroundColor(TextColor.ANSI.BLACK);
        tg.putString(centerX - 11, centerY + 4, "[Enter] para Confirmar");
    }
}
