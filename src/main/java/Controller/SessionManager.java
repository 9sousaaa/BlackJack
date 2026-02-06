package Controller;

import Model.GameStateData;

public class SessionManager {
    private int currentBalance;

    public SessionManager(int initialBalance) {
        this.currentBalance = initialBalance;
    }

    public boolean canPlay() {
        return currentBalance > 0;
    }

    public int getBalance() {
        return currentBalance;
    }

    public void updateBalance(int newBalance) {
        this.currentBalance = newBalance;
    }
}