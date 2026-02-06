package View.Strategies;

import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import javax.swing.text.View;
import java.io.IOException;
import java.util.List;

public class GameStateStrategy implements ViewStrategy {
    @Override
    public void draw(GameScreen screen, GameStateData state, List<String> buttons, int selectedButton) {
        TerminalSize size = screen.getScreen().getTerminalSize();
        TextGraphics tg = screen.getScreen().newTextGraphics();
        int centerX = size.getColumns() / 2;
        //int height = size.getRows();
        boolean hideDealerCard = !state.isDealerHandVisible();

        // Dealer
        String totalValue = state.isDealerHandVisible() ? String.valueOf(state.getDealerHand().calculateValue()) : "?";
        String dealerTxt = "DEALER - Total: " + totalValue;
        screen.drawText(centerX - (dealerTxt.length() / 2), 2, dealerTxt, TextColor.ANSI.BLACK);
        screen.drawHand(centerX - 7 , 4, state.getDealerHand(), hideDealerCard);

        // Player
        String playerTxt = "PLAYER - Total: " + state.getPlayerHand().calculateValue();
        screen.drawText(centerX - (dealerTxt.length() / 2), 12, playerTxt, TextColor.ANSI.BLACK);
        screen.drawHand(centerX - 7, 14, state.getPlayerHand(), false);

        // Saldo e Aposta
        String stats = "Saldo: $" + state.getBalance() + "  |  Aposta: $" + state.getCurrentBet();
        tg.setBackgroundColor(TextColor.ANSI.WHITE_BRIGHT);
        tg.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
        tg.putString(centerX - (stats.length() / 2), 40, stats);

        String msg = state.getMessage();
        if (msg != null && !msg.isEmpty()) {
            screen.drawText(centerX - (msg.length() / 2), 22, msg, TextColor.ANSI.WHITE_BRIGHT);
        }

        drawGameButtons(tg, buttons, selectedButton, centerX, size.getRows());
    }
    public void drawGameButtons(TextGraphics tg, List<String> buttons, int selectedButton, int centerX, int height) {
        if (buttons == null || buttons.isEmpty()) {
            return;
        }
        int startY = height - 8;
        for (int i = 0; i < buttons.size(); i++) {
            String text = buttons.get(i);
            int posY = startY + (i * 2);
            boolean isDisabled = false;
            if (i == selectedButton) {
                tg.setForegroundColor(new TextColor.RGB(235, 9, 9));      // Texto Vermelho
                tg.setBackgroundColor(new TextColor.RGB(252, 250, 250));    // Fundo Branco
                tg.putString(centerX - (text.length() / 2) - 2, posY, "> " + text + " <");
            } else {
                if (isDisabled) {
                    tg.setForegroundColor(TextColor.ANSI.BLACK);
                }else {
                    tg.setForegroundColor(new TextColor.RGB(252, 250, 250));
                }
                tg.setBackgroundColor(GameScreen.COR_FUNDO);
                tg.putString(centerX - (text.length() / 2), posY, text);
            }
        }
    }
}
