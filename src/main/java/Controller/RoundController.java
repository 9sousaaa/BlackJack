package Controller;

import Model.GameStateData;
import Util.GameRules;
import Util.MusicPlayer;

import java.util.Locale;

public class RoundController {
    private final GameStateData state;
    private final GameRules rules;
    private final DealerAIController dealerAI;

    public RoundController(GameStateData state, GameRules rules, DealerAIController dealerAI) {
        this.state = state;
        this.rules = rules;
        this.dealerAI = dealerAI;
    }

    public void processInitialGameStatus() {
        // Verifica se o jogador já começou com Blackjack
        if (state.getPlayerHand().calculateValue() == 21) {
            processStand();
        }
    }

    // lógica do hit
    public void processHit() {
        if (state.isPlayerTurn()) {
            state.getPlayerHand().addCard(state.getDeck().drawCard());
            if (rules.isBust(state.getPlayerHand())) {
                endRound();
            }
        }
    }

    // lógica do stand
    public void processStand() {
        if (state.isPlayerTurn()) {
            state.setPlayerTurn(false);
            dealerAI.executeTurn(state.getDealerHand(), state.getDeck());
            endRound();
        }
    }

    // Finaliza a ronda e calcula vencedor
    public void endRound() {
        String result = rules.determineWinner(state.getPlayerHand(), state.getDealerHand());
        state.setMessage(result);
        //toca o som do resultado final e para o background
        playResultSound(result);
        state.setGameOver(true);

        // Devolve a aposta + o lucro (2x)
        if (result.contains("BLACKJACK")) {
            int payout = (int) (state.getCurrentBet() * 2.5);
            state.setBalance(state.getBalance() + payout);
        }
        else if (result.contains("YOU WON")) {
            state.setBalance(state.getBalance() + (state.getCurrentBet() * 2));
        }
        // Devolve só a aposta
        else if (result.contains("DRAW")) {
            state.setBalance(state.getBalance() + state.getCurrentBet());
        }
        // Se perdeu, o dinheiro já foi retirado no início, não fazemos nada.
    }

    // metodo para tocar o efeito sonoro
    private void playResultSound(String resultMessage) {
        MusicPlayer.stopBackground();
        MusicPlayer sfxPlayer = new MusicPlayer();
        String lowerMsg = resultMessage.toLowerCase(Locale.ROOT);

        if (lowerMsg.contains("you won") || lowerMsg.contains("blackjack")) {
            sfxPlayer.loadMusic("win.wav");
            sfxPlayer.playOnce();
        } else if (lowerMsg.contains("dealer won")) {
            sfxPlayer.loadMusic("lose.wav");
            sfxPlayer.playOnce();
        }
        // quando for "draw" nao acontece nada
    }
}