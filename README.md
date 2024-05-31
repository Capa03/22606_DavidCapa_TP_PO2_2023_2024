### Relatório PO2

Autor: David Capa

#### Issues:
https://github.com/Capa03/PO2/issues?q=is%3Aissue+is%3Aclosed

#### Trello:
https://github.com/users/Capa03/projects/5


#### Classe: WSModel

##### 1. Método: `positionSelected`

```java
public void positionSelected(Position currentPosition) {
    if (currentPosition == null) {
        this.previousButtonPosition = null;
        this.positions.clear();
        return;
    }

    if (this.previousButtonPosition != null) {
        String word = this.checkWord(this.previousButtonPosition, currentPosition);
        word = this.wordFound(word);
        this.previousButtonPosition = null;

        if (!word.equals("Not Match")) {
            wsView.update(new MessageToUI(this.positions, "match"));
        }

        this.positions.clear();
    } else {
        this.previousButtonPosition = currentPosition;
    }
}
```

**Descrição**:
Função de fluxo que recebe uma posição, executando as funções definidas caso abas as posições se encontram preenchidas

**Ciclo**: Chama `checkWord`, que vai construir a palavra, enviando o valor para `wordFound` que vai verificar se a palavra existe no tabuleiro.

##### 2. Método: `checkWord`

```java
private String checkWord(Position previousPosition, Position currentPosition) {
    StringBuilder word = new StringBuilder();
    int startLine = Math.min(previousPosition.line(), currentPosition.line());
    int endLine = Math.max(previousPosition.line(), currentPosition.line());
    int startCol = Math.min(previousPosition.col(), currentPosition.col());
    int endCol = Math.max(previousPosition.col(), currentPosition.col());

    // Loop através do intervalo dos botões
    for (int line = startLine; line <= endLine; line++) {
        for (int col = startCol; col <= endCol; col++) {
            this.positions.add(new Position(line, col));
            word.append(this.textInPosition(new Position(line, col)));
        }
    }
    this.previousButtonPosition = null;
    return word.toString();
}
```

**Descrição**:
Este método percorre o tabuleiro no intervalo definido pela posição inicial e final. Ele adiciona as posições percorridas numa lista e constrói a palavra correspondente.

**Ciclo**: Duplo ciclo `for` que itera sobre as linhas e colunas no intervalo definido.

##### 3. Método: `wordFound`

```java
public String wordFound(String word) {
    Set<String> solutionsSet = new HashSet<>();
    for (String solution : getSolutions()) {
        solutionsSet.addAll(Arrays.asList(solution.split("\\s+")));
    }

    if (solutionsSet.contains(word)) {
        this.wordsFound.add(word);
        StringBuilder message = new StringBuilder(word + " ");
        for (Position p : this.positions) {
            message.append(String.format("(%d, %c) ", p.line(), (char) ('A' + p.col())));
        }
        this.wsView.updateInfoLabel(message.toString());
        return word;
    }
    return "Not Match";
}
```

**Descrição**:
Este método verifica se a palavra formada é uma das soluções. Se for, adiciona a palavra à lista de palavras encontradas.

**Ciclo**: A função percorre uma lista de soluções e, em seguida, verifica se a solução contém a palavra. Se a condição for verdadeira, adiciona a palavra a uma lista de palavras encontradas e constrói uma mensagem percorrendo um ciclo `for` com os movimentos, enviando-os para a `view`.

##### 4. Método: `allWordsWereFound`

```java
public boolean allWordsWereFound() {
    List<String> solutions = getSolutions();

    Set<String> allSolutionsWords = new HashSet<>();
    for (String solution : solutions) {
        allSolutionsWords.addAll(Arrays.asList(solution.split("\\s+")));
    }
    return this.wordsFound.size() == allSolutionsWords.size() &&
            allSolutionsWords.containsAll(this.wordsFound);
}
```

**Descrição**:
O método `allWordsWereFound` verifica se todas as palavras presentes nas soluções foram encontradas pelo jogador. Ele faz isso comparando o conjunto de palavras encontradas pelo jogador com o conjunto de todas as palavras possíveis das soluções.

**Ciclo**: O método usa um ciclo para iterar sobre todas as soluções, dividindo cada solução em palavras individuais e adicionando essas palavras a um conjunto (`Set`). Este conjunto é então comparado com o conjunto de palavras encontradas pelo jogador para determinar se todas as palavras foram encontradas.

#### Classe: WSBoard

##### 1. Método: `handleButtonAction`

```java
private void handleButtonAction(Button button) {
    Position buttonPosition = new Position(getRowIndex(button), getColumnIndex(button));
    Background background = button.getBackground();
    boolean isYellow = background != null && background.getFills().stream()
            .anyMatch(fill -> fill.getFill().equals(Color.YELLOW));

    if (isYellow) {
        button.setStyle("");
        wsModel.positionSelected(null);
        previousButton.set(null);
    } else {
        button.setStyle("-fx-background-color: #FFFF00");

        wsModel.positionSelected(buttonPosition);

        if (previousButton.get() != null) {
            Position previousButtonPosition = new Position(getRowIndex(previousButton.get()), getColumnIndex(previousButton.get()));
            String stylePrevious = foundWordPositions.contains(previousButtonPosition) ? "-fx-background-color: #00D100" : "";
            previousButton.get().setStyle(stylePrevious);
            String styleCurrent = foundWordPositions.contains(buttonPosition) ? "-fx-background-color: #00D100" : "";
            button.setStyle(styleCurrent);

            previousButton.set(null);
        } else {
            previousButton.set(button);
        }
    }
}
```

