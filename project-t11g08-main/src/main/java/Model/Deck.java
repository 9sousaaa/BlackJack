package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        // Adiciona todas as cartas possíveis ao baralho
        for (Naipe naipe : Naipe.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(naipe, rank));
            }
        }
        // Baralha as cartas
        Collections.shuffle(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    // Retira a carta do topo do baralho
    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}

