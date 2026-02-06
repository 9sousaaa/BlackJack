package Model;

import Controller.SessionManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testCardAndEnums() {
        // Testa a criação e leitura de cartas
        Card card = new Card(Naipe.COPAS, Rank.ACE);

        assertEquals(Naipe.COPAS, card.getNaipe());
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(11, card.getValor());
        assertNotNull(card.toString());

        // Testa os Enums
        assertEquals("♥", Naipe.COPAS.getSymbol());
        assertEquals("A", Rank.ACE.getSymbol());

        // Loop para cobrir todos os valores dos Enums
        for (Naipe n : Naipe.values()) { assertNotNull(n.getSymbol()); }
        for (Rank r : Rank.values()) { assertNotNull(r.getSymbol()); }
    }

    @Test
    public void testDeck() {
        Deck deck = new Deck();
        // Um baralho novo tem 52 cartas
        assertEquals(52, deck.getCards().size());

        Card c = deck.drawCard();
        assertNotNull(c);
        assertEquals(51, deck.getCards().size());
    }

    @Test
    public void testGameStateDataGettersSetters() {
        GameStateData state = new GameStateData();

        // Testa Getters e Setters básicos
        state.setBalance(500);
        assertEquals(500, state.getBalance());

        state.setCurrentBet(50);
        assertEquals(50, state.getCurrentBet());

        state.setMessage("Test");
        assertEquals("Test", state.getMessage());

        state.setGameOver(true);
        assertTrue(state.isGameOver());

        state.setPlayerTurn(false);
        assertFalse(state.isPlayerTurn());

        state.setDealerHandVisible(true);
        assertTrue(state.isDealerHandVisible());

        assertNotNull(state.getPlayerHand());
        assertNotNull(state.getDealerHand());
        assertNotNull(state.getDeck());
    }

    @Test
    public void testSessionManager() {
        SessionManager session = new SessionManager(100);
        assertTrue(session.canPlay());
        assertEquals(100, session.getBalance());

        session.updateBalance(0);
        assertFalse(session.canPlay());
    }
}