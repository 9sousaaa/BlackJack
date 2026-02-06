package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    @Test
    public void testBlackjackValue() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.COPAS, Rank.ACE));
        hand.addCard(new Card(Naipe.COPAS, Rank.KING));

        assertEquals(21, hand.calculateValue());
        assertEquals(2, hand.getCards().size());
    }

    @Test
    public void testHandValueCalculation() {
        Hand hand = new Hand();
        assertEquals(0, hand.calculateValue()); // Testa lista vazia (loop start)

        hand.addCard(new Card(Naipe.COPAS, Rank.TWO));
        assertEquals(2, hand.calculateValue()); // Testa 1 elemento

        hand.addCard(new Card(Naipe.OUROS, Rank.KING));
        assertEquals(12, hand.calculateValue()); // Testa soma simples
    }

    @Test
    public void AddCard() {
        Hand hand = new Hand();
        Card card = new Card(Naipe.COPAS, Rank.FIVE); // 5

        hand.addCard(card);

        assertEquals(1, hand.getCards().size());
        assertEquals(5, hand.calculateValue());
    }

    @Test
    public void AceCalculation() {
        Hand hand = new Hand();

        // O Ás deve valer 11 inicialmente
        hand.addCard(new Card(Naipe.COPAS, Rank.ACE));
        assertEquals(11, hand.calculateValue());

        // Ás + Rei = 21 (Blackjack)
        hand.addCard(new Card(Naipe.OUROS, Rank.KING));
        assertEquals(21, hand.calculateValue());

        // Ás + Rei + Rei = 21 (O Ás vale 1 para não rebentar)
        hand.addCard(new Card(Naipe.ESPADAS, Rank.KING));

        // Verifica (1 + 10 + 10 = 21)
        assertEquals(21, hand.calculateValue());

        // Se adicionar mais um 5, rebenta (1 + 10 + 10 + 5 = 26)
        hand.addCard(new Card(Naipe.PAUS, Rank.FIVE));
        assertEquals(26, hand.calculateValue());
    }
    @Test
    public void TwoAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.COPAS, Rank.ACE));
        hand.addCard(new Card(Naipe.ESPADAS, Rank.ACE));

        // Não deve ser 22, deve ser 12
        assertEquals(12, hand.calculateValue());
    }
    @Test
    public void EmptyHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.calculateValue());
        assertTrue(hand.getCards().isEmpty());
    }

    @Test
    public void testCalculateValue_Boundary21_WithAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE)); // 11
        hand.addCard(new Card(Naipe.PAUS, Rank.KING)); // 10
        // Tem de dar exatamente 21. Se der 11, a mutação >= 21 sobreviveu.
        assertEquals(21, hand.calculateValue());
    }

    @Test
    public void testCalculateValue_Bust_NoAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.PAUS, Rank.TEN));
        hand.addCard(new Card(Naipe.PAUS, Rank.TEN));
        hand.addCard(new Card(Naipe.PAUS, Rank.TWO));
        assertEquals(22, hand.calculateValue());
    }

    @Test
    public void testCalculateValue_MultipleAces_Math() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE)); // 11
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE)); // 11+11=22 -> ajusta para 12
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE)); // 12+11=23 -> ajusta para 13
        assertEquals(13, hand.calculateValue());
    }

    @Test
    public void testCalculateValue_Boundary21_DoNotReduceAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE)); // 11
        hand.addCard(new Card(Naipe.PAUS, Rank.KING)); // 10

        // Total é 21.
        // Mutante: se mudar condição para ">= 21", vai reduzir o Ás e dar 11.
        assertEquals(21, hand.calculateValue());
    }

    @Test
    public void testCalculateValue_FourAces_HardMath() {
        Hand hand = new Hand();
        hand.addCard(new Card(Naipe.PAUS, Rank.ACE));
        hand.addCard(new Card(Naipe.COPAS, Rank.ACE));
        hand.addCard(new Card(Naipe.OUROS, Rank.ACE));
        hand.addCard(new Card(Naipe.ESPADAS, Rank.ACE));

        assertEquals(14, hand.calculateValue());
    }
}