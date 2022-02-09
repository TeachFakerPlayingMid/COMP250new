package assignment2;

import javax.swing.*;
import java.util.Random;

public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

    /*
  * TODO: Initializes a Deck object using the inputs provided
  */
 public Deck(int numOfCardsPerSuit, int numOfSuits)
 {
  /**** ADD CODE HERE ****/
      //throw Exception
      if (numOfCardsPerSuit<1 || numOfCardsPerSuit>13)
      {
            throw new IllegalArgumentException("numOfCardsPerSuit should between 1 and 13");
      }
      if (numOfSuits<1 || numOfSuits > suitsInOrder.length)
      {
            throw new IllegalArgumentException("numOfSuits should between 1 and number of suits");
      }

     Deck deck = new Deck();
     int[] rankArray = {1,2,3,4,5,6,7,8,9,10,11,12,13};
     for (int i = 0; i<numOfSuits;i++)
     {
           for (int j=0;j<numOfCardsPerSuit;j++)
           {
                Deck.PlayingCard oneCard = deck.new PlayingCard(suitsInOrder[i],rankArray[j]);
                //Card[] cardArray = new Card[numOfCards];
               addCard(oneCard);
           }
     }
     Joker redJoker = new Joker("red");
     Joker blackJoker = new Joker("black");
     addCard(redJoker);
     addCard(blackJoker);
 }


 /*
  * TODO: Implements a copy constructor for Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d)
     {
      /**** ADD CODE HERE ****/
      Deck deck = new Deck();
      Deck.Card cardCopy = d.head;
      for (int i = 0;i<d.numOfCards;i++)
      {
          addCard(cardCopy.getCopy());
          cardCopy= cardCopy.next;
      }
     }

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {}

 /* 
  * TODO: Adds the specified card at the bottom of the deck. This 
  * method runs in $O(1)$. 
  */
 public void addCard(Card c)
 {
  /**** ADD CODE HERE ****/
  //PlayingCard newCard = new PlayingCard(null,0);
  Card last = head;
     if (numOfCards==0)
  {
      head=c;
      c.next = head;
      c.prev = head;
  }
  else
  {
      Card oldLast = head.prev;
      head.prev = c;
      c.next=head;
      oldLast.next = c;
      c.prev=oldLast;
  }
  numOfCards++;
 }

 /*
  * TODO: Shuffles the deck using the algorithm described in the pdf. 
  * This method runs in O(n) and uses O(n) space, where n is the total 
  * number of cards in the deck.
  */
 public void shuffle()
 {
  /**** ADD CODE HERE ****/
  Card[] shuffleArray = new Card[numOfCards];
  Card temp1 = head;//add the card into the array
  for (int i = 0;i < numOfCards;i++)
  {
      shuffleArray[i] = temp1;
      temp1=temp1.next;
  }
  //int randomIndex = gen.nextInt(shuffleArray.length);
  Card randomChoosing = null;
  Card temp2 = null;
  for (int j = shuffleArray.length-1;j>=1;j--)
  {
      int randomIndex = gen.nextInt(j+1);
      randomChoosing = shuffleArray[randomIndex];
      temp2 = randomChoosing;
      shuffleArray[randomIndex] = shuffleArray[j];
      shuffleArray[j] = temp2;
  }
  numOfCards=0;
  for (int k = 0;k<shuffleArray.length;k++)
  {
      //Card rootCard = shuffleArray[0];
      addCard(shuffleArray[k]);
  }
 }

 /*
  * TODO: Returns a reference to the joker with the specified color in 
  * the deck. This method runs in O(n), where n is the total number of 
  * cards in the deck. 
  */
 public Joker locateJoker(String color)
 {
  /**** ADD CODE HERE ****/
  Card toolCard1 = head;
  if (toolCard1 instanceof Joker &&((Joker) toolCard1).getColor().equals(color))
  {
      return (Joker) toolCard1;
  }
  for (int i = 0; toolCard1.next != head;i++)
  {
      toolCard1=toolCard1.next;
      if (toolCard1 instanceof Joker && ((Joker) toolCard1).getColor().equals(color))
      {
          return (Joker) toolCard1;
      }
  }
  return null;
 }

 /*
  * TODO: Moved the specified Card, p positions down the deck. You can 
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */
 public void moveCard(Card c, int p)
 {
  /**** ADD CODE HERE ****/
  Card temp = c.next;
  int count = (int)Math.floor(p/numOfCards);
  for (int i = 0;i<p+count;i++)
  {
      if (i==p-1+count)
      {
          Card tool1 = c.next;
          Card tool2 = c.prev;
          Card tool3 = temp.next;
          c.prev.next = tool1;
          c.next.prev=tool2;
          temp.next = c;
          tool3.prev = c;
          c.prev = temp;
          c.next = tool3;
      }
      temp=temp.next;
  }
 }

 /*
  * TODO: Performs a triple cut on the deck using the two input cards. You 
  * can assume that the input cards belong to the deck and the first one is 
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard)
 {
  /**** ADD CODE HERE ****/
  /* Card toolCard1 = head;
  Card toolCard2 = firstCard.prev;
  Card toolCard3 = secondCard.next;
  Card toolCard4 = head.prev;
  toolCard3.prev = toolCard2;
  toolCard2.next = toolCard3;
  head = toolCard3;
  secondCard.next=toolCard1;
  toolCard1.prev = secondCard;
  firstCard.prev = toolCard4;
  toolCard4.next = firstCard;
  head.prev = toolCard2;*/
     if (firstCard == head && secondCard != head.prev) {
         head = secondCard.next;
     }
     else if (firstCard != head && secondCard == head.prev){
        head = firstCard;
     }
     else{
         Card first = head;
         Card before = firstCard.prev;
         Card after = secondCard.next;
         Card last = head.prev;
         first.prev = secondCard;
         before.next = after;
         firstCard.prev = last;
         secondCard.next = first;
         after.prev = before;
         last.next = firstCard;
         head = after;
     }
 }

 /*
  * TODO: Performs a count cut on the deck. Note that if the value of the 
  * bottom card is equal to a multiple of the number of cards in the deck, 
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut()
 {
  /**** ADD CODE HERE ****/
  Card last = head.prev;
  Card secondLast = null;
  Card temp = head;
  int lastValue = last.getValue();
  if (numOfCards != 0)
  {
      for (int i=0;i<(lastValue % numOfCards);i++)
      {
          // temp = cardMove;
          secondLast = last.prev;
          head = temp.next;
          temp.next.prev = last;
          last.prev=temp;
          last.next = temp.next;
          temp.next=last;
          temp.prev = secondLast;
          secondLast.next=temp;
          // cardMove=cardMove.next;
          temp = head;
      }
  }
  else
  {
      System.out.println("empty deck");
  }
 }

 /*
  * TODO: Returns the card that can be found by looking at the value of the 
  * card on the top of the deck, and counting down that many cards. If the 
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard()
 {
  /**** ADD CODE HERE ****/
  Card countCard = head;
  //Card temp = topCard;
  int topValue = head.getValue();
  for (int i=0;i< topValue;i++)
  {
      countCard=countCard.next;
  }
  if (countCard instanceof Joker)
  {
      return null;
  }
  return countCard;
 }

 /*
  * TODO: Uses the Solitaire algorithm to generate one value for the keystream 
  * using this deck. This method runs in O(n).
  */
 public int generateNextKeystreamValue()
 {
  /**** ADD CODE HERE ****/
  Card toolCard = head;
  Card blackJoker = locateJoker("black");
  Card redJoker = locateJoker("red");
  moveCard(redJoker,1);
  moveCard(blackJoker,2);
 if (head.prev instanceof Joker)
  {
      moveCard(head.prev,1);
  }
  if (head.prev.prev instanceof Joker)
  {
      moveCard(head.prev.prev, 2);
  }
  while (toolCard.next != head)
  {
      if (toolCard instanceof Joker)
      {
          if (toolCard.toString().equals("BJ"))
          {
              tripleCut(blackJoker,redJoker);
              countCut();
              return lookUpCard().getValue();
          }
          if (toolCard.toString().equals("RJ"))
          {
              tripleCut(redJoker,blackJoker);
              countCut();
              return lookUpCard().getValue();
          }
      }
      toolCard=toolCard.next;
  }
  countCut();
  return lookUpCard().getValue();
 }


 public abstract class Card { 
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);   
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
    throw new IllegalArgumentException("Jokers can only be red or black"); 

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

}
