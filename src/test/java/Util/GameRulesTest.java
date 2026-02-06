package Util;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameRulesTest {
    private Hand player;
    private Hand dealer;
    private GameRules rules = new GameRules();

    @BeforeEach
    public void setUp() {
        rules = new GameRules();
        player = new Hand();
        dealer = new Hand();
    }

    @Test
    public void testBustBoundary() {
        // 21 não é Bust
        player.addCard(new Card(Naipe.COPAS, Rank.KING)); // 10
        player.addCard(new Card(Naipe.COPAS, Rank.ACE)); // 11
        assertFalse(rules.isBust(player));

        // 22 é Bust
        player.addCard(new Card(Naipe.COPAS, Rank.ACE)); // 10+1+1 = 12 (Hard)

        Hand bustHand = new Hand();
        bustHand.addCard(new Card(Naipe.COPAS, Rank.TEN));
        bustHand.addCard(new Card(Naipe.COPAS, Rank.TEN));
        bustHand.addCard(new Card(Naipe.COPAS, Rank.TWO)); // 22
        assertTrue(rules.isBust(bustHand));
    }
    @Test
    public void testBlackjack() {
        Hand p = new Hand();
        // Player fica com Blackjack
        p.addCard(new Card(Naipe.PAUS, Rank.ACE));
        p.addCard(new Card(Naipe.PAUS, Rank.KING));

        // Dealer fica com mão aleatória
        Hand d = new Hand();

        assertEquals("BLACKJACK!!!", rules.determineWinner(p, d));
    }

    @Test
    public void testPlayerBust() {
        Hand p = new Hand();
        // Player fica com 25
        p.addCard(new Card(Naipe.PAUS, Rank.TEN));
        p.addCard(new Card(Naipe.PAUS, Rank.TEN));
        p.addCard(new Card(Naipe.PAUS, Rank.FIVE));

        assertTrue(rules.isBust(p));
        assertTrue(rules.determineWinner(p, new Hand()).contains("YOU BUSTED"));
    }

    @Test
    public void testDealerBust() {
        Hand p = new Hand();
        // Player fica com 10
        p.addCard(new Card(Naipe.PAUS, Rank.TEN));

        // Dealer fica com 25
        Hand d = new Hand();
        d.addCard(new Card(Naipe.COPAS, Rank.TEN));
        d.addCard(new Card(Naipe.COPAS, Rank.TEN));
        d.addCard(new Card(Naipe.COPAS, Rank.FIVE));

        assertTrue(rules.determineWinner(p, d).contains("DEALER BUSTED"));
    }

    @Test
    public void testDraw() {
        Hand p = new Hand(); p.addCard(new Card(Naipe.PAUS, Rank.TEN));
        Hand d = new Hand(); d.addCard(new Card(Naipe.PAUS, Rank.TEN));

        assertEquals("DRAW!", rules.determineWinner(p, d));
    }


    @Test
    public void testPlayerWinsScore() {
        Hand p = new Hand(); p.addCard(new Card(Naipe.PAUS, Rank.KING));
        Hand d = new Hand(); d.addCard(new Card(Naipe.PAUS, Rank.NINE));

        assertTrue(rules.determineWinner(p, d).contains("YOU WON"));
    }

    @Test
    public void testDealerWinsScore() {
        Hand p = new Hand(); p.addCard(new Card(Naipe.PAUS, Rank.NINE)); // 9
        Hand d = new Hand(); d.addCard(new Card(Naipe.PAUS, Rank.KING)); // 10

        assertTrue(rules.determineWinner(p, d).contains("DEALER WON"));
    }
}