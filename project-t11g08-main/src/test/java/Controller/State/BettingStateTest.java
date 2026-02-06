package Controller.State;

import Controller.GameContext;
import Controller.States.BettingState;
import Controller.States.DealerTurnState;
import Controller.States.GameState;
import Controller.States.PlayerTurnState;
import Model.Card;
import Model.GameStateData;
import Model.Naipe;
import Model.Rank;
import View.GameScreen;
import View.Strategies.ViewStrategy;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BettingStateTest {
    private GameContext context;
    private BettingState bettingState;
    private GameScreen screenMock;
    private GameStateData dataSpy;

    @BeforeEach
    public void setUp() {
        // Mock do Screen para o contexto não falhar ao tentar desenhar
        screenMock = mock(GameScreen.class);
        Screen lanternaScreen = mock(Screen.class);
        when(screenMock.getScreen()).thenReturn(lanternaScreen);

        // Spy permite usar o objeto real mas monitorizar chamadas
        dataSpy = spy(new GameStateData());

        context = mock(GameContext.class);
        when(context.getData()).thenReturn(dataSpy);
        when(context.getScreen()).thenReturn(screenMock);
        bettingState = new BettingState();
    }

    @Test
    public void IncreaseBet_WithinBalance() throws IOException {
        context.getData().setBalance(100);
        context.getData().setCurrentBet(10);

        // Simula Seta Cima
        bettingState.handleInput(new KeyStroke(KeyType.ArrowUp), context);

        assertEquals(20, context.getData().getCurrentBet());
    }

    @Test
    public void IncreaseBet_LimitBalance() throws IOException {
        context.getData().setBalance(20);
        context.getData().setCurrentBet(20);

        // Tenta aumentar além do saldo
        bettingState.handleInput(new KeyStroke(KeyType.ArrowUp), context);

        // Verifica se volta ao mínimo
        assertEquals(10, context.getData().getCurrentBet());
    }

    @Test
    public void DecreaseBet_Normal() throws IOException {
        context.getData().setBalance(100);
        context.getData().setCurrentBet(30);

        // Seta Baixo (30 -> 20)
        bettingState.handleInput(new KeyStroke(KeyType.ArrowDown), context);

        assertEquals(20, context.getData().getCurrentBet());
    }

    @Test
    public void DecreaseBet_GoesToMax() throws IOException {
        context.getData().setBalance(500);
        context.getData().setCurrentBet(10);

        // Tenta baixar do mínimo (Seta Baixo)
        bettingState.handleInput(new KeyStroke(KeyType.ArrowDown), context);

        // Se baixar de 10 vai para maxBet
        assertEquals(500, context.getData().getCurrentBet());
    }

    @Test
    public void PlaceBet_NormalStart() throws IOException {
        context.getData().setBalance(100);
        context.getData().setCurrentBet(10);

        context.getData().getDeck().getCards().clear();

        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.ACE));

        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.TWO));

        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.TWO));

        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.KING));

        bettingState.handleInput(new KeyStroke(KeyType.Enter), context);

        assertEquals(90, context.getData().getBalance());

        ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
        verify(context).setState(captor.capture());

        assertTrue(captor.getValue() instanceof PlayerTurnState);
    }

    @Test
    public void PlaceBet_InstantBlackjack() throws IOException {
        context.getData().setBalance(100);
        context.getData().setCurrentBet(10);

        context.getData().getDeck().getCards().clear();

        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.TWO));   // Dealer 2
        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.KING));  // Player 2 (10)
        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.TWO));   // Dealer 1
        context.getData().getDeck().getCards().add(new Card(Naipe.COPAS, Rank.ACE));   // Player 1 (11)
        // Player Total = 21

        bettingState.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se detetou o Blackjack e mudou para o Dealer
        ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
        verify(context).setState(captor.capture());
        assertTrue(captor.getValue() instanceof DealerTurnState);
    }

    @Test
    public void PlaceBet_InvalidBetAmount() throws IOException {
        context.getData().setBalance(100);
        // Força uma aposta ilegal manualmente
        context.getData().setCurrentBet(200);

        bettingState.handleInput(new KeyStroke(KeyType.Enter), context);

        // Verifica se a aposta foi corrigida para o saldo (100) e não iniciou o jogo
        assertEquals(100, context.getData().getCurrentBet());
        // O estado deve manter-se BettingState
        verify(context, never()).setState(any());
    }

    @Test
    public void IncreaseBet_ExactBalanceBoundary() throws IOException {
        // Testa se consegue apostar TUDO
        dataSpy.setBalance(20);
        dataSpy.setCurrentBet(10);

        bettingState.handleInput(new KeyStroke(KeyType.ArrowUp), context);

        // Verifica se permite ir para 20
        verify(dataSpy).setCurrentBet(20);
    }

    @Test
    public void PlaceBet_CallsStartRound() throws IOException {
        dataSpy.setBalance(100);
        dataSpy.setCurrentBet(10);

        bettingState.handleInput(new KeyStroke(KeyType.Enter), context);

        verify(dataSpy, times(1)).startRound();

        // Verifica a matemática do saldo
        verify(dataSpy).setBalance(90);
    }

    @Test
    public void testDraw() throws IOException {
        bettingState.draw(context);

        // Verifica se o render foi chamado
        verify(screenMock).render(
                any(ViewStrategy.class),
                eq(dataSpy),
                isNull(),
                eq(0)
        );
    }

    @Test
    public void testDecreaseBet_CalculatesMaxBetWithRounding_MutantKiller() throws IOException {

        context.getData().setBalance(505);
        context.getData().setCurrentBet(10);

        bettingState.handleInput(new KeyStroke(KeyType.ArrowDown), context);

        assertEquals(500, context.getData().getCurrentBet());
    }


}
