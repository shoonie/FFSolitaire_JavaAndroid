package com.shoonie.ffsolitaire;

import com.shoonie.ffsolitaire.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FlowerCard {
	private int nID			=	0;
	private int nNumber		=	0;
	private int nPosition	=	0;
	private Bitmap image4Card		=	null;
	private Bitmap hiddenImage		=	null;
	private Context	m_Context;
	public int		GetID(){return nID;}
	public int		GetNumber(){return nNumber;}
	public int		GetPosition(){return nPosition;}
	public Bitmap 	GetImage(){	return image4Card;}
	public Bitmap	GetHiddenImage(){return hiddenImage;}
	private boolean SetPositionAndNumber()
	{
		if(nID >= 0 && nID <48)
		{
			nNumber = nID/4 +1;
			nPosition	= nID%4 +1;
			image4Card = BitmapFactory.decodeResource(m_Context.getResources(), R.drawable.pic0101+nID);
			hiddenImage= BitmapFactory.decodeResource(m_Context.getResources(), R.drawable.pic1301);
			return true;
		}
		else
			return false;
	}
	public FlowerCard(int nNo,Context context)
	{
		nID = nNo;
		m_Context	=	context;
		if(!SetPositionAndNumber())
		{
			nNumber		=	0;
			nPosition	=	0;
		}
	}
	boolean SetID(int nNumber)
	{
		nID	= nNumber;
		if(SetPositionAndNumber())
			return true;
		else
		{
			nNumber	=	0;
			nPosition	=	0;
			return false;
		}
	}
}
