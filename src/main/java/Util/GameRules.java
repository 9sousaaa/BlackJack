package Util;

import Model.Hand;

public class GameRules {
    // Verifica se a mão rebentou
    public boolean isBust(Hand hand) {return hand.calculateValue() > 21;}

    public String determineWinner(Hand jogador, Hand dealer) {
        int JogadorValor = jogador.calculateValue();
        int DealerValor = dealer.calculateValue();

        // Verificar todos os casos
        if (JogadorValor == 21 && jogador.getCards().size() == 2) {return "BLACKJACK!!!";}
        if (JogadorValor > 21) {return "YOU BUSTED! DEALER WON!";}
        if (DealerValor > 21) {return "DEALER BUSTED! YOU WON!";}
        if (JogadorValor > DealerValor) {return "YOU WON! (" + JogadorValor + " vs " + DealerValor + ")";}
        if (JogadorValor == DealerValor) {return "DRAW!";}
        return "DEALER WON. (" + DealerValor + " vs " + JogadorValor + ")";
    }
}