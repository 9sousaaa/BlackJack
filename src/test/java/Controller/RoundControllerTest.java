package Controller;

import Model.*;
import Util.GameRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// Certifica-te de que estes imports estáticos estão presentes
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundControllerTest {
    private GameStateData state;
    private GameRules rules;
    private DealerAIController dealerAI;
    private RoundController controller;

    private Hand playerHand;
    private Hand dealerHand;
    private Deck deck;

    @BeforeEach
    public void setUp() {
        state = mock(GameStateData.class);
        rules = mock(GameRules.class);
        dealerAI = mock(DealerAIController.class);
        deck = mock(Deck.class);

        playerHand = new Hand();
        dealerHand = new Hand();

        when(state.getPlayerHand()).thenReturn(playerHand);
        when(state.getDealerHand()).thenReturn(dealerHand);
        when(state.getDeck()).thenReturn(deck);

        when(deck.drawCard()).thenReturn(new Card(Naipe.COPAS, Rank.TWO));

        when(rules.isBust(any())).thenReturn(false);
        when(rules.determineWinner(any(), any())).thenReturn("DRAW!");

        controller = new RoundController(state, rules, dealerAI);
    }

    @Test
    public void testProcessHit_Normal() {
        int initialSize = state.getPlayerHand().getCards().size();

        when(state.isPlayerTurn()).thenReturn(true);

        controller.processHit();

        assertEquals(initialSize + 1, state.getPlayerHand().getCards().size());

        verify(state, times(0)).setGameOver(true);
    }

    @Test
    public void testProcessInitialStatus_Blackjack() {
        playerHand.addCard(new Card(Naipe.PAUS, Rank.ACE));
        playerHand.addCard(new Card(Naipe.PAUS, Rank.KING));

        when(state.isPlayerTurn()).thenReturn(true);
        when(rules.determineWinner(any(), any())).thenReturn("BLACKJACK!!!");

        controller.processInitialGameStatus();

        // Verifica se passou a vez
        verify(state).setPlayerTurn(false);
        // Verifica se o Dealer jogou
        verify(dealerAI, times(1)).executeTurn(any(), any());
    }

    @Test
    public void testProcessHit_Bust() {
        playerHand.addCard(new Card(Naipe.PAUS, Rank.KING));
        playerHand.addCard(new Card(Naipe.PAUS, Rank.QUEEN));

        when(state.isPlayerTurn()).thenReturn(true);

        when(rules.isBust(any())).thenReturn(true);

        when(rules.determineWinner(any(), any())).thenReturn("YOU BUSTED!");

        controller.processHit();

        verify(state, times(1)).setGameOver(true);
    }

    @Test
    public void testEndRound_BlackjackPayoutIsExactly2Point5() {
        // Setup
        when(state.getCurrentBet()).thenReturn(100);
        when(state.getBalance()).thenReturn(0); // Saldo 0 para facilitar conta
        // Simula mensagem de vitória de Blackjack
        when(rules.determineWinner(any(), any())).thenReturn("BLACKJACK!!!");

        controller.endRound();

        // Se o mutante mudar para * 2, daria 200 e o teste falharia.
        verify(state).setBalance(250);
    }

    @Test
    public void testEndRound_UpdatesMessageAndGameOver() {

        controller.endRound();

        verify(state).setGameOver(true);

        verify(state).setMessage(argThat(msg -> msg.contains("WON") || msg.contains("LOST") || msg.contains("DRAW")));
    }

    @Test
    public void testEndRound_BlackjackPayoutExactMath_MutantKiller() {
        when(state.getCurrentBet()).thenReturn(100);
        when(state.getBalance()).thenReturn(1000);
        when(rules.determineWinner(any(), any())).thenReturn("BLACKJACK!!!");

        controller.endRound();

        //Mata mutantes de matemática no payout
        verify(state).setBalance(1250);

        // Mata mutantes "Void Method Call" que removem estas linhas
        verify(state).setGameOver(true);
        verify(state).setMessage(contains("BLACKJACK"));
    }



}