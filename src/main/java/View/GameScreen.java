package View;

import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import javax.swing.JFrame;
import java.util.Arrays;
import java.util.List;
import Model.GameStateData;
import Model.Hand;
import Model.Card;

public class GameScreen {
    private Screen screen;

    // Definir cor de fundo para verde
    public static final TextColor COR_FUNDO = new TextColor.RGB(74, 161, 50);
    // Definir cor da borda para castanho
    public static final TextColor COR_BORDA = new TextColor.RGB(94, 43, 15);

    public GameScreen() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();

        // Cria o terminal
        Terminal terminal = factory.createTerminal();

        // Verifica se é uma janela Swing e maximiza
        this.screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);

        if (terminal instanceof SwingTerminalFrame) {
            SwingTerminalFrame frame = (SwingTerminalFrame) terminal;

            frame.setVisible(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Forçar Swing a aplicar de imediato
            frame.validate();
            frame.repaint();
        }
    }
    // Construtor para testes
    public GameScreen(Screen screen) {
        this.screen = screen;
    }

    public void render(ViewStrategy strategy, GameStateData state, List<String> buttons, int selectedButton) throws IOException {
        if (screen == null) {
            throw new IllegalStateException("ERRO CRÍTICO: 'screen' está null.");
        }
        // Limpa e desenha a base (comum a todos)
        drawBaseLayer();

        // Escolhe o desenho específico para a estratégia
        if (strategy != null) {
            strategy.draw(this, state, buttons, selectedButton);
        }

        // Atualiza o ecrã
        screen.refresh();
    }

    public Screen getScreen() {
        return screen;
    }

    // Método para redesenhar a base da tela
    public void drawBaseLayer() throws IOException {
        TextGraphics tg = screen.newTextGraphics();
        TerminalSize size = screen.getTerminalSize();

        int width = size.getColumns();
        int height = size.getRows();

        // Preencher o CENTRO de Verde
        tg.setBackgroundColor(COR_FUNDO);
        tg.fill(' ');

        // Desenhar a borda
        tg.setBackgroundColor(COR_BORDA);

        // Desenhar barras horizontais (Cima e Baixo) usando espaços
        tg.drawLine(0, 0, width - 1, 0, ' ');           // Topo
        tg.drawLine(0, height - 1, width - 1, height - 1, ' '); // Fundo

        // Desenhar barras verticais (Esquerda e Direita)
        tg.drawLine(0, 0, 0, height - 1, ' ');           // Esquerda
        tg.drawLine(width - 1, 0, width - 1, height - 1, ' '); // Direita
    }
    // Escrever texto no ecrã
    public void drawText(int x, int y, String text, TextColor color) {
        TextGraphics tg = screen.newTextGraphics();
        tg.setBackgroundColor(COR_FUNDO);
        tg.setForegroundColor(color);
        tg.putString(x, y, text);
    }
    // Desenha uma carta
    public void drawCard(int x, int y, Card card) {
        TextGraphics tg = screen.newTextGraphics();
        tg.setBackgroundColor(new TextColor.RGB(250, 250, 250)); // Fundo branco
        String naipeSymbol = card.getNaipe().getSymbol();
        // Escolher cor do naipe
        if (naipeSymbol.equals("♥") || naipeSymbol.equals("♦")) {
            tg.setForegroundColor(new TextColor.RGB(235, 9, 9));
        }
        else {
            tg.setForegroundColor(new TextColor.RGB(0, 0, 0));
        }
        String rankSymbol = card.getRank().getSymbol();
        if ("10".equals(rankSymbol)) {
            tg.putString(x, y, rankSymbol + "     ");
            tg.putString(x, y + 1, "       ");
            tg.putString(x, y + 2, "       ");
            tg.putString(x, y + 3, "   " + naipeSymbol + "   ");
            tg.putString(x, y + 4, "       ");
            tg.putString(x, y + 5, "       ");
            tg.putString(x, y + 6, "     " + rankSymbol);
        }else {
            tg.putString(x, y, rankSymbol + "      ");
            tg.putString(x, y + 1, "       ");
            tg.putString(x, y + 2, "       ");
            tg.putString(x, y + 3, "   " + naipeSymbol + "   ");
            tg.putString(x, y + 4, "       ");
            tg.putString(x, y + 5, "       ");
            tg.putString(x, y + 6, "      " + rankSymbol);
        }
    }
    public void drawHiddenCard(int x, int y) {
        TextGraphics tg = screen.newTextGraphics();
        tg.setBackgroundColor(new TextColor.RGB(20, 50, 150));
        tg.putString(x, y, "       ");
        tg.putString(x, y + 1, "       ");
        tg.putString(x, y + 2, "       ");
        tg.putString(x, y + 3, "       ");
        tg.putString(x, y + 4, "       ");
        tg.putString(x, y + 5, "       ");
        tg.putString(x, y + 6, "       ");
    }
    // Desenha a mão
    public void drawHand(int x, int y, Hand hand, boolean hideSecondCard) {
        int currentX = x;
        List<Card> cards = hand.getCards();
        for(int i = 0; i < cards.size(); i++) {
            if (i == 1 && hideSecondCard) {
                drawHiddenCard(currentX, y);
            } else {
                drawCard(currentX, y, cards.get(i));
            }
            currentX += 8; //Espaço entre cartas
        }
    }


    public void close() throws IOException {
        if (screen != null) {
            screen.stopScreen();
        }
    }

}
