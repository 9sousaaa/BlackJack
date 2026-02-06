package Controller.State;

import Controller.GameContext;
import Controller.States.DealerTurnState;
import Controller.States.PlayerTurnState;
import Model.Card;
import Model.GameStateData;
import Model.Naipe;
import Model.Rank;
import View.GameScreen;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTurnStateTest {

    private GameContext context;
    private GameStateData data;
    private PlayerTurnState state;
    private GameScreen screenMock;

    @BeforeEach
    public void setUp() {
        // Mock do contexto e do ecrã
        screenMock = mock(GameScreen.class);
        Screen lanternaScreenMock = mock(Screen.class);
        when(screenMock.getScreen()).thenReturn(lanternaScreenMock);

        context = mock(GameContext.class);
        data = new GameStateData();

        when(context.getData()).thenReturn(data);
        when(context.getScreen()).thenReturn(screenMock);

        state = new PlayerTurnState();
        state = new PlayerTurnState((time) -> {});
    }

    @Test
    public void testStand() throws IOException {
        // Simula Seta Baixo e Enter (Seleciona STAND)
        state.handleInput(new KeyStroke(KeyType.ArrowDown), context);
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se muda para o estado do Dealer
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());

        assertTrue(captor.getValue() instanceof DealerTurnState);
    }

    @Test
    public void testHit() throws IOException {
        // Configura baralho e mão inicial
        int initialSize = data.getPlayerHand().getCards().size();

        // Simula Enter (Seleciona HIT)
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se recebeu carta
        assertEquals(initialSize + 1, data.getPlayerHand().getCards().size());
    }

    @Test
    public void testInstant21() throws IOException {
        // Dá 21 ao jogador
        data.getPlayerHand().addCard(new Card(Naipe.COPAS, Rank.ACE));
        data.getPlayerHand().addCard(new Card(Naipe.COPAS, Rank.KING));

        // Qualquer input deve acionar a verificação de 21
        state.handleInput(new KeyStroke(KeyType.ArrowUp), context);

        // Verifica se passa logo para o Dealer
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());
        assertTrue(captor.getValue() instanceof DealerTurnState);
    }

    @Test
    public void testHitAndBust() throws IOException {
        data.getPlayerHand().getCards().clear();
        data.getPlayerHand().addCard(new Card(Naipe.COPAS, Rank.KING));
        data.getPlayerHand().addCard(new Card(Naipe.ESPADAS, Rank.QUEEN));

        // Manipula o baralho para a próxima carta ser um 5 (Total 25 -> Bust)
        data.getDeck().getCards().add(new Card(Naipe.OUROS, Rank.FIVE));

        // Simula Enter (Seleciona HIT)
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se mudou para GameOverState
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());

        // Verifica se é GameOverState
        assertTrue(captor.getValue() instanceof Controller.States.GameOverState);
    }

    @Test
    public void testHitAndGetExactly21() throws IOException {
        // Configura a mão com 11
        data.getPlayerHand().getCards().clear();
        data.getPlayerHand().addCard(new Card(Naipe.COPAS, Rank.FIVE));
        data.getPlayerHand().addCard(new Card(Naipe.ESPADAS, Rank.SIX));

        // Manipula o baralho para sair um REI (10) -> Total 21
        data.getDeck().getCards().add(new Card(Naipe.OUROS, Rank.KING));

        // Simula Enter (Seleciona HIT)
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Se tiver 21, deve passar a vez ao Dealer automaticamente
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());

        assertTrue(captor.getValue() instanceof DealerTurnState);
    }

    @Test
    public void testNavigationArrowUp() throws IOException {
        // Pressiona Seta Baixo (Seleciona Stand)
        state.handleInput(new KeyStroke(KeyType.ArrowDown), context);

        // Pressiona Seta Cima (Volta a selecionar Hit)
        state.handleInput(new KeyStroke(KeyType.ArrowUp), context);

        // Pressiona Enter
        int initialSize = data.getPlayerHand().getCards().size();
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se deu HIT
        assertEquals(initialSize + 1, data.getPlayerHand().getCards().size());
    }
    @Test
    public void testDraw() throws IOException {
        state.draw(context);

        // Verifica se o render do ecrã foi chamado com os botões "HIT" e "STAND"
        verify(context.getScreen(), times(1)).render(
                any(View.Strategies.ViewStrategy.class),
                eq(data),
                anyList(),
                anyInt()
        );
    }
}
