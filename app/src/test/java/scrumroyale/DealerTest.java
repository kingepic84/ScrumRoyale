 package scrumroyale;

 import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import scrumroyale.vegas.Dealer;

 class DealerTest {

     Map<Integer, List<Integer>> result;
     Map<Integer, List<Integer>> correctR;

     @Spy
     Dealer dealer;

     @BeforeEach
     public void setUp() {
         MockitoAnnotations.openMocks(this);
     }

     @Test // This makes sure the highest card is first in a pair
     void testpullCards() {
         ArrayList<Integer> deck = dealer.pullCards(2);
         boolean firstHighest = false;
         int setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
         deck = dealer.pullCards(2);
         firstHighest = false;
         System.out.println(deck);
         setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
         deck = dealer.pullCards(2);
         firstHighest = false;
         System.out.println(deck);
         setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
         deck = dealer.pullCards(2);
         firstHighest = false;
         System.out.println(deck);
         setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
         deck = dealer.pullCards(2);
         firstHighest = false;
         System.out.println(deck);
         setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
         deck = dealer.pullCards(2);
         firstHighest = false;
         System.out.println(deck);
         setValue = deck.get(0) + deck.get(1);
         if ((deck.get(0) >= deck.get(1)) && (deck.get(2) == setValue)) {
             firstHighest = true;
         }
         assertTrue(firstHighest,
                 "The first card should be greater than the second. Last element should be the first twos sum");
     }

     @Test // This test makes sure the first card in a pair is the higher of the 2
     void testHigherPair() {
         doReturn(new ArrayList<Integer>(Arrays.asList(10, 9, 19)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(6, 5, 11)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(8, 7, 15)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(4, 4, 8)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(4, 3, 7)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(3, 3, 6)))
                 .when(dealer).pullCards(2);
         result = dealer.dealToVegas(12, 2);
         correctR = Map.of(0, List.of(10, 9), 1, List.of(8, 7 ), 2, List.of(6, 5), 3, List.of(4, 4), 4, List.of( 4, 3 ), 5, List.of( 3, 3 ));
         verify(dealer, times(6)).pullCards(2);
         assertTrue(result.equals(correctR), "The card pairs should be in descending order");
     }

     @Test // This test checks for ties in dealer payouts
     void testDealerTies() {
         doReturn(new ArrayList<Integer>(Arrays.asList(10, 9, 19)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(6, 5, 11)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(8, 7, 15)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(5, 3, 8)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(4, 4, 8)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(3, 3, 6)))
                 .when(dealer).pullCards(2);
         result = dealer.dealToVegas(12, 2);
         correctR =Map.of(0, List.of(10, 9), 1, List.of(8, 7 ), 2, List.of(6, 5), 3, List.of(5, 3), 4, List.of( 4, 4 ), 5, List.of( 3, 3 ));
         verify(dealer, times(6)).pullCards(2);
         assertTrue(result.equals(correctR), "Ties should be broken wit the pair with the highest individual card going first");
     }
    
     @Test // This test checks for ties in dealer payouts
     void testDealerTies2() {
         doReturn(new ArrayList<Integer>(Arrays.asList(6, 4, 10)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(6, 5, 10)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(6, 4, 10)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(7, 3, 10)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(8, 4, 12)))
                 .doReturn(new ArrayList<Integer>(Arrays.asList(10, 3, 13)))
                 .when(dealer).pullCards(2);
         result = dealer.dealToVegas(12, 2);
         correctR = Map.of(0, List.of(10, 3), 1, List.of(8, 4 ), 2, List.of(7, 3), 3, List.of(6, 4), 4, List.of( 6, 5 ), 5, List.of( 6, 4 ));
         verify(dealer, times(6)).pullCards(2);
         assertTrue(result.equals(correctR), "Ties should be broken wit the pair with the highest individual card going first");
     }
 }
