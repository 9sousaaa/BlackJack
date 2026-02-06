package Model;

public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11);

    private final String symbol; // O que aparece no ecrã
    private final int valor;    // Quanto vale no jogo

    Rank(String symbol, int valor) {
        this.symbol = symbol;
        this.valor = valor;
    }

    // Devolve o valor da carta
    public int getValor() {
        return valor;
    }

    // Devolve o símbolo para desenhar
    public String getSymbol() {
        return symbol;
    }
}