package com.shoonie.ffsolitaire;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import	android.widget.Toast;
import android.widget.Button;

public class Play4By12 extends PlayGame {
        static int system_width = 0;
        static int system_height = 0;
		private int START_X_1_4BY12 = 10;
		private int START_Y_1_4BY12	= 10;
		private int START_X_2_4BY12	= 10;
		private int START_Y_2_4BY12 = 800;
		private int INTERVAL_COLUMNTOCOLUMN_4BY12	= 230;
		private int INTERVAL_YTOY_4BY12	 = 60;
		private int HIDDEN_STARTX_4BY12	 = 10;
		private int HIDDEN_STARTY_4BY12	 = 1600;
		private int CARD_CX =	200;
		private int CARD_CY=	310;
		
		private int m_nStatus; // 0 normal 1 finish can't win 2 win the game.
		private Context m_Context	=	null;
		private Deck aDeck;
		private boolean m_bAnyCardSelected;
		private int m_nSelectedColumn;
		FlowerCard 	m_SelectedCard;
		CardColumn[] m_aMainColumn	=	new CardColumn[12];
		CardColumn m_HiddenColumn;
		
		public Play4By12(Context context, int nWidth, int nHeight,Deck deck) {
            m_Context = context;
            system_width = nWidth;
            system_height = nHeight;

			CalcDimension();

			aDeck = deck;
			for(int i=0;i<12;i++){
				m_aMainColumn[i] 	= 	new CardColumn(m_Context); 
			}
			m_HiddenColumn		=	new CardColumn(m_Context);
			m_nStatus			=	0;
			m_nSelectedColumn	=	-1;
			m_SelectedCard		=	null;
			m_bAnyCardSelected	=	false;
		}
		
		
		public FlowerCard getCard(int index){
			return aDeck.GetAt(index);
		}
		
		public boolean CheckDbClick(Point pt) {
			if(m_HiddenColumn.GetSize()>1 && m_HiddenColumn.GetLastRect().contains(pt.x,pt.y))
			{
				for(int i=0;i<12;i++)
				{
					m_aMainColumn[i%12].PushCard(m_HiddenColumn.PopLastCard());
				}
				m_bAnyCardSelected	=	false;
				return true;
			}
			else
			{
				m_bAnyCardSelected	=	false;
				return true;
			}

		}

