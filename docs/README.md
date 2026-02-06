## LDTS_<11><08> - <Blackjack>

Neste projeto, iremos desenvolver uma versao digital do clássico jogo de cartas "Blackjack". Este vai consistir, principalmente, na mecânica de distribuição de cartas ao jogador e ao dealer, seguindo as regras tradicionais.
Este projeto foi desenvolvido por Nuno Sousa (up202404498@up.pt) e Guilherme Pinho (up202407213@up.pt) para LDTS 2025/2026.

## Table of Contents

1. [Introduction](#ldts_1108---blackjack)
2. [Controls](#controls)
3. [Implemented Features](#implemented-features)
4. [Planned Features](#planned-features)
5. [General Structure](#general-structure)
6. [Design](#design)
    - [Code Structure (MVC)](#code-structure)
    - [Game Loop](#game-loop)
    - [States](#states)
    - [Factory](#factory)
    - [Strategy](#strategy)
7. [Known Code Smells](#known-code-smells)
8. [Testing](#testing)
9. [Self-Evaluation](#self-evaluation)

## CONTROLS

`⏷`: Move a opção para baixo ou se estiver na última opção move para a primeira.

`⏶`: Move a opção para cima ou se estiver na primeira opção move para a última.

`ENTER`: Pressiona ENTER as opções desejadas.

## IMPLEMENTED FEATURES

- Menu Screen - página inicial.
- Betting Screen - tela onde o jogador vai decidir a aposta
- Inicialização do jogo - o jogo começa com um baralho de cartas baralhado, e com duas cartas, tanto para o dealer como para o jogador.
- Ações Hit/Stand - O jogador pode pedir mais cartas (Hit) ou pode optar por terminar a sua jogada (Stand).
- Dealer Auto-Play - O dealer vai sempre dar Hit até atingir o valor 17 ou superior.
- Lógica Bust / Win / Draw - Implementado no final de cada ronda.
- Efeitos sonoros - Implementação de uma música de fundo e efeitos sonoros para o resultado vitória/derrota.

## PLANNED FEATURES

Todas as funcionalidades planeadas foram implementados com sucesso. As animações das cartas foram simplificadas para um sistema baseado em atraso, para que fiquem mais adequadas às limitações da interface do terminal, sem prejudicar a continuidade do jogo.

## GENERAL STRUCTURE

<p align="center">
  <img src="https://github.com/user-attachments/assets/1473b52f-781a-4e25-a4ec-d2b29b3166f2" />
</p>

## DESIGN

### Code Structure

#### Context Problems

Nas fases iniciais do nosso projeto, a nossa principal preocupação era como estruturar o projeto. Tivemos dificuldades em separar a camada de apresentação (renderização no terminal, cores) da lógica do jogo (regras, gestão do baralho, cálculo da pontuação). Sem uma estrutura clara, o código estava a tornar-se difícil de ler, o que complicava a implementação de novas funcionalidades.

#### The Pattern

Nós aplicamos o padrão Model-View-Controller (MVC). Este padrão arquitetônico divide a lógica do jogo em três componentes:
- Model - gere os dados e a lógica pura e as regras do jogo.
- View - gere a visualização dos dados (output).
- Controller - liga tudo, ele recebe os inputs, atualiza o model e mando o view desenhar.

Ao aplicar este padrão, garantimos que os componentes se tornaram muito mais independentes. Por exemplo, podemos alterar a forma como as cartas são desenhadas no terminal (View) sem ter de mexer na lógica de como os pontos são calculados (Model). Isto foi essencial para manter o código organizado à medida que o projeto avançava.

#### Implementation

A implementação deste modelo pode ser observada ao consultar os pacotes dentro do código-fonte do nosso jogo (as ligações referem-se aos pacotes principais):

- [Model](/src/main/java/Model)
- [View](/src/main/java/View)
- [Controller](/src/main/java/Controller)

Uma explicação é também fornecida no diagrama seguinte:

<p align="center">
  <img src="https://github.com/user-attachments/assets/f5bcc75e-e973-4106-8eb6-870d036b4eae" width="600" />
</p>

#### Consequences

Tal como mencionámos anteriormente, este padrão de arquitetura permite uma melhor organização e segregação do código. Os principais benefícios que observámos foram:
- Princípio da Responsabilidade Única: Cada classe tem uma função distinta, tornando mais fácil identificar e corrigir problemas específicos.

### Game Loop

#### Context Problems

No nosso jogo "Blackjack", a aplicação tem de tratar os inputs do utilizador (hit/stand) enquanto, em simultâneo, gere os eventos do jogo, como as animações das cartas do dealer ou as transições de estado. Por vezes, o processador do utilizador não é suficientemente rápido para renderizar tantas frames quanto as desejadas. A componente de lógica e física do jogo é a que mais sofre com estas diferenças de desempenho entre máquinas. Ao separar a renderização do ciclo de atualização (update loop), conseguimos simular o jogo com a frequência necessária e renderizar apenas quando for possível, garantindo assim um comportamento mais consistente e estável em diferentes sistemas.

#### The Pattern

Nós implementamos o padrão Game Loop. Idealmente, um Game Loop é um ciclo while que corre continuamente dependendo do estado do jogo (no nosso caso, corre enquanto a flag isRunning permanecer verdadeira).Em conjunto com este loop, implementámos um mecanismo de controlo da taxa de frames utilizando thread sleeping.
Esta abordagem permite-nos dissociar a progressão do tempo do jogo da velocidade do processador, isto garante que o jogo corre de forma contínua e fluida.

#### Implementation

A implementação do padrão Game Loop pode ser encontrada no método run() da classe [GameController](/src/main/java/Controller/GameController.java). Este método garante que o ciclo continua enquanto o jogo estiver em execução, uma explicação do ciclo é fornecida no diagrama seguinte:

<p align="center">
  <img src="https://github.com/user-attachments/assets/0ac33714-e138-4c1f-823e-0c7b3f5236cf" width="600" />
</p>

#### Consequences

Ao usar o padrão Game Loop garantimos uma experiência de jogo suave e uma experiência de jogo semelhante em diversos sistemas de utilizador, assim como a capacidade de controlar facilmente a velocidade de execução do nosso código. Por outro lado, usar Thread.sleep num ciclo é por vezes considerado um "code smell" por bloquear a thread principal. Porém no nosso caso, isto é intencional, sem esta pausa o loop correria demasiado rápido, o que não é necessário num jogo de cartas como o "Blackjack". Portanto, usamos este padrão para manter o jogo a uma velocidade constante e jogável.

### States

#### Context Problems

O nosso jogo "Blackjack" pode ser dividido em quatro partes: colocar a aposta, a vez do jogador (hit/stand), a vez do dealer e, por fim, o ecrã de fim de jogo. Gerir estas transições usando cadeias complexas de if-else resultaria num código confuso e tornaria o fluxo de controlo difícil de acompanhar.

#### The Pattern

Nós aplicamos o padrão State. Este padrão de design comportamental permite que um objeto (no nosso caso, o GameContext) altere o seu comportamento quando o seu estado interno muda. Definimos uma interface comum GameState e criámos classes separadas para cada fase do jogo (BettingState, PlayerTurnState, DealerTurnState, GameOverState).

#### Implementation

A implementação do padrão State pode ser encontrada no pacote [States](/src/main/java/Controller/States). O GameContext mantém uma referência para o estado atual e delega nele o processamento dos inputs e a renderização, uma explicação da transição de estados é fornecida no diagrama seguinte:

<p align="center">
  <img src="https://github.com/user-attachments/assets/1aec17a5-cd2c-4d5d-99bb-ba10141b2380" width="600" />
</p>

#### Consequences

- Localiza e divide o compotamento: Cada estado corresponde a uma classe específica (por exemplo, BettingState, PlayerTurnState), que encapsula todo o comportamento dessa fase do jogo. Isto respeita o Princípio da Responsabilidade Única e torna o código mais limpo e fácil de manter.
- Transições de Estado Explícitas: As transições entre as fases do jogo são definidas de forma explícita no código (por exemplo, mudar de PlayerTurnState para DealerTurnState). Isto elimina a necessidade de cadeias complexas de if-else para controlar o fluxo do jogo.

### Factory

#### Context Problems

Para o nosso jogo, nós queriamos que a aplicação corra em modo fullscreen, mas a configuração padrão da biblioteca Lanterna não suporta isto de forma nativa. Criar um terminal que suporte Swing e fullscreen exige passos de configuração complexos que não deveriam sobrecarregar a lógica principal do jogo.

#### The Pattern

Nós aplicamos o padrão Fcatory (especificamente ao usar o DefaultTerminalFactory). Este padrão de design criacional define uma interface para criar objetos, mas permite sub-classes alterar o tipo de objetos que vao ser criados. No nosso caso, abstrai a lógica complexa de instância necessária para configurar uma janela de terminal Swing com capacidades de fullscreen.

#### Implementation

A implementação deste padrão pode ser encontrada no construtor da classe [GameScreen](/src/main/java/View/GameScreen.java). Em vez de instanciarmos manualmente um frame Swing, confiamos na factory para gerar o terminal correto com base na nossa configuração.

  ```java
DefaultTerminalFactory factory = new DefaultTerminalFactory();

// Cria o terminal
Terminal terminal = factory.createTerminal();

// Verifica se é uma janela Swing e maximiza
this.screen = new TerminalScreen(terminal);
screen.startScreen();
screen.setCursorPosition(null);
```

#### Consequences

- Encapsulamento da Complexidade: Os detalhes específicos de criar uma janela Swing, definir estados expandidos e gerir frames AWT ficam escondidos dentro da utilização da factory e do construtor da GameScreen.
- Desacoplamento: A lógica principal do jogo interage com a interface genérica Screen e não precisa de conhecer a implementação subjacente em Swing nem os detalhes de gestão da janela.

### Strategy

#### Context Problems

O nosso jogo precisa de renderizar ecrãs completamente diferentes dependendo da fase do jogo, por exemplo, o nenu principal, o ecrã de apostas ou a mesa de Jogo. No inicio, a classe GameScreen dependia de cadeias complexas de switch para decidir o que desenhar com base no estado. Isto criava uma classe que define muitos comportamentos repetidos em várias instruções condicionais, tornando-a rígida e violando o Princípio Open/Closed.

#### The Pattern

Nós aplicamos o padrão Strategy. Este padrão de design comportamnetal define uma familia de algoritmos, encapsula cada um, e torna-os intercambiáveis. Criámos uma interface comum ViewStrategy que define o método draw(). Classes específicas (MenuStrategy, BettingStrategy, GameStateStrategy) implementam esta interface, fornecendo diferentes variantes do algoritmo de renderização.

#### Implementation

A implementação deste padrão pode ser encontrada no pacote [Strategies](/src/main/java/View/Strategies). O GameScreen atribui a lógica de desenho à ViewStrategy, uma explicação da transição de estados é fornecida no diagrama seguinte:

<p align="center">
  <img src="https://github.com/user-attachments/assets/85625cbc-2fa6-41de-9614-28e97a0e1dfe" width="700" />
</p>

#### Consequences

- Eliminação de Instruções Condicionais: Conseguimos remover a necessidade de grandes blocos de if-else na lógica de renderização.
- Fornece Diferentes Implementações: Cada ecrã tem a sua própria classe dedicada. Se precisarmos de alterar a aparência do ecrã de Apostas, modificamos apenas a BettingStrategy.

## KNOWN CODE SMELLS

Nós corrigimos todos os erros reportados pelo error-prone. Não foram identificados outros problemas graves no código.

## TESTING

- Instruction Coverage ≈ 90%
- Branch Coverage ≈ 80%
  <p align="center">
    <img src="https://github.com/user-attachments/assets/11badf9f-aa66-482c-97cc-127055659cd4" />
  </p>
  
- Line Coverage ≈ 88%
- Mutation Coverage ≈ 53%
- Test Strength ≈ 58%
  <p align="center">
    <img src="https://github.com/user-attachments/assets/2161c4aa-6bb0-4788-a8f5-bc5d5a5c0892" />
  </p>

## SELF-EVALUATION
    - Nuno Sousa: 50.0%
    - Guilherme Pinho: 50.0%
    