**Descrição**:
Este método lida com a acção do botão quando é clicado. Ele verifica se o botão foi previamente clicado e marca a posição do botão, alterando o seu estilo.

**Ciclo**: Não contém mas interage com `wsModel.positionSelected` que envia a posição do botão.

##### 2. Método: `update`

```java
@Override
public void update(MessageToUI messageToUI) {
    for (Position p : messageToUI.positions()) {
        Button button = this.getButton(p.line(), p.col());
        if (button == null) {
            System.err.println("Button not found at position: " + p);
            continue;
        }
        button.setStyle("-fx-background-color: #00D100");
        foundWordPositions.add(p);
    }
    if (this.wsModel.allWordsWereFound()) {
        FileReadWrite readWrite = new FileReadWrite();
        readWrite.writeFile(getWordsFound(), "score", true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finish");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("Level completed!");
        alert.showAndWait();
        System.exit(0);
    }
}
```

**Descrição**:
Este método actualiza a interface do utilizador com base nas posições recebidas, alterando o estilo dos botões nas posições especificadas.

**Ciclo**: Ciclo `for` para iterar sobre as posições recebidas e actualizar os botões correspondentes.

##### 3. Método: `getWordsFound`

```java
public String getWordsFound() {
    StringBuilder sb = new StringBuilder();
    List<String> solutions = wsModel.getSolutions();
    Set<String> totalWords = new HashSet<>();

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String timestamp = now.format(formatter);

    for (String solution : solutions) {
        totalWords.addAll(Arrays.asList(solution.split("\\s+")));
    }
    List<String> wordsFound = this.wsModel.getWordsFound();
    int totalWordsCount = totalWords.size();
    int foundWordsCount = wordsFound.size();

    sb.append("Words found: " + "Time: " + timestamp + "\n");
    for (String word : wordsFound) {
        sb.append(word).append("\n");
    }

    double percentage = ((double) foundWordsCount / totalWordsCount) * 100;
    sb.append(String.format("Total words found: %d/%d (%.2f%%) \n", foundWordsCount, totalWordsCount, percentage));

    return sb.toString();
}
```

**Descrição**:
Este método retorna uma string que lista todas as palavras encontradas e a percentagem de palavras encontradas em relação ao total de palavras possíveis.

**Ciclo**: Ciclo `for` para iterar sobre as soluções e palavras encontradas.

#### Classe: BoardContent

A classe `BoardContent` implementa um modelo de tabuleiro para a colocação de palavras, gerando e gerindo o conteúdo do tabuleiro de forma eficiente. Cada método desempenha uma função específica, desde a inicialização do tabuleiro até à colocação e verificação de palavras. A implementação utiliza vários ciclos para iterar sobre os elementos do tabuleiro e das listas de palavras, garantindo que o conteúdo seja gerado e preenchido correctamente. O uso de classes auxiliares como `FileReadWrite` sugere uma arquitectura modular que facilita a manutenção e extensão do código.

##### 1. Método: `generateBoard`

```java
private String generateBoard(String wordString) {
    List<String> filteredWords = filterWordsBySize(wordString.split("\\s+"), getCols());
    List<String> selectedWords = selectRandomWords(filteredWords);
    List<List<Character>> board = initializeBoard(getRows(), getCols());
    List<String> placedWords = placeWordsOnBoard(board, selectedWords);

    fillEmptyCells(board);
    this.words.add

All(placedWords);
    this.solutions.addAll(this.words);

    return convertBoardToString(board);
}
```

**Descrição**:
Este método gera o tabuleiro com as palavras dadas, colocando as palavras e preenchendo células vazias com letras aleatórias.

**Ciclo**: Chama métodos que possuem vários ciclos, incluindo `filterWordsBySize`, `selectRandomWords`, `initializeBoard`, `placeWordsOnBoard`, e `fillEmptyCells`.

##### 2. Método: `filterWordsBySize`

```java
private List<String> filterWordsBySize(String[] words, int maxSize) {
    List<String> filteredWords = new ArrayList<>();
    for (String word : words) {
        if (word.length() <= maxSize) {
            filteredWords.add(word);
        }
    }
    return filteredWords;
}
```

**Descrição**:
Este método filtra palavras pelo tamanho especificado, retornando apenas aquelas que cabem no tabuleiro.

**Ciclo**: Um ciclo `for` que itera sobre as palavras.

##### 3. Método: `selectRandomWords`

