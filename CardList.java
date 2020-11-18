package flashcards;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CardList {
    private final Map<String, Card> cards = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = Logger.getInstance();

    public void addNewCard() {
        logger.logMessageWithNL("The card:");
        String term = scanner.nextLine();
        logger.logInput(term);
        if (cards.containsKey(term)) {
            logger.logMessageWithNL(String.format("The card \"%s\" already exists.", term));
            return;
        }

        logger.logMessageWithNL("The definition of the card:");
        String definition = scanner.nextLine();
        logger.logInput(definition);
        if (cards.containsValue(new Card(term, definition))) {
            logger.logMessageWithNL(String.format("The definition \"%s\" already exists.", definition));
            return;
        }

        cards.put(term, new Card(term, definition));
        logger.logMessageWithNL(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
    }

    public void deleteCard() {
        logger.logMessageWithNL("Which card?");
        String term = scanner.nextLine();
        logger.logInput(term);
        if (!cards.containsKey(term)) {
            logger.logMessageWithNL(String.format("Can't remove \"%s\": there is no such card.", term));
            return;
        }

        cards.remove(term);
        logger.logMessageWithNL("The card has been removed.");
    }

    public void importValues(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] value = line.split(" ");
                cards.put(value[0], new Card(value));
            }
            logger.logMessageWithNL(String.format("%d cards have been loaded.", lines.size()));
        }
    }

    public void importValues() throws IOException {
        logger.logMessageWithNL("File name:");
        String name = scanner.nextLine();
        logger.logInput(name);
        File file = new File(name);
        if (file.exists() && !file.isDirectory()) {
            List<String> lines = Files.readAllLines(Paths.get(name));
            for (String line : lines) {
                String[] value = line.split(" ");
                cards.put(value[0], new Card(value));
            }
            logger.logMessageWithNL(String.format("%d cards have been loaded.", lines.size()));
        } else {
            logger.logMessageWithNL("File not found.");
        }
    }

    public void exportValues() throws IOException {
        logger.logMessageWithNL("File name:");
        String name = scanner.nextLine();
        logger.logInput(name);
        PrintWriter printWriter = new PrintWriter(name);
        for (Card card : cards.values()) {
            printWriter.print(card.serializeToText());
        }
        printWriter.close();
        logger.logMessageWithNL(String.format("%d cards have been saved.", cards.size()));
    }

    public void exportValues(String filename) throws IOException {
        PrintWriter printWriter = new PrintWriter(filename);
        for (Card card : cards.values()) {
            printWriter.print(card.serializeToText());
        }
        printWriter.close();
        logger.logMessageWithNL(String.format("%d cards have been saved.", cards.size()));
    }

    public void getHardestCards() {
        List<Card> baseList = new ArrayList<>(cards.values());
        baseList.sort(Comparator.comparingInt(Card::getMistakes));
        Optional<Card> optional = baseList.stream().max(Comparator.comparingInt(Card::getMistakes));
        if (optional.isPresent()) {
            Card maxCard = optional.get();
            List<String> mostMistakeCardsList = new ArrayList<>();
            for (Card card : baseList) {
                if (card.getMistakes() == maxCard.getMistakes()) {
                    mostMistakeCardsList.add(card.getTerm());
                }
            }

            if (maxCard.getMistakes() == 0) {
                logger.logMessageWithNL("There are no cards with errors.");
            } else if (mostMistakeCardsList.size() == 1) {
                logger.logMessageWithNL(String.format("The hardest card is \"%s\". " +
                        "You have %d errors answering it.",
                        mostMistakeCardsList.get(0), maxCard.getMistakes()));
            } else {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < mostMistakeCardsList.size() - 1; i++) {
                    result.append("\"");
                    result.append(mostMistakeCardsList.get(i));
                    result.append("\", ");
                }
                result.append("\"");
                result.append(mostMistakeCardsList.get(mostMistakeCardsList.size() - 1));
                result.append("\"");
                logger.logMessageWithNL(String.format("The hardest cards are %s. " +
                        "You have %d errors answering them.",
                        result.toString(), maxCard.getMistakes()));
            }
        } else {
            logger.logMessageWithNL("There are no cards with errors.");
        }
    }

    public void resetMistakeStatistics() {
        cards.replaceAll((term, card) -> card.resetMistakes());
        logger.logMessageWithNL("Card statistics have been reset.");
    }

    private String getKeyByValue(String value) {
        for (Map.Entry<String, Card> entry : cards.entrySet()) {
            if (entry.getValue().getDefinition().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void checkCardKnowledge() {
        List<Card> cardList = new ArrayList<>(cards.values());
        logger.logMessageWithNL("How many times to ask?");
        int questionNumber = scanner.nextInt();
        scanner.nextLine();
        logger.logInput(questionNumber + "\n");

        for (int i = 0; i < questionNumber; i++) {
            Card tempCard = cardList.get(i % cardList.size());
            logger.logMessageWithNL(String.format("Print the definition of \"%s\":", tempCard.getTerm()));
            String answer = scanner.nextLine();
            logger.logInput(answer);
            if (answer.equals(tempCard.getDefinition())) {
                logger.logMessageWithNL("Correct!");
            } else {
                logger.logMessage(String.format("Wrong. The right answer is \"%s\"", tempCard.getDefinition()));
                tempCard.addMistake();
                String result = getKeyByValue(answer);
                if (result != null) {
                    logger.logMessageWithNL(String.format(", but your definition is correct for \"%s\".", result));
                } else {
                    logger.logMessage("\n");
                }
            }
        }
    }
}
