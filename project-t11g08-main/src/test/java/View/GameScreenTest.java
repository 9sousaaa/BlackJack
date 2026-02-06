package View;

import Model.Card;
import Model.GameStateData;
import Model.Hand;
import Model.Naipe;
import Model.Rank;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GameScreenTest {

    private Screen screenMock;
    private TextGraphics graphicsMock;
    private GameScreen gameScreen;

    @BeforeEach
    public void setUp() {
        screenMock = mock(Screen.class);
        graphicsMock = mock(TextGraphics.class);

        // Tamanho falso para o drawBaseLayer não rebentar
        TerminalSize mockSize = new TerminalSize(80, 24);

        // Configura o tamanho para drawBaseLayer
        when(screenMock.getTerminalSize()).thenReturn(mockSize);

        // Mock sempre que o código pedir graphics
        when(screenMock.newTextGraphics()).thenReturn(graphicsMock);

        gameScreen = new GameScreen(screenMock);
    }

    @Test
    public void testRender() throws IOException {
        ViewStrategy strategyMock = mock(ViewStrategy.class);
        GameStateData dataMock = mock(GameStateData.class);

        gameScreen.render(strategyMock, dataMock, Collections.emptyList(), 0);

        verify(strategyMock).draw(eq(gameScreen), eq(dataMock), anyList(), anyInt());
        verify(screenMock).refresh();
    }

    @Test
    public void testDrawText() {
        gameScreen.drawText(10, 10, "Hello", TextColor.ANSI.RED);

        verify(graphicsMock).setForegroundColor(TextColor.ANSI.RED);
        verify(graphicsMock).putString(10, 10, "Hello");
    }

    @Test
    public void testDrawCard_RedSuit() {
        // Testa Ouros/Copas (cor vermelha)
        Card card = new Card(Naipe.OUROS, Rank.ACE);

        gameScreen.drawCard(5, 5, card);

        // Verifica se configurou a cor do texto
        verify(graphicsMock, atLeastOnce()).setForegroundColor(new TextColor.RGB(235, 9, 9));

        // Verifica se desenhou o simbolo
        verify(graphicsMock, atLeastOnce()).putString(anyInt(), anyInt(), contains("♦"));
    }

    @Test
    public void testDrawCard_BlackSuit() {
        // Testa Espadas/Paus (cor preta)
        Card card = new Card(Naipe.ESPADAS, Rank.KING);

        gameScreen.drawCard(10, 10, card);

        verify(graphicsMock, atLeastOnce()).setForegroundColor(new TextColor.RGB(0, 0, 0));
        verify(graphicsMock, atLeastOnce()).putString(anyInt(), anyInt(), contains("♠"));
    }

    @Test
    public void testDrawHiddenCard() {
        gameScreen.drawHiddenCard(2, 2);

        // Verifica se a carta escondida é azul
        verify(graphicsMock).setBackgroundColor(new TextColor.RGB(20, 50, 150));

        // Verifica se desenha blocos vazios
        verify(graphicsMock, atLeastOnce()).putString(eq(2), anyInt(), eq("       "));
    }

    @Test
    public void testDrawHand_AllVisible() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.COPAS, Rank.TEN));
        hand.addCard(new Card(Naipe.PAUS, Rank.FIVE));

        // false = não esconder nenhuma carta
        gameScreen.drawHand(0, 0, hand, false);

        // Verifica que nunca foi chamada a cor azul
        verify(graphicsMock, never()).setBackgroundColor(new TextColor.RGB(20, 50, 150));
    }

    @Test
    public void testDrawHand_HideSecondCard() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.COPAS, Rank.TEN));
        hand.addCard(new Card(Naipe.PAUS, Rank.FIVE)); // Esta deve ficar escondida

        // true = esconder a segunda carta (índice 1)
        gameScreen.drawHand(0, 0, hand, true);

        // Verifica se a cor azul foi chamada uma vez (para a carta escondida)
        verify(graphicsMock, atLeastOnce()).setBackgroundColor(new TextColor.RGB(20, 50, 150));
    }

    @Test
    public void testClose() throws IOException {
        gameScreen.getScreen().close();
        verify(screenMock).close();
    }
    @Test
    public void testDrawBaseLayer_PreciseCoordinates_MutantKiller() throws IOException {

        gameScreen.drawBaseLayer();

        verify(graphicsMock).drawLine(eq(0), eq(0), anyInt(), eq(0), eq(' '));

        verify(graphicsMock).drawLine(eq(0), eq(23), anyInt(), eq(23), eq(' '));
    }
}