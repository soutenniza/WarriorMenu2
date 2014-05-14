package com.warriormenu;

import java.util.Comparator;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by vannguyen on 5/12/14.
 */
public class DistComparator implements Comparator<Card> {
    @Override
    public int compare(Card first, Card second){
        Float firstDist = ((CustomCard) first).info.distance;
        Float secondDist = ((CustomCard) second).info.distance;
        return firstDist.compareTo(secondDist);
    }
}
