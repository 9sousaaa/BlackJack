package Model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    //vai ser a lista de cartas que fazem parte da mao
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }
    // Adiciona uma carta à mão
    public void addCard(Card card) {
        cards.add(card);
    }
    //calcula o valor da mao
    public int calculateValue() {
        int value = 0;
        int asCount = 0;

        for (Card card : cards) {
            value += card.getValor();
            if (card.getRank() == Rank.ACE) {
                asCount++;
            }
        }

        // Ajusta Áses se o valor total for maior que 21
        while (value > 21 && asCount > 0) {
            value -= 10;
            asCount--;
        }

        return value;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.toString() + " (Total: " + calculateValue() + ")";
    }
}