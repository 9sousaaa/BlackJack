package Model;

public enum Naipe {
    PAUS("♣"), OUROS("♦"), COPAS("♥"), ESPADAS("♠");

    private final String symbol;

    Naipe(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}