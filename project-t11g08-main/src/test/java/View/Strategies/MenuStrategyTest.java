package View.Strategies;

import Model.GameStateData;
import View.GameScreen;
import View.Strategies.MenuStrategy;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MenuStrategyTest {

    private MenuStrategy strategy;
    private GameScreen gameScreenMock;
    private Screen lanternaScreenMock;
    private TextGraphics graphicsMock;

    @BeforeEach
    public void setUp() {
        strategy = new MenuStrategy();

        gameScreenMock = mock(GameScreen.class);
        lanternaScreenMock = mock(Screen.class);
        graphicsMock = mock(TextGraphics.class);

        // Cadeia de mocks: GameScreen -> Screen -> TextGraphics
        when(gameScreenMock.getScreen()).thenReturn(lanternaScreenMock);
        when(lanternaScreenMock.newTextGraphics()).thenReturn(graphicsMock);
        when(lanternaScreenMock.getTerminalSize()).thenReturn(new TerminalSize(80, 24));
    }

    @Test
    public void testDrawMenuOptions() {
        List<String> options = Arrays.asList("JOGAR", "SAIR");

        // Simula desenhar com a opção 0 (JOGAR) selecionada
        strategy.draw(gameScreenMock, new GameStateData(), options, 0);

        // Verifica se desenhou a opção selecionada com a seta ">"
        verify(graphicsMock).putString(anyInt(), anyInt(), contains("> JOGAR <"));

        // Verifica se a opção não selecionada foi desenhada normal
        verify(graphicsMock).putString(anyInt(), anyInt(), eq("SAIR"));

        // Verifica se as cores foram aplicadas
        verify(graphicsMock, atLeastOnce()).setBackgroundColor(GameScreen.COR_FUNDO);
    }

    @Test
    public void testDrawMenuOptions_MathPrecision() {
        List<String> options = Arrays.asList("JOGAR"); // "JOGAR" tem 5 letras

        strategy.draw(gameScreenMock, new GameStateData(), options, 0);

        verify(graphicsMock).putString(eq(36), eq(16), contains("> JOGAR <"));
    }

    @Test
    public void testSelectedOptionColor() {
        List<String> options = Arrays.asList("JOGAR");

        strategy.draw(gameScreenMock, new GameStateData(), options, 0);

        // Verifica se mudou a cor para vermelho (RGB 235, 9, 9) ao desenhar o selecionado
        verify(graphicsMock, atLeastOnce()).setForegroundColor(argThat(color ->
                color.getRed() == 235 && color.getGreen() == 9
        ));
    }
}
