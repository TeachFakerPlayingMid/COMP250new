package assignment2;

import assignment2.Deck;
import assignment2.Deck.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setup() {
        deck = new Deck(4, 3);
        Deck.gen.setSeed(10);
    }

    @Test
    public void testDeckConstructor1() {
        String expected = "AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ";
        String actual = getDeckString(deck);

        assertEquals(actual, expected);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void testDeckConstructor2() {
        Deck copiedDeck = new Deck(deck);

        List<Card> initialDeckCards = getCardsInDeck(deck);
        List<Card> copiedDeckCards = getCardsInDeck(copiedDeck);

        assertEquals(initialDeckCards.size(), copiedDeckCards.size());

        String expected = getDeckString(deck);
        String actual = getDeckString(copiedDeck);
        assertEquals(actual, expected);

        for(int i = 0; i < initialDeckCards.size(); i++) {
            Card initialCard = initialDeckCards.get(i);
            Card copiedCard = copiedDeckCards.get(i);

            assertNotSame(initialCard, copiedCard);
        }

        assertTrue(this.checkCorrectReferences(this.deck));

    }

    @Test
    public void testDeckConstructorInvalidInputs() {
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck(1300, 4);
        });
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck(-2, 4);
        });
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck(2, -1);
        });
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck( 12, 130);
        });
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck(-2, -4);
        });
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = new Deck(130, 40);
        });
    }

    @Test
    public void testEmptyDeckPassed() {
        Deck copiedDeck = new Deck(new Deck());
        assertEquals(copiedDeck.numOfCards, 0);
        assertNull(copiedDeck.head);
    }

    @Test
    public void testOneCardDeckPassed() {
        Deck testDeck = new Deck();
        testDeck.addCard(deck.new PlayingCard(Deck.suitsInOrder[0], 1));

        Deck copiedDeck = new Deck(testDeck);
        assertEquals(copiedDeck.numOfCards, 1);
        Card head = copiedDeck.head;
        assertEquals(head.next, head);
        assertEquals(head.prev, head);
        assertNotSame(head, testDeck.head);
    }


    @Test
    public void testAddCardEmptyDeck() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);

        testDeck.addCard(c1);
        Card head = testDeck.head;
        assertEquals(head, head.next);
        assertEquals(head, head.prev);

        testDeck.addCard(c2);
        assertEquals(head.next, c2);
        assertEquals(head.prev, c2);
        assertEquals(c2.next, head);
        assertEquals(c2.prev, head);

        assertEquals(getDeckString(testDeck), "AC 2C");
    }

    @Test
    public void testAddCard() {
        Deck copiedDeck = new Deck();

        Card c1 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 4);
        Card j1 = copiedDeck.new Joker("red");
        Card j2 = copiedDeck.new Joker("black");

        copiedDeck.addCard(c1);
        copiedDeck.addCard(c2);
        copiedDeck.addCard(c3);
        copiedDeck.addCard(c4);
        copiedDeck.addCard(j1);
        copiedDeck.addCard(j2);

        String expected = "AC 2C 3C 4C RJ BJ";
        String actual = getDeckString(copiedDeck);
        assertEquals(expected, actual);
        assertEquals(copiedDeck.numOfCards, 6);

        assertTrue(this.checkCorrectReferences(copiedDeck));
    }

    @Test
    public void testShuffle1() {
        String expected = sortString(getDeckString(this.deck));
        deck.shuffle();
        String actual = sortString(getDeckString(this.deck));

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void testShuffle2() {
        Deck deck = new Deck(5, 2);

        String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D";
        deck.shuffle();
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void testShuffle3() {
        deck.shuffle();
        String expected = "AC 2D 3H 4D AD 4H 4C AH 2C RJ 3C BJ 3D 2H";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void testMethodWithoutJoker() {
        Deck copiedDeck = new Deck();

        Card c1 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 4);

        copiedDeck.addCard(c1);
        copiedDeck.addCard(c2);
        copiedDeck.addCard(c3);
        copiedDeck.addCard(c4);

        assertNull(copiedDeck.locateJoker("red"));
        assertNull(copiedDeck.locateJoker("black"));
    }

    @Test
    public void testWrongLocateJoker() {
        Deck copiedDeck = new Deck();

        Card c1 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = copiedDeck.new PlayingCard(Deck.suitsInOrder[0], 4);
        Card j1 = copiedDeck.new Joker("red");

        copiedDeck.addCard(c1);
        copiedDeck.addCard(c2);
        copiedDeck.addCard(c3);
        copiedDeck.addCard(c4);
        copiedDeck.addCard(j1);

        assertNotNull(copiedDeck.locateJoker("red"));
        assertEquals(copiedDeck.locateJoker("red"), j1);
        assertNull(copiedDeck.locateJoker("black"));
    }

    @Test
    public void testLocateJokerWithShuffle() {
        deck.shuffle();

        Card redJoker = this.deck.head.next.next.next.next.next.next.next.next.next;
        Card blackJoker = redJoker.next.next;

        Deck.Joker redJokerReal = deck.locateJoker("red");
        Deck.Joker blackJokerReal = deck.locateJoker("black");

        assertEquals(redJokerReal, redJoker);
        assertEquals(blackJokerReal, blackJoker);
    }

    @Test
    public void toMoveCard() {
        Card toMove = this.deck.head.next.next;
        deck.moveCard(toMove, 2);
        String expected = "AC 2C 4C AD 3C 2D 3D 4D AH 2H 3H 4H RJ BJ";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveCard2() {
        Card toMove = this.deck.head;
        for(int i = 0; i < 10; i++) {
            toMove = toMove.next;
        }

        deck.moveCard(toMove, 6);
        String expected = "AC 2C 3C 3H 4C AD 2D 3D 4D AH 2H 4H RJ BJ";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveCard3() {
        Card toMove = this.deck.head;
        for(int i = 0; i < 10; i++) {
            toMove = toMove.next;
        }

        deck.moveCard(toMove, 3);
        String expected = "AC 2C 3C 4C AD 2D 3D 4D AH 2H 4H RJ BJ 3H";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveHeadCard() {
        deck.moveCard(deck.head, 4);

        String expected = "AC 2D 3D 4D AH 2H 3H 4H RJ BJ 2C 3C 4C AD";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveBottomCard() {
        deck.moveCard(deck.head.prev, 1);

        String expected = "AC BJ 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveBottomCard2() {
        deck.moveCard(deck.head.prev.prev, 2);

        String expected = "AC RJ 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H BJ";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }

    @Test
    public void toMoveHighNumber() {
        deck.moveCard(deck.head.next, 15);
        String expected = "AC 3C 2C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));

    }

    @Test
    public void toMoveCardJoker() {
        deck.shuffle();
        Deck.Joker redJoker = deck.locateJoker("red");
        deck.moveCard(redJoker, 3);

        String expected = "AC 2D 3H 4D AD 4H 4C AH 2C 3C BJ 3D RJ 2H";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(this.deck));
    }



    @Test
    public void testTripleCut() {
        deck.shuffle();
        Deck.Joker redJoker = deck.locateJoker("red");
        Deck.Joker blackJoker = deck.locateJoker("black");
        deck.tripleCut(redJoker, blackJoker);

        String expected = "3D 2H RJ 3C BJ AC 2D 3H 4D AD 4H 4C AH 2C";
        String actual = getDeckString(deck);

        assertEquals(expected, actual);
    }

    @Test
    public void testTripleCutHeadJoker() {
        Deck testDeck = new Deck();
        Card j1 = deck.new Joker("red");
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card j2 = deck.new Joker("Black");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card[] cards = {j1, c1, c2, j2, c3, c4};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.tripleCut(testDeck.locateJoker("red"), testDeck.locateJoker("black"));
        String expected = "3C 4C RJ AC 2C BJ";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testTripleCutTailJoker() {
        Deck testDeck = new Deck();
        Card j1 = deck.new Joker("red");
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card j2 = deck.new Joker("Black");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card[] cards = {c1, c2, j2, c3, c4, j1};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.tripleCut(testDeck.locateJoker("black"), testDeck.locateJoker("red"));
        String expected = "BJ 3C 4C RJ AC 2C";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testTripleCutBothEndsJokers() {
        Deck testDeck = new Deck();
        Card j1 = deck.new Joker("red");
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card j2 = deck.new Joker("Black");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card[] cards = {j1, c1, c2, c3, c4, j2};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.tripleCut(testDeck.locateJoker("red"), testDeck.locateJoker("black"));
        String expected = "RJ AC 2C 3C 4C BJ";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testTripleCutRandomCards() {
        Deck testDeck = new Deck();
        Card j1 = deck.new Joker("red");
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card j2 = deck.new Joker("Black");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card[] cards = {j1, c1, c2, c3, c4, j2};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.tripleCut(testDeck.head.next, testDeck.head.prev.prev);
        String expected = "BJ AC 2C 3C 4C RJ";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testTripleCutRandomCards2() {
        Deck testDeck = new Deck();
        Card j1 = deck.new Joker("red");
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card j2 = deck.new Joker("Black");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card[] cards = {j1, c1, c2, c3, c4, j2};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.tripleCut(testDeck.head.next.next, testDeck.head.prev.prev);
        String expected = "BJ 2C 3C 4C RJ AC";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testCountCut() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[0], 5);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[0], 6);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[0], 7);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8);

        Card[] cards = {c1, c3, c4, c5, c6, c7, c8, c2};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.countCut();

        String expected = "4C 5C 6C 7C 8C AC 3C 2C";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));

    }

    @Test
    public void testCountCut2() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[0], 5);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[0], 6);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[0], 7);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c8, c7};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.countCut();
        String expected = "AC 2C 3C 4C 5C 6C 8C 7C";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(checkCorrectReferences(testDeck));
    }

    @Test
    public void testCountCutMultiple() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[0], 5);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[0], 6);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[0], 7);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.countCut();
        String expected = "AC 2C 3C 4C 5C 6C 7C 8C";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    public void testCountCutOverlap() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        testDeck.countCut();

        String expected = "2C 3C 4C AD 2D 3D AC 4D";
        String actual = getDeckString(testDeck);

        assertEquals(expected, actual);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void lookUpTest() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        Card card = testDeck.lookUpCard();
        assertEquals(card, c2);
    }

    @Test
    public void lookUpTestJoker() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Card c2 = deck.new Joker("red");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        Card card = testDeck.lookUpCard();
        assertNull(card);
    }

    @Test
    public void lookUpTestMultiple() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 8);
        Card c2 = deck.new Joker("red");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        Card card = testDeck.lookUpCard();
        assertEquals(card, c1);
    }

    @Test
    public void testLookUpOverlap() {
        Deck testDeck = new Deck();
        Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 10);
        Card c2 = deck.new Joker("red");
        Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);

        Card c5 = deck.new PlayingCard(Deck.suitsInOrder[1], 1);
        Card c6 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Card c7 = deck.new PlayingCard(Deck.suitsInOrder[1], 3);
        Card c8 = deck.new PlayingCard(Deck.suitsInOrder[1], 4);

        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8};
        for(Card card: cards) {
            testDeck.addCard(card);
        }

        Card card = testDeck.lookUpCard();
        assertEquals(card, c3);
    }

    @Test
    public void testGenerateNextKeystreamValue() {
        deck.shuffle();
        int nextValue = deck.generateNextKeystreamValue();
        assertEquals(3, nextValue);
        assertTrue(this.checkCorrectReferences(deck));
    }

    @Test
    public void testGenerateValue2() {
        Deck testDeck = new Deck(5, 2);
        testDeck.shuffle();

        int nextValue = testDeck.generateNextKeystreamValue();
        assertEquals(4, nextValue);
        assertTrue(this.checkCorrectReferences(testDeck));
    }

    @Test
    public void testGenerateValue3() {

        int nextValue = deck.generateNextKeystreamValue();
        assertEquals(4, nextValue);
        assertTrue(this.checkCorrectReferences(deck));
    }

    private String getDeckString(Deck deck) {
        String actual = "";

        Card head = deck.head;
        Card current = head;
        while(current.next != head) {
            actual = actual + current.toString() + " ";

            current = current.next;

        }
        actual = actual + current.toString();
        return actual;
    }

    private List<Card> getCardsInDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();

        Card head = deck.head;
        Card current = head;
        while(current.next != head) {
            cards.add(current);
            current = current.next;
        }

        cards.add(current);

        return cards;
    }

    private String sortString(String strToSort) {
        char[] charArray = strToSort.toCharArray();
        Arrays.sort(charArray);

        return new String(charArray);
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
}
