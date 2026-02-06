package Controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputControllerTest {

    @Test
    public void testMenuNavigation() {
        InputHandler handlerMock = Mockito.mock(InputHandler.class);
        InputController controller = new InputController(handlerMock);
        List<String> buttons = Arrays.asList("0", "1", "2");

        // Teste Normal: Descer
        assertEquals(1, controller.updateSelection(new KeyStroke(KeyType.ArrowDown), 0, buttons));

        // Teste Normal: Subir
        assertEquals(1, controller.updateSelection(new KeyStroke(KeyType.ArrowUp), 2, buttons));

        // Teste Limite (Math Mutation): Wrap Around Baixo (Último -> Primeiro)
        assertEquals(0, controller.updateSelection(new KeyStroke(KeyType.ArrowDown), 2, buttons));

        // Teste Limite (Math Mutation): Wrap Around Cima (Primeiro -> Último)
        assertEquals(2, controller.updateSelection(new KeyStroke(KeyType.ArrowUp), 0, buttons));

        // Teste Input Ignorado
        assertEquals(1, controller.updateSelection(new KeyStroke(KeyType.Escape), 1, buttons));
    }

    // Teste para garantir que não há erros de índice com listas vazias ou unitárias
    @Test
    public void testSingleButtonList() {
        InputHandler handlerMock = Mockito.mock(InputHandler.class);
        InputController controller = new InputController(handlerMock);
        List<String> buttons = Arrays.asList("Unico");

        // Baixo deve manter no 0
        assertEquals(0, controller.updateSelection(new KeyStroke(KeyType.ArrowDown), 0, buttons));
        // Cima deve manter no 0
        assertEquals(0, controller.updateSelection(new KeyStroke(KeyType.ArrowUp), 0, buttons));
    }
}
