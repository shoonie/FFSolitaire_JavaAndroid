package com.shoonie.ffsolitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;

public class Deck {
	private ArrayList<FlowerCard> CardDeck;
	public Deck(Context context){
		CardDeck = new ArrayList<FlowerCard>(48);
		for(int i=47;i>=0;i--)
		{
			FlowerCard aCard = new FlowerCard(i,context);
			CardDeck.add(aCard);
		}
	}
	public FlowerCard GetAt(int index){
		return CardDeck.get(index);
	}
	public Bitmap GetBitmapOfCard(int index)
	{
		return CardDeck.get(index).GetImage();
	}
	public void Shuffle(){
		Collections.shuffle(CardDeck, new Random());
	}
}
