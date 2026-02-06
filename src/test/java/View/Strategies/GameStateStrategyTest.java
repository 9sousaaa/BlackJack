package View.Strategies;

import Model.Card;
import Model.GameStateData;
import Model.Hand;
import Model.Naipe;
import Model.Rank;
import View.GameScreen;
import View.Strategies.GameStateStrategy;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GameStateStrategyTest {

    @Test
    public void testDrawDelegatesHandDrawing() {
        // Setup Mocks
        GameScreen gameScreenMock = mock(GameScreen.class);
        Screen lanternaScreenMock = mock(Screen.class);
        TextGraphics graphicsMock = mock(TextGraphics.class);

        when(gameScreenMock.getScreen()).thenReturn(lanternaScreenMock);
        when(lanternaScreenMock.newTextGraphics()).thenReturn(graphicsMock);
        when(lanternaScreenMock.getTerminalSize()).thenReturn(new TerminalSize(80, 24));

        // Setup Data
        GameStateData data = new GameStateData();
        data.getPlayerHand().addCard(new Card(Naipe.COPAS, Rank.TEN));

        GameStateStrategy strategy = new GameStateStrategy();

        // Executa
        strategy.draw(gameScreenMock, data, Arrays.asList("HIT", "STAND"), 0);

        // Verifica se a estratégia pediu ao GameScreen para desenhar a mão do jogador
        // Nota: verify(mock).metodo(argumentos)
        verify(gameScreenMock).drawHand(anyInt(), anyInt(), eq(data.getPlayerHand()), eq(false));

        // Verifica se desenhou a mão do dealer
        verify(gameScreenMock).drawHand(anyInt(), anyInt(), eq(data.getDealerHand()), anyBoolean());
    }
}
