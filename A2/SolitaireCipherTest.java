package assignment2;

import assignment2.Deck;
import assignment2.SolitaireCipher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.junit.jupiter.api.Assertions.*;

public class SolitaireCipherTest {

    private Deck deck;
    private SolitaireCipher cipher;

    @BeforeEach
    public void setup() {
        deck = new Deck(5, 2);
        Deck.gen.setSeed(10);

        deck.shuffle();

        cipher = new SolitaireCipher(deck);
    }

    @Test
    public void testGeneratedKeyStream() {
        int size = 12;

        int[] keystream = new int[size];
        for(int i = 0; i < size; i++) {
            int keyPart = this.deck.generateNextKeystreamValue();
            keystream[i] = keyPart;

            assertTrue(this.checkCorrectReferences(this.deck));

            System.out.println(keyPart);
        }

        for(int keyPart: keystream) {
            System.out.print(keyPart+ " ");
        }
    }

    @Test
    public void fixParticularCase() {
        Deck pDeck = new Deck();
        Deck.Card j1 = deck.new Joker("red");
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 5);
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[1], 5);
        Deck.Card c4 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);
        Deck.Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Deck.Card c6 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Deck.Card j2 = deck.new Joker("black");
        Deck.Card c9 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Deck.Card c10 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);


        pDeck.addCard(j1);
        pDeck.addCard(c1);
        pDeck.addCard(c2);
        pDeck.addCard(c3);
        pDeck.addCard(c4);
        pDeck.addCard(c5);
        pDeck.addCard(c6);
        pDeck.addCard(c7);
        pDeck.addCard(c8);
        pDeck.addCard(j2);
        pDeck.addCard(c9);
        pDeck.addCard(c10);

        System.out.println(getDeckString(pDeck));
        System.out.println(checkCorrectReferences(pDeck));

        System.out.println(pDeck.generateNextKeystreamValue());
    }

    @Test
    public void getFirstValue() {
        int firstValue = deck.generateNextKeystreamValue();
        assertEquals(firstValue, 4);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void testEncode() {
        String encodedMessage = cipher.encode("Is that you, Bob?");
        System.out.println(encodedMessage);
        assertEquals(encodedMessage, "MWIKDVZCKSFP");
    }

    @Test
    public void testDecode() {
        String encodedMessage = "MWIKDVZCKSFP";
        String decodedMessage = cipher.decode(encodedMessage);
        assertEquals(decodedMessage, "ISTHATYOUBOB");
    }

    private boolean checkCorrectReferences(Deck deck) {
        List<Deck.Card> forward = new ArrayList<>();

        Deck.Card current = deck.head;
        while(current.next != deck.head) {
            forward.add(current);
            current = current.next;
        }
        forward.add(current);

        List<Deck.Card> backward = new ArrayList<>();
        current = deck.head.prev;
        while(current.prev != deck.head.prev) {
            backward.add(current);
            current = current.prev;
        }
        backward.add(current);

        Collections.reverse(backward);

        return forward.equals(backward);
    }

    private String getDeckString(Deck deck) {
        String actual = "";

        Deck.Card head = deck.head;
        Deck.Card current = head;
        while(current.next != head) {
            actual = actual + current.toString() + " ";

            current = current.next;

        }
        actual = actual + current.toString();
        return actual;
    }
}
