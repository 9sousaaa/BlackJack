package Controller.State;

import Controller.GameContext;
import Controller.States.BettingState;
import Controller.States.GameOverState;
import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameOverStateTest {
    private GameContext context;
    private GameStateData data;
    private GameOverState state;
    private GameScreen screenMock;

    @BeforeEach
    public void setUp() {
        screenMock = mock(GameScreen.class);
        context = mock(GameContext.class);
        data = new GameStateData();

        when(context.getData()).thenReturn(data);
        when(context.getScreen()).thenReturn(screenMock);

        state = new GameOverState();
    }

    @Test
    public void testPlayAgainResetsGame() throws IOException {
        data.setBalance(500);
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica reset obrigatório
        verify(context).resetForNewRound();

        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());
        assertTrue(captor.getValue() instanceof BettingState);
    }

    @Test
    public void testPlayAgainWithMoney() {
        data.setBalance(500);

        // Seleciona PLAY AGAIN
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        verify(context).resetForNewRound();

        // Verifica transição para BettingState
        ArgumentCaptor<Controller.States.GameState> captor = ArgumentCaptor.forClass(Controller.States.GameState.class);
        verify(context).setState(captor.capture());
        assertTrue(captor.getValue() instanceof BettingState);

        // O saldo mantém-se
        assertEquals(500, data.getBalance());
    }

    @Test
    public void testPlayAgainNoMoneyResetsBalance() {
        // Saldo = 0
        data.setBalance(0);

        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se o saldo inicial foi reposto (1000)
        assertEquals(1000, data.getBalance());
        verify(context).resetForNewRound();
    }

    @Test
    public void testExitGame() {
        // Seta Baixo (Seleciona EXIT)
        state.handleInput(new KeyStroke(KeyType.ArrowDown), context);
        state.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica sinal de saída (balance = -1)
        assertEquals(-1, data.getBalance());
    }
    @Test
    public void testDraw() throws IOException {
        // Configuração prévia: finge que havia uma mensagem e cartas escondidas
        data.setMessage("WINNER");
        data.setDealerHandVisible(false);

        state.draw(context);

        // Verifica se o Dealer revela as cartas
        assertTrue(data.isDealerHandVisible(), "O Dealer deve mostrar as cartas no Game Over");

        // Verifica se a mensagem foi limpa
        assertEquals("", data.getMessage(), "A mensagem deve ser limpa antes de renderizar o menu");

        // Verifica se o render foi chamado com os botões "PLAY AGAIN" e "EXIT"
        verify(screenMock).render(
                any(View.Strategies.ViewStrategy.class),
                eq(data),
                argThat((list) ->
                        list != null &&
                                list.size() == 2 &&
                                list.contains("PLAY AGAIN") &&
                                list.contains("EXIT")
                ),
                eq(0)
        );
    }
}