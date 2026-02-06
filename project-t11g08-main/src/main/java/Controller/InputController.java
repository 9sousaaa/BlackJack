package Controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;
import java.util.List;

public class InputController {
    private final InputHandler inputHandler;

    public InputController(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // Método para ler input
    public int updateSelection (KeyStroke key, int current, List<String> buttons) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            if (current <= 0) {
                // Se estiver no topo, salta para o último botão (loop)
                return buttons.size() - 1;
            } else {
                // Caso contrário, sobe apenas uma posição
                return current - 1;
            }
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            if (current >= buttons.size() - 1) {
                // Se estiver no último botão, volta para o topo (loop)
                return 0;
            } else {
                // Caso contrário, desce apenas uma posição
                return current + 1;
            }
        }
        // Se for outra tecla qualquer, mantém a seleção atual
        return current;
    }
}