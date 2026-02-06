package Controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InputHandlerTest {

    @Test
    public void testGetNextKey() throws IOException {
        // Mock do ecrã do Lanterna
        Screen screenMock = mock(Screen.class);
        InputHandler handler = new InputHandler(screenMock);

        // Simula que o Lanterna recebeu um "Enter"
        KeyStroke expectedKey = new KeyStroke(KeyType.Enter);
        when(screenMock.pollInput()).thenReturn(expectedKey);

        // Executa
        KeyStroke result = handler.getNextKey();

        // Verifica e garante que chamou pollInput (não bloqueia)
        assertEquals(expectedKey, result);
        verify(screenMock).pollInput();
    }

    @Test
    public void testReadKey() throws IOException {
        Screen screenMock = mock(Screen.class);
        InputHandler handler = new InputHandler(screenMock);

        KeyStroke expectedKey = new KeyStroke(KeyType.Escape);
        when(screenMock.readInput()).thenReturn(expectedKey);

        KeyStroke result = handler.readKey();

        // Verifica e garante que chamou readInput (bloqueia)
        assertEquals(expectedKey, result);
        verify(screenMock).readInput();
    }

    @Test
    public void testGetNextKeyReturnsInput() throws IOException {
        Screen screenMock = mock(Screen.class);
        KeyStroke expectedKey = new KeyStroke(KeyType.Enter);

        when(screenMock.pollInput()).thenReturn(expectedKey);

        InputHandler handler = new InputHandler(screenMock);

        assertSame(expectedKey, handler.getNextKey());
    }

    @Test
    public void testGetNextKeyHandlesNull() throws IOException {
        Screen screenMock = mock(Screen.class);
        when(screenMock.pollInput()).thenReturn(null);

        InputHandler handler = new InputHandler(screenMock);
        assertNull(handler.getNextKey());
    }
}