```java
private List<String> selectRandomWords(List<String> words) {
    List<String> selectedWords = new ArrayList<>();
    Random random = new Random();
    Set<String> selectedSet = new HashSet<>();

    while (selectedSet.size() < words.size()) {
        String word = words.get(random.nextInt(words.size()));
        if (!selectedSet.contains(word)) {
            selectedSet.add(word);
            selectedWords.add(word);
        }
    }

    return selectedWords;
}
```

**Descrição**:
Este método selecciona um subconjunto aleatório de palavras da lista filtrada.

**Ciclo**: Um ciclo `while` que continua até que o conjunto de palavras seleccionadas tenha o mesmo tamanho da lista original.

##### 4. Método: `initializeBoard`

```java
private List<List<Character>> initializeBoard(int rows, int cols) {
    List<List<Character>> board = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
        List<Character> boardRow = new ArrayList<>(Collections.nCopies(cols, PLACEHOLDER));
        board.add(boardRow);
    }
    return board;
}
```

**Descrição**:
Este método inicializa um tabuleiro 2D com placeholders.

**Ciclo**: Um ciclo `for` que itera sobre as linhas do tabuleiro.

##### 5. Método: `placeWordsOnBoard`

```java
private List<String> placeWordsOnBoard(List<List<Character>> board, List<String> words) {
    List<String> placedWords = new ArrayList<>();
    Random random = new Random();

    for (String word : words) {
        if (tryPlaceWord(board, word, random)) {
            placedWords.add(word);
        }
    }
    return placedWords;
}
```

**Descrição**:
Este método coloca palavras no tabuleiro se elas couberem, retorna uma lista de palavras colocadas com sucesso.

**Ciclo**: Um ciclo `for` que itera sobre as palavras a serem colocadas.

##### 6. Método: `tryPlaceWord`

```java
private boolean tryPlaceWord(List<List<Character>> board, String word, Random random) {
    for (int attempts = 0; attempts < 100; attempts++) {
        int row = random.nextInt(getRows());
        int col = random.nextInt(getCols());
        boolean horizontal = random.nextBoolean();

        if (canPlaceWord(board, word, row, col, horizontal)) {
            placeWord(board, word, row, col, horizontal);
            return true;
        }
    }
    return false;
}
```

**Descrição**:
Este método tenta colocar uma palavra no tabuleiro, tentando até um número máximo de vezes.

**Ciclo**: Um ciclo `for` com até 100 tentativas.

##### 7. Método: `canPlaceWord`

```java
private boolean canPlaceWord(List<List<Character>> board, String word, int row, int col, boolean horizontal) {
    if (horizontal) {
        if (col + word.length() > getCols()) return false;
        for (int i = 0; i < word.length(); i++) {
            if (board.get(row).get(col + i) != PLACEHOLDER) return false;
        }
    } else {
        if (row + word.length() > getRows()) return false;
        for (int i = 0; i < word.length(); i++) {
            if (board.get(row + i).get(col) != PLACEHOLDER) return false;
        }
    }
    return true;
}
```

**Descrição**:
Este método verifica se uma palavra pode ser colocada no tabuleiro na posição especificada.

**Ciclo**: Dois ciclos `for` que iteram sobre o comprimento da palavra.

##### 8. Método: `placeWord`

```java
private void placeWord(List<List<Character>> board, String word, int row, int col, boolean horizontal) {
    for (int i = 0; i < word.length(); i++) {
        if (horizontal) {
            board.get(row).set(col + i, word.charAt(i));
        } else {
            board.get(row + i).set(col, word.charAt(i));
        }
    }
}
```

**Descrição**:
Este método coloca uma palavra no tabuleiro na posição especificada.

**Ciclo**: Um ciclo `for` que itera sobre o comprimento da palavra.

##### 9. Método: `fillEmptyCells`

```java
private void fillEmptyCells(List<List<Character>> board) {
    Random random = new Random();
    for (List<Character> row : board) {
        for (int col = 0; col < getCols(); col++) {
            if (row.get(col) == PLACEHOLDER) {
                row.set(col, getRandomLetter(random));
            }
        }
    }
}
```

**Descrição**:
Este método preenche as células vazias do tabuleiro com letras aleatórias.

**Ciclo**: Dois ciclos `for`, um para iterar sobre as linhas e outro para as colunas.

##### 10. Método: `getRandomLetter`

```java
private char getRandomLetter(Random random) {
    return LETTERS[random.nextInt(LETTERS.length)];
}
```

**Descrição**:
Este método retorna uma letra aleatória do array de letras.

**Ciclo**: Não possui ciclos.

##### 11. Método: `convertBoardToString`

```java
private String convertBoardToString(List<List<Character>> board) {
    StringBuilder boardString = new StringBuilder(getRows() * (getCols() + 1));
    for (int row = 0; row < getRows(); row++) {
        for (int col = 0; col < getCols(); col++) {
            boardString.append(board.get(row).get(col));
        }
        if (row < getRows() - 1) {
            boardString.append('\n');
        }
    }
    return boardString.toString();
}
```

**Descrição**:
Este método converte o tabuleiro em uma string.

**Ciclo**: Dois ciclos `for`, um para iterar sobre as linhas e outro para as colunas.
