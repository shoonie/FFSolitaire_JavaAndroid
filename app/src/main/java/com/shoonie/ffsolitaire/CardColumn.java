package com.shoonie.ffsolitaire;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class CardColumn {
	private int countOfCards = 0;
	private LinkedList< FlowerCard> cardColumn;
	private Bitmap selectedImage	=	null;
	private boolean hasHidden	=	true;
	private Rect rtBoudary; 
	private Rect rtLast;
	private Rect rtLastBefore;
	private Rect rtFirst;
	public boolean	HasHidden(){return hasHidden;}
	public void	SetHiddenOrShow(boolean bShow){hasHidden	=	bShow;}
	Context m_Context;
	public int GetSize()
	{
		return countOfCards;
	}
	public CardColumn(Context context)
	{
		m_Context	=	context;
		cardColumn	=	new LinkedList<FlowerCard>();
		rtBoudary	=	new Rect(); 
		rtLast		=	new Rect(); 
		rtLastBefore=	new Rect(); 
		rtFirst		=	new Rect(); 
	}
	public boolean PushCard(FlowerCard aCard){
		cardColumn.addLast(aCard);
		countOfCards++;
		return true;
	}
	public FlowerCard PopFirstCard()
	{
		FlowerCard aCard = null;
		if (countOfCards == 0)
			return aCard;
		aCard	=	cardColumn.getFirst();
		cardColumn.removeFirst();
		countOfCards	--;
		return aCard;
	}
	public FlowerCard PopLastCard(){
		FlowerCard aCard = null;
		if (countOfCards == 0)
			return aCard;

		aCard	=	cardColumn.getLast();
		cardColumn.removeLast();
		countOfCards	--;
		return aCard;
	}
	
	public FlowerCard ShowLastCard()
	{
		FlowerCard aCard = null;
		if (countOfCards == 0)
			return aCard;

		aCard	=	cardColumn.getLast();
//		cardColumn.removeLast();
//		countOfCards	--;
		return aCard;
	}
	
	public FlowerCard ShowFirstCard()
	{
		FlowerCard aCard = null;
		if (countOfCards == 0)
			return aCard;

		aCard	=	cardColumn.getFirst();
//		cardColumn.removeFirst();
//		countOfCards	--;
		return aCard;
	}
	public FlowerCard ShowSecondCard()
	{
		FlowerCard aCard = null;
		if (countOfCards < 2)
			return aCard;

		aCard	=	cardColumn.get(1);
		return aCard;
	}
	public FlowerCard ShowThirdCard()
	{
		FlowerCard aCard = null;
		if (countOfCards < 3)
			return aCard;

		aCard	=	cardColumn.get(2);
		return aCard;
	}
	
	public FlowerCard ShowLastBeforeCard()
	{
		FlowerCard aCard = null;
		if (countOfCards < 2)
			return aCard;
		
		aCard	=	cardColumn.get(countOfCards-2);
		return aCard;
	}

	public Bitmap GetSelectedBitmap()
	{
		if(selectedImage == null)
		{
			selectedImage	=	BitmapFactory.decodeResource(m_Context.getResources(), R.drawable.pic1302);
		}
		return selectedImage;
	}
	public Bitmap GetBitmapOfCard(int nIndex)
	{
		if (countOfCards == 0)
			return null;
		return cardColumn.get(nIndex).GetImage();
	}
	
	public Bitmap GetHiddenBitmap()
	{
		if (countOfCards == 0)
			return null;
		return  cardColumn.getFirst().GetHiddenImage();
	}
	public boolean CleanCard()
	{
		cardColumn.clear();
		countOfCards	=	0;
		hasHidden		=	true;
		return false;
	}
	
	
	public Rect GetBoundaryRect(){return rtBoudary;}
	public Rect GetLastRect(){return rtLast;}
	public Rect GetLastBeforeRect(){return rtLastBefore;}
	public Rect GetFirstRect(){return rtFirst;}

	boolean	SetBoundaryRect(int x,int y,int cx,int cy){
		rtBoudary.left	=	x;
		rtBoudary.top	=	y;
		rtBoudary.right	=	x+cx;
		rtBoudary.bottom=	y+cy;
		return true;
	}
	boolean	SetLastRect(int x,int y,int cx,int cy){
		rtLast.left		=	x;
		rtLast.top		=	y;
		rtLast.right	=	x+cx;
		rtLast.bottom	=	y+cy;
		return true;
	}
	boolean	SetLastBeforeRect(int x,int y,int cx,int cy){
		rtLastBefore.left		=	x;
		rtLastBefore.top		=	y;
		rtLastBefore.right		=	x+cx;
		rtLastBefore.bottom		=	y+cy;
		return true;
	}
	boolean	SetFirstRect(int x,int y,int cx,int cy){
		rtFirst.left	=	x;
		rtFirst.top		=	y;
		rtFirst.right	=	x+cx;
		rtFirst.bottom	=	y+cy;
		return true;
	}
	boolean CheckNumberOrder()
	{
		boolean retBool	=	true;
		if(cardColumn.size()	==	0)
			return retBool;
		for(int i=0;i<cardColumn.size()-1;i++)
		{
			if(cardColumn.get(i).GetNumber() !=  cardColumn.get(i+1).GetNumber()+1)
			{
				retBool = false;
				break;
			}
		}
		return retBool;
	}
}
