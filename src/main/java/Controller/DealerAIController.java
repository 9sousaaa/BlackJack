package Controller;

import Model.Hand;
import Model.Deck;

public class DealerAIController {
    public void executeTurn(Hand dealerHand, Deck deck) {
        // O Dealer é obrigado a pedir cartas até ter 17 ou mais
        while (dealerHand.calculateValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }
    }
}