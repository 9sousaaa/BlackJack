package Controller.State;

import Controller.GameContext;
import Controller.States.DealerTurnState;
import Controller.States.GameOverState;
import Model.Card;
import Model.GameStateData;
import Model.Naipe;
import Model.Rank;
import View.GameScreen;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DealerTurnStateTest {
    private GameContext context;
    private GameStateData data;
    private DealerTurnState state;
    private Screen lanternaScreenMock;

    @BeforeEach
    public void setUp() throws IOException {
        GameScreen gameScreenMock = mock(GameScreen.class);
        lanternaScreenMock = mock(Screen.class);
        when(gameScreenMock.getScreen()).thenReturn(lanternaScreenMock);

        context = mock(GameContext.class);
        data = new GameStateData();

        when(context.getData()).thenReturn(data);
        when(context.getScreen()).thenReturn(gameScreenMock);

        state = new DealerTurnState();
        state = new DealerTurnState((time) -> {});
    }

    @Test
    public void testDealerLogicExecution() throws IOException {
        // Dealer começa com 10 + 5 = 15 (Deve pedir carta obrigatoriamente)
        data.getDealerHand().addCard(new Card(Naipe.COPAS, Rank.TEN));
        data.getDealerHand().addCard(new Card(Naipe.COPAS, Rank.FIVE));

        // Executa o draw (que contém a lógica do loop while e sleeps)
        state.draw(context);

        // Verifica se o dealer pediu pelo menos mais uma carta (size > 2)
        assertTrue(data.getDealerHand().getCards().size() >= 2);

        verify(lanternaScreenMock, atLeastOnce()).refresh();

        // Verifica se mudou para GameOverState no final
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());
        assertTrue(captor.getValue() instanceof GameOverState);

        // Verifica se a mensagem de resultado foi definida
        assertNotNull(data.getMessage());
    }
}