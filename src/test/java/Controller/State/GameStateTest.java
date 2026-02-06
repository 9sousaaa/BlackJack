package Controller.State;

import Model.GameStateData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void testGettersAndSetters() {
        GameStateData data = new GameStateData();

        // Teste de saldo e aposta
        data.setBalance(500);
        assertEquals(500, data.getBalance());

        data.setCurrentBet(100);
        assertEquals(100, data.getCurrentBet());

        // Teste de flags booleanas
        data.setPlayerTurn(false);
        assertFalse(data.isPlayerTurn());

        data.setGameOver(true);
        assertTrue(data.isGameOver());

        data.setDealerHandVisible(true);
        assertTrue(data.isDealerHandVisible());

        // Teste de mensagem
        data.setMessage("Teste");
        assertEquals("Teste", data.getMessage());

        // Verifica se os objetos internos existem
        assertNotNull(data.getPlayerHand());
        assertNotNull(data.getDealerHand());
        assertNotNull(data.getDeck());
    }

    @Test
    public void testStartRound() {
        GameStateData data = new GameStateData();
        data.startRound();

        // Método startRound deve dar 2 cartas ao player e 2 ao dealer
        assertEquals(2, data.getPlayerHand().getCards().size());
        assertEquals(2, data.getDealerHand().getCards().size());
    }

    @Test
    public void testStartRoundDistributesCards() {
        GameStateData data = new GameStateData();
        int initialDeckSize = data.getDeck().getCards().size();
        data.startRound();

        // Verifica número exato de cartas
        assertEquals(2, data.getPlayerHand().getCards().size());
        assertEquals(2, data.getDealerHand().getCards().size());

        // Verifica que o baralho perdeu 4 cartas (52 - 4 = 48)
        assertEquals(initialDeckSize - 4, data.getDeck().getCards().size());
    }
}
