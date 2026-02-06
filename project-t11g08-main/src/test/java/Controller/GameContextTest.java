package Controller;

import Controller.States.GameState;
import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameContextTest {

    private GameContext context;
    private GameScreen screenMock;
    private GameState stateMock;

    @BeforeEach
    public void setUp() {
        // Mock do ecrã
        screenMock = mock(GameScreen.class);

        // Inicializa o contexto
        context = new GameContext(screenMock);

        // Mock do estado
        stateMock = mock(GameState.class);

        // Força o contexto a usar o estado falso
        context.setState(stateMock);
    }

    @Test
    public void testUpdateDelegatesToState() throws IOException {
        KeyStroke key = new KeyStroke(KeyType.Enter);

        // Executa o método update
        context.update(key);

        // Verifica se o contexto chamou "handleInput" no estado atual
        verify(stateMock, times(1)).handleInput(key, context);
    }

    @Test
    public void testUpdateIgnoresNullKey() throws IOException {
        // Executa com null
        context.update(null);

        // Garante que não chamou handleInput para evitar NullPointerException
        verify(stateMock, never()).handleInput(any(), any());
    }

    @Test
    public void testRenderDelegatesToState() throws IOException {
        // Executa o render
        context.render();

        // Verifica se o contexto chamou "draw" no estado atual
        verify(stateMock, times(1)).draw(context);
    }

    @Test
    public void testResetForNewRound() {
        // Configura o cenário inicial
        context.getData().setBalance(500);
        context.getData().setMessage("Jogo Antigo");

        // Guarda a referência do objeto de dados antigo
        GameStateData oldData = context.getData();

        // Executa o reset
        context.resetForNewRound();

        assertNotSame(oldData, context.getData());
        assertEquals(500, context.getData().getBalance());

        // Verifica se o objeto de dados é novo
        assertNotNull(context.getData().getDeck());


        // Verifica se o dinheiro foi preservado
        assertEquals(52, context.getData().getDeck().getCards().size());

        // Verifica se os outros campos foram limpos
        assertNull(context.getData().getMessage());
        assertEquals(0, context.getData().getPlayerHand().getCards().size());
    }
}