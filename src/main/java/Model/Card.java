package Model;

public class Card {
    private Naipe naipe;
    private Rank rank;

    public Card(Naipe naipe, Rank rank) {
        this.naipe = naipe;
        this.rank = rank;
    }
    public Naipe getNaipe() {
        return naipe;
    }
    // Devolve o valor da carta
    public int getValor() {
        return rank.getValor();
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " de " + naipe;
    }
}