		public void DrawAll(Canvas canvas) {
			int i,j;
			Bitmap bitmap2Draw;
		///////////////////////////////////////////////////////////////
		//draw main column....
			for( j=0;j<6;j++)	//main column....
			{
				if(m_aMainColumn[j].GetSize() == 0)
				{
					bitmap2Draw	=	m_aMainColumn[j].GetSelectedBitmap();
					Rect drawRect = new Rect(START_X_1_4BY12 + j*INTERVAL_COLUMNTOCOLUMN_4BY12,	START_Y_1_4BY12 , START_X_1_4BY12 + j*INTERVAL_COLUMNTOCOLUMN_4BY12+CARD_CX,START_Y_1_4BY12+CARD_CY);
					canvas.drawBitmap(bitmap2Draw,null, drawRect, null);
				}
				else
				{
					for ( i=0;i<m_aMainColumn[j].GetSize();i++)
					{
						if(i== 0  && m_aMainColumn[j].HasHidden() )
						{
							bitmap2Draw	=	m_aMainColumn[j].GetHiddenBitmap();
						}
						else
							bitmap2Draw	=	m_aMainColumn[j].GetBitmapOfCard(i);
						Rect drawRect = new Rect(START_X_1_4BY12 + j*INTERVAL_COLUMNTOCOLUMN_4BY12, START_Y_1_4BY12 + INTERVAL_YTOY_4BY12 *i , START_X_1_4BY12 + j*INTERVAL_COLUMNTOCOLUMN_4BY12 +CARD_CX, START_Y_1_4BY12 + INTERVAL_YTOY_4BY12 *i + CARD_CY);
						canvas.drawBitmap(bitmap2Draw, null, drawRect, null);
					}
				}
			}
			
			for( j=6;j<12;j++)	//main column....
			{
				if(m_aMainColumn[j].GetSize() == 0)
				{
					bitmap2Draw	=	m_aMainColumn[j].GetSelectedBitmap();
					Rect drawRect = new Rect(START_X_2_4BY12 + (j-6)*INTERVAL_COLUMNTOCOLUMN_4BY12, START_Y_2_4BY12 , START_X_2_4BY12 + (j-6)*INTERVAL_COLUMNTOCOLUMN_4BY12 + CARD_CX, START_Y_2_4BY12 +CARD_CY);
					canvas.drawBitmap(bitmap2Draw,null,drawRect , null);
				}
				else
				{
					for ( i=0;i<m_aMainColumn[j].GetSize();i++)
					{
						if(i== 0  && m_aMainColumn[j].HasHidden() )
						{
							bitmap2Draw	=	m_aMainColumn[j].GetHiddenBitmap();
						}
						else
							bitmap2Draw	=	m_aMainColumn[j].GetBitmapOfCard(i);
						Rect drawRect = new Rect(START_X_2_4BY12 + (j-6)*INTERVAL_COLUMNTOCOLUMN_4BY12, 	START_Y_2_4BY12 + INTERVAL_YTOY_4BY12 *i  , START_X_2_4BY12 + (j-6)*INTERVAL_COLUMNTOCOLUMN_4BY12 + CARD_CX, 	START_Y_2_4BY12 + INTERVAL_YTOY_4BY12 *i  +CARD_CY);
						canvas.drawBitmap(bitmap2Draw, null, drawRect, null);
					}
				}
			}

		///////////////////////////////////////////////////////////////////
//			Draw	Hidden Set.
			if(m_HiddenColumn.GetSize() > 0)
			{
				bitmap2Draw	=	m_HiddenColumn.GetHiddenBitmap();
                Rect drawRect = new Rect(HIDDEN_STARTX_4BY12, 	HIDDEN_STARTY_4BY12 , HIDDEN_STARTX_4BY12 + CARD_CX, 	HIDDEN_STARTY_4BY12  +CARD_CY);
				canvas.drawBitmap(bitmap2Draw,null, drawRect , null);
			}
		//////////////////////////////////////////////////////////
			if(m_bAnyCardSelected)
			{
				Paint aPaint = new Paint();
				aPaint.setAlpha(192);
				Bitmap bufBitmap	=	 m_HiddenColumn.GetSelectedBitmap();
				if( m_nSelectedColumn < 6)
				{
                    Rect drawRect = new Rect(START_X_1_4BY12 +  INTERVAL_COLUMNTOCOLUMN_4BY12 * m_nSelectedColumn,START_Y_1_4BY12 + (m_aMainColumn[m_nSelectedColumn].GetSize() -1)*INTERVAL_YTOY_4BY12 , START_X_1_4BY12 +  INTERVAL_COLUMNTOCOLUMN_4BY12 * m_nSelectedColumn +CARD_CX,START_Y_1_4BY12 + (m_aMainColumn[m_nSelectedColumn].GetSize() -1)*INTERVAL_YTOY_4BY12 +CARD_CY);
					canvas.drawBitmap(bufBitmap,null,drawRect, aPaint);
				}
				else 
				{
                    Rect drawRect = new Rect(START_X_2_4BY12 +  INTERVAL_COLUMNTOCOLUMN_4BY12 * (m_nSelectedColumn-6),START_Y_2_4BY12 + (m_aMainColumn[m_nSelectedColumn].GetSize()-1) *INTERVAL_YTOY_4BY12 , START_X_2_4BY12 +  INTERVAL_COLUMNTOCOLUMN_4BY12 * (m_nSelectedColumn-6) +CARD_CX,START_Y_2_4BY12 + (m_aMainColumn[m_nSelectedColumn].GetSize()-1) *INTERVAL_YTOY_4BY12+CARD_CY);
					canvas.drawBitmap(bufBitmap,null, drawRect,	aPaint);
				}
			}
			if(m_nStatus ==1 )
			{
				Toast toast = Toast.makeText(m_Context,R.string.textCantwinTheGame, Toast.LENGTH_LONG);
				toast.show();
			}
			else if(m_nStatus == 2)
			{
				Toast toast = Toast.makeText(m_Context,R.string.textWonTheGame, Toast.LENGTH_LONG);
				toast.show();
			}
				
		}
		public int CheckPoint(Point pt, Rect rt1, Rect rt2) {
			int			nRedraw		=	0;
			int			i,j;
			boolean		bFoundSomething	=	false;
			if(!m_bAnyCardSelected)
			{
				for(i=0;i<12;i++)
				{
					if(m_aMainColumn[i].GetLastRect().contains(pt.x,pt.y) && m_aMainColumn[i].GetSize() > 0)
					{
						m_bAnyCardSelected	=	true;
						m_SelectedCard	=	m_aMainColumn[i].ShowLastCard();
						m_nSelectedColumn	=	i;
						SetMouseRegion();
						rt1.left	=	m_aMainColumn[i].GetLastRect().left;
						rt1.right	=	m_aMainColumn[i].GetLastRect().right;
						rt1.top		=	m_aMainColumn[i].GetLastRect().top;
						rt1.bottom	=	m_aMainColumn[i].GetLastRect().bottom;
						nRedraw	=	1;
						break;
					}
				}
			}
			else	//has selected card.
			{
				for( i=0;i<12;i++)
				{
					if(m_aMainColumn[i].GetLastRect().contains(pt.x,pt.y) )
					{
						bFoundSomething	=	true;
						if(i == m_nSelectedColumn)	//reselecte one column, unselect...
						{
							rt1.left	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().left;
							rt1.top		=	m_aMainColumn[m_nSelectedColumn].GetLastRect().top;
							rt1.right	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().right;
							rt1.bottom	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().bottom;
							m_bAnyCardSelected	=	false;
							nRedraw	=	1;
							break;
						}
						else
						{		/////////think over and over,,,,very important part////
							bFoundSomething			=	true;	
							int nSizeCurColumn		=	m_aMainColumn[i].GetSize();
							FlowerCard CurCard;
							int nCurNumber			=	0;
							int nCurPosition		=	0;
							if(nSizeCurColumn > 0)
							{
								CurCard			=	m_aMainColumn[i].ShowLastCard();
								nCurNumber		=	CurCard.GetNumber();
								nCurPosition	=	CurCard.GetPosition();
							}
							else
							{
								CurCard			=	null;
								nCurNumber		=	0;
								nCurPosition	=	0;
							}	

							if( nSizeCurColumn > 0 && m_SelectedCard.GetNumber()	!= nCurNumber )
							{
								m_bAnyCardSelected	=	false;
								bFoundSomething		=	false;
								nRedraw	=	1;
								break;
							}
							
							FlowerCard[] TempCard = new FlowerCard[4];	
							int		nDepthInColumn = 0;
						
							//check prev card top....
							{
								int		nColumnSize	=	m_aMainColumn[m_nSelectedColumn].GetSize();
								boolean	bHasHidden	=	m_aMainColumn[m_nSelectedColumn].HasHidden();
								int		nPositionCur,nPositionLast;
								for(j=0;j< nColumnSize;j++)
								{
									if(bHasHidden && j==nColumnSize-1)
										break;
									TempCard[nDepthInColumn]	= m_aMainColumn[m_nSelectedColumn].PopLastCard();
									if(m_aMainColumn[m_nSelectedColumn].GetSize() > 0)
									{
										if(m_aMainColumn[m_nSelectedColumn].GetSize() == 1 && bHasHidden)
											break;
										if(TempCard[nDepthInColumn].GetNumber() == m_aMainColumn[m_nSelectedColumn].ShowLastCard().GetNumber())
										{
											nPositionCur	=	TempCard[nDepthInColumn].GetPosition();
											nPositionLast	=	m_aMainColumn[m_nSelectedColumn].ShowLastCard().GetPosition();
											if(	( nPositionCur ==  nPositionLast+1 ) ||
												( nPositionCur+nPositionLast == 7 && TempCard[nDepthInColumn].GetNumber() != 12) ||
												( nPositionCur == 4 && nPositionLast == 2 && TempCard[nDepthInColumn].GetNumber() != 12) )
											{
												nDepthInColumn++;
												continue;
											}
											else
												break;
										}
										else
											break;
									}
									else
										break;
								}
							}

							if(nCurPosition +1 == TempCard[nDepthInColumn].GetPosition())
							{
								for( j= nDepthInColumn;j>=0;j--)
									m_aMainColumn[i].PushCard(TempCard[j]);//m_PlayingCard01.GetColumn(m_PlayingCard01.GetSelectedColumn())->PopCard());
								SetMouseRegion();
								if(m_aMainColumn[m_nSelectedColumn].GetSize() == 1)
								{
									m_aMainColumn[m_nSelectedColumn].SetHiddenOrShow(false);
								}
								m_bAnyCardSelected	=	false;
								if(m_nSelectedColumn < 6)
								{
									rt1.top		=	START_Y_1_4BY12;
									rt1.bottom	=	rt1.top	+ (m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12 + CARD_CY;
									rt1.left	=	START_X_1_4BY12 + m_nSelectedColumn * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	rt1.left+ CARD_CX;
								}
								else
								{
									rt1.top		=	START_Y_2_4BY12;
									rt1.bottom	=	rt1.top+ (m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt1.left	=	START_X_2_4BY12 + (m_nSelectedColumn-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	rt1.left+ CARD_CX;
								}
								if(i < 6)
								{
									rt2.top		=	START_Y_1_4BY12;
									rt2.bottom	=	rt2.top	+(m_aMainColumn[i].GetSize() + 1)* INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_1_4BY12 + i * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+ CARD_CX;
								}
								else
								{
									rt2.top		=	START_Y_2_4BY12;
									rt2.bottom	=	rt2.top+(m_aMainColumn[i].GetSize() +1) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_2_4BY12 + (i-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+CARD_CX;
								}
								nRedraw	=	2;
							}
							else if(nCurPosition+TempCard[nDepthInColumn].GetPosition() == 7 
									&& m_SelectedCard.GetNumber() !=12)
							{
								for( j= nDepthInColumn;j>=0;j--)
									m_aMainColumn[i].PushCard(TempCard[j]);//m_PlayingCard01.GetColumn(m_PlayingCard01.GetSelectedColumn())->PopCard());
								SetMouseRegion();
								if(m_aMainColumn[m_nSelectedColumn].GetSize() == 1)
								{
									m_aMainColumn[m_nSelectedColumn].SetHiddenOrShow(false);
								}
								m_bAnyCardSelected	=	false;
								if(m_nSelectedColumn < 6)
								{
									rt1.top		=	START_Y_1_4BY12;
									rt1.bottom	=	rt1.top+(m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12 + CARD_CY;
									rt1.left	=	START_X_1_4BY12 + m_nSelectedColumn * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	 rt1.left+CARD_CX;
								}
								else
								{
									rt1.top		=	START_Y_2_4BY12;
									rt1.bottom	=	rt1.top+ (m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt1.left	=	START_X_2_4BY12 + (m_nSelectedColumn-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	 rt1.left	+ CARD_CX;
								}
								if(i < 6)
								{
									rt2.top		=	START_Y_1_4BY12;
									rt2.bottom	=	rt2.top	+ (m_aMainColumn[i].GetSize() + 1)* INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_1_4BY12 + i * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left + CARD_CX;
								}
								else
								{
									rt2.top		=	START_Y_2_4BY12;
									rt2.bottom	=	rt2.top + (m_aMainColumn[i].GetSize() +1) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_2_4BY12+ (i-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left + CARD_CX;
								}
								nRedraw	=	2;
							}
							else if(nCurPosition == 2 && TempCard[nDepthInColumn].GetPosition() == 4 && m_SelectedCard.GetNumber() !=12)
							{
								for( j= nDepthInColumn;j>=0;j--)
									m_aMainColumn[i].PushCard(TempCard[j]);//m_PlayingCard01.GetColumn(m_PlayingCard01.GetSelectedColumn())->PopCard());
								SetMouseRegion();
								if(m_aMainColumn[m_nSelectedColumn].GetSize() == 1)
								{
									m_aMainColumn[m_nSelectedColumn].SetHiddenOrShow(false);
								}
								m_bAnyCardSelected	=	false;
								if(m_nSelectedColumn < 6)
								{
									rt1.top		=	START_Y_1_4BY12;
									rt1.bottom 	= 	rt1.top +	(m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12 + CARD_CY;
									rt1.left	=	START_X_1_4BY12 + m_nSelectedColumn * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	= 	rt1.left+CARD_CX;
								}
								else
								{
									rt1.top		=	START_Y_2_4BY12;
									rt1.bottom 	= 	rt1.top +	 (m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt1.left	=	START_X_2_4BY12 + (m_nSelectedColumn-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	rt1.left+CARD_CX;
								}
								if(i < 6)
								{
									rt2.top		=	START_Y_1_4BY12;
									rt2.bottom	=	rt2.top+(m_aMainColumn[i].GetSize() + 1)* INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_1_4BY12 + i * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+ CARD_CX;
								}
								else
								{
									rt2.top		=	START_Y_2_4BY12;
									rt2.bottom	=	rt2.top	+(m_aMainColumn[i].GetSize() +1) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_2_4BY12 + (i-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+CARD_CX;
								}
								nRedraw	=	2;
							}
							else if(TempCard[nDepthInColumn].GetPosition() == 1 && m_aMainColumn[i].GetSize() ==0)
							{
								for( j= nDepthInColumn;j>=0;j--)
									m_aMainColumn[i].PushCard(TempCard[j]);//m_PlayingCard01.GetColumn(m_PlayingCard01.GetSelectedColumn())->PopCard());
								SetMouseRegion();
								if(m_aMainColumn[m_nSelectedColumn].GetSize() == 1)
								{
									m_aMainColumn[m_nSelectedColumn].SetHiddenOrShow(false);
								}
								m_bAnyCardSelected	=	false;
								if(m_nSelectedColumn < 6)
								{
									rt1.top		=	START_Y_1_4BY12;
									rt1.bottom 	= rt1.top +	(m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12 + CARD_CY;
									rt1.left	=	START_X_1_4BY12 + m_nSelectedColumn * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	rt1.left + CARD_CX;
								}
								else
								{
									rt1.top		=	START_Y_2_4BY12;
									rt1.bottom 	= 	rt1.top +	(m_aMainColumn[m_nSelectedColumn].GetSize()+4) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt1.left	=	START_X_2_4BY12 + (m_nSelectedColumn-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt1.right	=	rt1.left+ CARD_CX;
								}
								if(i < 6)
								{
									rt2.top		=	START_Y_1_4BY12;
									rt2.bottom	=	rt2.top + (m_aMainColumn[i].GetSize() + 1)* INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_1_4BY12 + i * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+ CARD_CX;
								}
								else
								{
									rt2.top		=	START_Y_2_4BY12;
									rt2.bottom	=	rt2.top+(m_aMainColumn[i].GetSize() +1) * INTERVAL_YTOY_4BY12+ CARD_CY;
									rt2.left	=	START_X_2_4BY12 + (i-6) * INTERVAL_COLUMNTOCOLUMN_4BY12;
									rt2.right	=	rt2.left+CARD_CX;
								}
								nRedraw	=	2;
							}
							else	// nothing happen....
							{
								for( j=nDepthInColumn;j>= 0;j--)
									m_aMainColumn[m_nSelectedColumn].PushCard(TempCard[j]);
								
								rt1.left	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().left;
								rt1.top		=	m_aMainColumn[m_nSelectedColumn].GetLastRect().top;
								rt1.right	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().right;
								rt1.bottom 	= 	m_aMainColumn[m_nSelectedColumn].GetLastRect().bottom;
								m_bAnyCardSelected	=	false;
								nRedraw	=	1;
							}
						}
						break;
					}//if
				}//for
				if(bFoundSomething == false)
				{
					rt1.left	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().left;
					rt1.top		=	m_aMainColumn[m_nSelectedColumn].GetLastRect().top;
					rt1.right	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().right;
					rt1.bottom	=	m_aMainColumn[m_nSelectedColumn].GetLastRect().bottom;
					m_bAnyCardSelected	=	false;
					nRedraw	=	1;
				}
			}//if
			//check Finish...
			int	nFinish	=	1;
			for(i=0;i<12;i++)
			{
				if(m_aMainColumn[i].GetSize() != 4)
				{
					nFinish	=	0;
					break;
				}
				if(m_aMainColumn[i].HasHidden() == true)
				{
					nFinish	=	0;
					break;
				}
			}
			if(nFinish > 0)
			{
				for(i=0;i<12;i++)
				{
					FlowerCard	 card;
					card	=	m_aMainColumn[i].ShowFirstCard();
					if(card.GetPosition() != 1)
					{
						nFinish	=	-1;
						break;
					}
					card	=	m_aMainColumn[i].ShowSecondCard();
					if(card.GetPosition() != 2)
					{
						nFinish	=	-1;
						break;
					}
					if(card.GetNumber() == 12)
					{
						card	=	m_aMainColumn[i].ShowThirdCard();
						if(card.GetPosition() != 3)
							nFinish	=	-1;
						break;
					}
				}
			}
			if(nFinish != 0)
			{
				if(nFinish == 1)
					m_nStatus	=	2;	
				else if(nFinish == -1)
					m_nStatus	=	1;
				nRedraw = 3;
			}
			SetMouseRegion();
			return nRedraw;	
		}
		public boolean SetMouseRegion() {
			boolean	bRet	=	true;
			int i;

		//set main column rect
			for( i = 0;i<6;i++)
				m_aMainColumn[i].SetLastRect(START_X_1_4BY12+i*INTERVAL_COLUMNTOCOLUMN_4BY12,
									START_Y_1_4BY12 + INTERVAL_YTOY_4BY12*(m_aMainColumn[i].GetSize() -1),
									CARD_CX,
									CARD_CY);
			for( i = 6;i<12;i++)
				m_aMainColumn[i].SetLastRect(START_X_2_4BY12+(i-6)*INTERVAL_COLUMNTOCOLUMN_4BY12,
									START_Y_2_4BY12 + INTERVAL_YTOY_4BY12*(m_aMainColumn[i].GetSize()-1),
									CARD_CX,
									CARD_CY);
			m_HiddenColumn.SetLastRect(HIDDEN_STARTX_4BY12,HIDDEN_STARTY_4BY12,CARD_CX,CARD_CY);
			return bRet;
		}


		public boolean ShuffleAndInit() {

			int i;
			for(i=0;i<12;i++)
			{
				m_aMainColumn[i].CleanCard();
			}
			m_HiddenColumn.CleanCard();
			aDeck.Shuffle();

			for(i=0;i<24;i++)
				m_aMainColumn[i%12].PushCard(getCard(i));

			for(i=24;i<48;i++)
				m_HiddenColumn.PushCard(getCard(i));
			return true;
		}
		private void CalcDimension() {
			CARD_CX = system_width * 200 / 1440;
			CARD_CY = CARD_CX * 310 / 200;

			START_X_1_4BY12 = system_width * 10 / 1440;
			START_Y_1_4BY12 = system_height * 10 / 2880;
			START_X_2_4BY12 = START_X_1_4BY12;
			START_Y_2_4BY12 = system_height * 900 / 2880;

			INTERVAL_COLUMNTOCOLUMN_4BY12	= CARD_CX * 230/200;

			INTERVAL_YTOY_4BY12	 = CARD_CY * 60/310;
			HIDDEN_STARTX_4BY12	 = system_width * 10 / 1440;
			HIDDEN_STARTY_4BY12	 = system_height*1800 / 2880;

	/*
		private int START_X_1_4BY12 = 10;
		private int START_Y_1_4BY12	= 10;
		private int START_X_2_4BY12	= 10;
		private int START_Y_2_4BY12 = 800;
		private int INTERVAL_COLUMNTOCOLUMN_4BY12	= 230;
		private int INTERVAL_YTOY_4BY12	 = 60;
		private int HIDDEN_STARTX_4BY12	 = 10;
		private int HIDDEN_STARTY_4BY12	 = 1600;
		private int CARD_CX =	200;
		private int CARD_CY=	310;
			this is for 1440*2880
	*/
		}
	}


