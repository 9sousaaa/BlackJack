package View.Strategies;

import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BettingStrategyTest {

    @Test
    public void testDrawBetInfo_ExactCoordinates() {
        GameScreen gameScreenMock = mock(GameScreen.class);
        Screen lanternaScreenMock = mock(Screen.class);
        TextGraphics graphicsMock = mock(TextGraphics.class);

        when(gameScreenMock.getScreen()).thenReturn(lanternaScreenMock);
        when(lanternaScreenMock.newTextGraphics()).thenReturn(graphicsMock);
        when(lanternaScreenMock.getTerminalSize()).thenReturn(new TerminalSize(80, 24));

        GameStateData data = new GameStateData();
        data.setBalance(500);
        data.setCurrentBet(50);

        BettingStrategy strategy = new BettingStrategy();
        strategy.draw(gameScreenMock, data, null, 0);

        verify(graphicsMock).putString(anyInt(), eq(3), contains("██"));

        verify(graphicsMock).putString(anyInt(), eq(12), contains("500"));

        verify(graphicsMock).putString(anyInt(), eq(14), contains("50"));

        verify(graphicsMock, atLeastOnce()).setBackgroundColor(GameScreen.COR_FUNDO);
        verify(graphicsMock, atLeastOnce()).setForegroundColor(TextColor.ANSI.BLACK);
    }
}