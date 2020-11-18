package flashcards;

import java.util.Objects;

public class Card {
    private final String term;
    private final String definition;
    private int mistakes;

    public Card(String term, String definition) {
        this.term = term;
        this.definition = definition;
        this.mistakes = 0;
    }

    public Card(String[] importString) {
        this.term = importString[0];
        this.definition = importString[1];
        this.mistakes = Integer.parseInt(importString[2], 10);
    }

    public String getTerm() {
        return term;
    }

    public Card resetMistakes() {
        this.mistakes = 0;
        return this;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void addMistake() {
        this.mistakes++;
    }

    public String getDefinition() {
        return definition;
    }

    public String serializeToText() {
        return String.format("%s %s %d\n", term, definition, mistakes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return definition.equals(card.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition);
    }
}
