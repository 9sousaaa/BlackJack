package Controller;

import Model.GameStateData;
import View.GameScreen;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class GameControllerTest {

    private GameScreen screenMock;
    private InputHandler inputHandlerMock;
    private GameContext contextMock;
    private GameStateData dataMock;
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        screenMock = mock(GameScreen.class);
        Screen lanternaScreenMock = mock(Screen.class);
        when(screenMock.getScreen()).thenReturn(lanternaScreenMock);

        inputHandlerMock = mock(InputHandler.class);
        contextMock = mock(GameContext.class);
        dataMock = mock(GameStateData.class);

        // Liga o mock de dados ao contexto
        when(contextMock.getData()).thenReturn(dataMock);

        // Usa o construtor novo que cria para injetar os mocks
        gameController = new GameController(screenMock, inputHandlerMock, contextMock);
    }

    @Test
    public void testRunLoopStopsOnExitSignal() throws IOException {
        // Na primeira verificação o saldo é 100 (continua). Na segunda é -1 (para).
        when(dataMock.getBalance()).thenReturn(100).thenReturn(-1);

        // Simula Seta Cima (podia ser qualquer tecla)
        KeyStroke key = new KeyStroke(KeyType.ArrowUp);
        when(inputHandlerMock.getNextKey()).thenReturn(key);


        // Executa o loop
        gameController.run();

        // Verifica se o update foi chamado com a tecla simulada
        verify(contextMock, atLeastOnce()).update(key);
        // Verifica se o render foi chamado
        verify(contextMock, atLeastOnce()).render();
        // Verifica se o ecrã fechou no fim
        verify(screenMock.getScreen(), times(1)).close();
    }

    @Test
    public void testInterruptedException() throws IOException {
        // Força o handler a lançar uma exceção ou o loop a correr apenas uma vez
        when(dataMock.getBalance()).thenReturn(-1);

        gameController.run();

        // Verifica se o jogo lida bem com a interrupção da thread (sleep)
        verify(screenMock.getScreen()).close();
    }
}
