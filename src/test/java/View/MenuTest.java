package View;

import Controller.InputHandler;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MenuTest {

    private GameScreen gameScreenMock;
    private Screen screenMock;
    private InputHandler inputHandlerMock;
    private Menu menu;

    // Verifica se os métodos protegidos foram chamados
    private boolean startGameCalled;
    private boolean exitGameCalled;

    @BeforeEach
    public void setUp() {
        gameScreenMock = mock(GameScreen.class);
        screenMock = mock(Screen.class);
        inputHandlerMock = mock(InputHandler.class);

        when(gameScreenMock.getScreen()).thenReturn(screenMock);

        // Cria uma subclasse anónima do Menu para impedir que o jogo real comece ou que o System.exit mate o teste
        menu = new Menu(gameScreenMock) {
            @Override
            protected void startGame() {
                startGameCalled = true;
            }

            @Override
            protected void exitGame() {
                exitGameCalled = true;
            }
        };

        // Mock do input
        menu.setInputHandler(inputHandlerMock);

        startGameCalled = false;
        exitGameCalled = false;
    }

    @Test
    public void testMenuNavigationDown() throws IOException {
        // Simula Seta Baixo, Seta Baixo e depois EOF (para sair do loop)
        when(inputHandlerMock.getNextKey()).thenReturn(
                new KeyStroke(KeyType.ArrowDown),
                new KeyStroke(KeyType.ArrowDown), // Volta ao topo
                new KeyStroke(KeyType.EOF)
        );

        menu.run();

        // Verifica se renderizou com a opção 0, depois 1, depois 0
        verify(gameScreenMock, times(1)).render(any(), any(), anyList(), eq(1));
        verify(gameScreenMock, atLeastOnce()).render(any(), any(), anyList(), eq(0));
    }

    @Test
    public void testMenuNavigationUp() throws IOException {
        // Simula Seta Cima (vai para o último) e depois EOF
        when(inputHandlerMock.getNextKey()).thenReturn(
                new KeyStroke(KeyType.ArrowUp),
                new KeyStroke(KeyType.EOF)
        );

        menu.run();

        // Como começou no 0 e clicou Cima, deve ir para o 1 (EXIT)
        verify(gameScreenMock, atLeastOnce()).render(any(), any(), anyList(), eq(1));
    }

    @Test
    public void testSelectStartGame() throws IOException {
        // Simula: Enter (na opção 0) -> EOF
        when(inputHandlerMock.getNextKey()).thenReturn(
                new KeyStroke(KeyType.Enter),
                new KeyStroke(KeyType.EOF)
        );

        menu.run();

        // Verifica se tentou iniciar o jogo
        assert(startGameCalled);
        assert(!exitGameCalled);
    }

    @Test
    public void testSelectExit() throws IOException {
        // Simula Baixo, Enter e depois EOF
        when(inputHandlerMock.getNextKey()).thenReturn(
                new KeyStroke(KeyType.ArrowDown), // Seleciona EXIT
                new KeyStroke(KeyType.Enter),     // Confirma
                new KeyStroke(KeyType.EOF)
        );

        menu.run();

        // Verifica se tentou sair
        assert(!startGameCalled);
        assert(exitGameCalled);
    }

    @Test
    public void testNullKey() throws IOException {
        // Simula null e depois EOF
        when(inputHandlerMock.getNextKey()).thenReturn(
                null,
                new KeyStroke(KeyType.EOF)
        );

        menu.run();

        // Verifica que não rebentou e correu o loop
        verify(gameScreenMock, atLeastOnce()).render(any(), any(), anyList(), anyInt());
    }

    @Test
    public void testResize() throws IOException {
        when(inputHandlerMock.getNextKey()).thenReturn(new KeyStroke(KeyType.EOF));
        menu.run();

        // Verifica se o código tentou redimensionar a janela
        verify(screenMock, atLeastOnce()).doResizeIfNecessary();
    }
}
