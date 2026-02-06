package Controller;

import Model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DealerAITest {
    @Test
    public void DealerDrawsUntil17() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            DealerAIController dealer = new DealerAIController();
            Hand dealerHand = new Hand();

            dealerHand.addCard(new Card(Naipe.COPAS, Rank.JACK));

            // Criar um Deck Falso
            Deck deckFalso = Mockito.mock(Deck.class);

            // Dar as cartas para que o valor seja 17
            Mockito.when(deckFalso.drawCard()).thenReturn(new Card(Naipe.COPAS, Rank.TWO)).thenReturn(new Card(Naipe.COPAS, Rank.FIVE));

            // Dealer joga com a mão criada
            dealer.executeTurn(dealerHand, deckFalso);

            // Verifica (10 + 2 + 5 = 17)
            assertEquals(17, dealerHand.calculateValue());

            // Verifica se foram pedidas cartas exatamente 2 vezes
            Mockito.verify(deckFalso, Mockito.times(2)).drawCard();
        });
    }

    @Test
    public void DealerHitsOn16() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            DealerAIController dealer = new DealerAIController();
            Hand dealerHand = new Hand();

            // Mão inicial (10 + 6 = 16)
            dealerHand.addCard(new Card(Naipe.OUROS, Rank.KING));
            dealerHand.addCard(new Card(Naipe.PAUS, Rank.SIX));

            // Criar um Deck Falso
            Deck deckFalso = Mockito.mock(Deck.class);

            // Vai sair um 2 (Total vai para 18)
            Mockito.when(deckFalso.drawCard()).thenReturn(new Card(Naipe.COPAS, Rank.TWO));

            // Dealer joga com a mão criada
            dealer.executeTurn(dealerHand, deckFalso);

            // Verifica que ele pediu carta e ficou com
            assertEquals(18, dealerHand.calculateValue());
            Mockito.verify(deckFalso, Mockito.times(1)).drawCard();
        });
    }

    @Test
    public void DealerStopsWhenBusted() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            DealerAIController dealer = new DealerAIController();
            Hand dealerHand = new Hand();

            // Mão inicial (10 + 5 = 15)
            dealerHand.addCard(new Card(Naipe.PAUS, Rank.TEN));
            dealerHand.addCard(new Card(Naipe.COPAS, Rank.FIVE));

            Deck deckFalso = Mockito.mock(Deck.class);

            // Vai sair um 10 (Total vai para 25)
            Mockito.when(deckFalso.drawCard()).thenReturn(new Card(Naipe.ESPADAS, Rank.QUEEN));

            // Dealer joga porque é obrigado
            dealer.executeTurn(dealerHand, deckFalso);

            // Verifica que o valor é 25
            assertTrue(dealerHand.calculateValue() > 21);
            assertEquals(25, dealerHand.calculateValue());

            // Verifica que pediu apenas 1 carta e parou após rebentar
            Mockito.verify(deckFalso, Mockito.times(1)).drawCard();
        });
    }
    @Test
    public void DealerStaysOn17() {
        assertTimeout(Duration.ofSeconds(10), () -> {
            DealerAIController dealer = new DealerAIController();
            Hand dealerHand = new Hand();
            // 10 + 7 = 17
            dealerHand.addCard(new Card(Naipe.OUROS, Rank.KING));
            dealerHand.addCard(new Card(Naipe.PAUS, Rank.SEVEN));

            Deck deckFalso = Mockito.mock(Deck.class);

            dealer.executeTurn(dealerHand, deckFalso);

            assertEquals(17, dealerHand.calculateValue());
            // Verifica que não pediu carta
            Mockito.verify(deckFalso, Mockito.never()).drawCard();
        });
    }
}
