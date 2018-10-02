package com.shoonie.ffsolitaire;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Toast;
import android.content.Intent;


public class Play6By8 extends PlayGame {
    static int system_width = 0;
    static int system_height = 0;

    private int START_X_1_8BY6 = 500;
    private int START_Y_1_8BY6 = 60;
    private int START_X_2_8BY6 = 820;
    private int START_Y_2_8BY6 = 60;

    private int INTERVAL_XTOX_8BY6 = 40;
    private int INTERVAL_YTOY_8BY6 = 270;

    private int START_SOLVE_X_8BY6 = 660;
    private int START_SOLVE_Y_8BY6 = 60;

    private int CARD_CX = 154;
    private int CARD_CY = 239;

    CardColumn[] m_aMainColumn = new CardColumn[12]; //index 12 -> Hidden Set...

    Context m_Context;
    Deck aDeck;
    boolean m_bAnyCardSelected;
    boolean m_bShowInfo;
    int m_nSelectedColumn;
    FlowerCard m_pSelectedCard;
    int m_nCount;
    boolean m_bWinthegame;
    boolean m_bAutoComplete;


    public Play6By8(Context context, int nWidth, int nHeight) {
        m_Context = context;
        system_width = nWidth;
        system_height = nHeight;
        CalcDimension();
        aDeck = new Deck(m_Context);
        for (int i = 0; i < 12; i++) {
            m_aMainColumn[i] = new CardColumn(m_Context);
        }
        m_bAnyCardSelected = false;
        m_nSelectedColumn = 0;
        m_pSelectedCard = null;
        m_nCount = 0;
        m_bShowInfo = false;
        m_bWinthegame = false;
        m_bAutoComplete = false;
    }

    public boolean ShuffleAndInit() {
        int i;
        for (i = 0; i < 12; i++) {
            m_aMainColumn[i].CleanCard();
        }

        aDeck.Shuffle();
        for (i = 0; i < 48; i++)
            m_aMainColumn[i % 8].PushCard(getCard(i));

        m_bAnyCardSelected = false;
        return true;
    }

    public FlowerCard getCard(int index) {
        return aDeck.GetAt(index);
    }

    public boolean SetMouseRegion() {
        boolean bRet = true;
        int i;

        //set main column rect
        // m_aMainColumn[i].GetSize() == 0
        for (i = 0; i < 4; i++) {
            if (m_aMainColumn[i].GetSize() == 0) {
                m_aMainColumn[i].SetLastRect(START_X_1_8BY6,
                        START_Y_1_8BY6 + INTERVAL_YTOY_8BY6 * i,
                        CARD_CX,
                        CARD_CY);
            } else {
                m_aMainColumn[i].SetLastRect(START_X_1_8BY6 - (m_aMainColumn[i].GetSize() - 1) * INTERVAL_XTOX_8BY6,
                        START_Y_1_8BY6 + INTERVAL_YTOY_8BY6 * i,
                        CARD_CX,
                        CARD_CY);
            }
        }
        for (i = 4; i < 8; i++) {
            if (m_aMainColumn[i].GetSize() == 0) {
                m_aMainColumn[i].SetLastRect(START_X_2_8BY6,
                        START_Y_2_8BY6 + INTERVAL_YTOY_8BY6 * (i - 4),
                        CARD_CX,
                        CARD_CY);
            } else {
                m_aMainColumn[i].SetLastRect(START_X_2_8BY6 + (m_aMainColumn[i].GetSize() - 1) * INTERVAL_XTOX_8BY6,
                        START_Y_2_8BY6 + INTERVAL_YTOY_8BY6 * (i - 4),
                        CARD_CX,
                        CARD_CY);
            }
        }
        for (i = 8; i < 12; i++)
            m_aMainColumn[i].SetLastRect(START_SOLVE_X_8BY6,
                    START_SOLVE_Y_8BY6 + INTERVAL_YTOY_8BY6 * (i - 8),
                    CARD_CX,
                    CARD_CY);
        return bRet;
    }

    public int CheckPoint(Point pt, Rect rt1, Rect rt2) {
        int nRedraw = 0;
        int i;
        boolean bFoundSomething = false;
        if (!m_bAnyCardSelected)  //first select...
        {
            for (i = 0; i < 8; i++) {
                if (m_aMainColumn[i].GetLastRect().contains(pt.x, pt.y) && m_aMainColumn[i].GetSize() > 0) {
                    m_bAnyCardSelected = true;
                    m_pSelectedCard = m_aMainColumn[i].ShowLastCard();
                    m_nSelectedColumn = i;
                    SetMouseRegion();
                    rt1.left = m_aMainColumn[i].GetLastRect().left;
                    rt1.right = m_aMainColumn[i].GetLastRect().right;
                    rt1.top = m_aMainColumn[i].GetLastRect().top;
                    rt1.bottom = m_aMainColumn[i].GetLastRect().bottom;
                    nRedraw = 1;
                    break;
                }
            }
        } else    //has selected card.
        {
            for (i = 0; i < 12; i++) {
                if (m_aMainColumn[i].GetLastRect().contains(pt.x, pt.y)) {
                    bFoundSomething = true;
                    if (i == m_nSelectedColumn)    //reselecte one column, unselect...
                    {
                        rt1.left = m_aMainColumn[i].GetLastRect().left;
                        rt1.right = m_aMainColumn[i].GetLastRect().right;
                        rt1.top = m_aMainColumn[i].GetLastRect().top;
                        rt1.bottom = m_aMainColumn[i].GetLastRect().bottom;
                        nRedraw = 1;
                        break;
                    } else if (i < 8) {        /////////think over and over,,,,very important part////
                        int nSelectedColumn = m_nSelectedColumn;
                        FlowerCard pPrevCard = m_pSelectedCard;
                        int nSizeCurColumn = m_aMainColumn[i].GetSize();
                        if (nSizeCurColumn > 0) {
                            FlowerCard pCurCard = m_aMainColumn[i].ShowLastCard();
                            if (pPrevCard.GetNumber() + 1 == pCurCard.GetNumber()) {
                                m_aMainColumn[i].PushCard(m_aMainColumn[nSelectedColumn].PopLastCard());
                                if (nSelectedColumn < 4) {
                                    rt1.top = START_Y_1_8BY6 + nSelectedColumn * INTERVAL_YTOY_8BY6;
                                    rt1.bottom = rt1.top + CARD_CY;
                                    rt1.left = START_X_1_8BY6 - m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6;
                                    rt1.right = START_X_1_8BY6 + CARD_CX;
                                } else {
                                    rt1.top = START_Y_2_8BY6 + (nSelectedColumn - 4) * INTERVAL_YTOY_8BY6;
                                    rt1.bottom = rt1.top + CARD_CY;
                                    rt1.left = START_X_2_8BY6;
                                    rt1.right = START_X_2_8BY6 + m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6 + CARD_CX;
                                }
                                if (i < 4) {
                                    rt2.top = START_Y_1_8BY6 + i * INTERVAL_YTOY_8BY6;
                                    rt2.bottom = rt2.top + CARD_CY;
                                    rt2.left = START_X_1_8BY6 - (m_aMainColumn[i].GetSize() + 1) * INTERVAL_XTOX_8BY6;
                                    rt2.right = START_X_1_8BY6 + CARD_CX;
                                } else {
                                    rt2.top = START_Y_2_8BY6 + (i - 4) * INTERVAL_YTOY_8BY6;
                                    rt2.bottom = rt2.top + CARD_CY;
                                    rt2.left = START_X_2_8BY6;
                                    rt2.right = START_X_2_8BY6 + (m_aMainColumn[i].GetSize() + 1) * INTERVAL_XTOX_8BY6 + CARD_CX;
                                }
                                //	Move
                                nRedraw = 2;
                                break;
                            } else {
                                //Nothing To do here///
                                nRedraw = 1;
//								rt1	=	m_aMainColumn[nSelectedColumn].GetLastRect();
                                rt1.left = m_aMainColumn[nSelectedColumn].GetLastRect().left;
                                rt1.right = m_aMainColumn[nSelectedColumn].GetLastRect().right;
                                rt1.top = m_aMainColumn[nSelectedColumn].GetLastRect().top;
                                rt1.bottom = m_aMainColumn[nSelectedColumn].GetLastRect().bottom;
                                //memcpy(rtInvalidateRect	,&(	m_aMainColumn[nSelectedColumn].GetMainRect()) ,sizeof(CRect));
                            }
                        } else {
                            // Move to New Column
//							GetColumn(i)->PushCards(m_aMainColumn[nSelectedColumn].PopCard());
                            m_aMainColumn[i].PushCard(m_aMainColumn[nSelectedColumn].PopLastCard());
                            if (nSelectedColumn < 4) {
                                rt1.top = START_Y_1_8BY6 + nSelectedColumn * INTERVAL_YTOY_8BY6;
                                rt1.bottom = rt1.top + CARD_CY;
                                rt1.left = START_X_1_8BY6 - m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6;
                                rt1.right = START_X_1_8BY6 + CARD_CX;
                            } else {
                                rt1.top = START_Y_2_8BY6 + (nSelectedColumn - 4) * INTERVAL_YTOY_8BY6;
                                rt1.bottom = rt1.top + CARD_CY;
                                rt1.left = START_X_2_8BY6;
                                rt1.right = START_X_2_8BY6 + m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6 + CARD_CX;
                            }
                            if (i < 4) {
                                rt2.top = START_Y_1_8BY6 + i * INTERVAL_YTOY_8BY6;
                                rt2.bottom = rt2.top + CARD_CY;
                                rt2.left = START_X_1_8BY6 - (m_aMainColumn[i].GetSize() + 1) * INTERVAL_XTOX_8BY6;
                                rt2.right = START_X_1_8BY6 + CARD_CX;
                            } else {
                                rt2.top = START_Y_2_8BY6 + (i - 4) * INTERVAL_YTOY_8BY6;
                                rt2.bottom = rt2.top + CARD_CY;
                                rt2.left = START_X_2_8BY6;
                                rt2.right = START_X_2_8BY6 + (m_aMainColumn[i].GetSize() + 1) * INTERVAL_XTOX_8BY6 + CARD_CX;
                            }
                            nRedraw = 2;
                            break;

                        }
                    } else //8 <= i < 12
                    {
                        int nColumnSize = m_aMainColumn[i].GetSize();
                        int nSelectedColumn = m_nSelectedColumn;
                        int nCurNumber = 0;
                        if (nColumnSize == 0)
                            nCurNumber = 0;
                        else
                            nCurNumber = m_aMainColumn[i].ShowLastCard().GetNumber();
                        if (nCurNumber + 1 == m_pSelectedCard.GetNumber()) {// check position....
                            int nCurPosition = (i % 4) + 1;
                            if (nCurPosition == m_pSelectedCard.GetPosition() ||
                                    ((nCurPosition + m_pSelectedCard.GetPosition() == 7) && m_pSelectedCard.GetNumber() != 12)) {
                                m_aMainColumn[i].PushCard(m_aMainColumn[nSelectedColumn].PopLastCard());
                                if (nSelectedColumn < 4) {
                                    rt1.top = START_Y_1_8BY6 + nSelectedColumn * INTERVAL_YTOY_8BY6;
                                    rt1.bottom = rt1.top + CARD_CY;
                                    rt1.left = START_X_1_8BY6 - m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6;
                                    rt1.right = START_X_1_8BY6 + CARD_CX;
                                } else {
                                    rt1.top = START_Y_2_8BY6 + (nSelectedColumn - 4) * INTERVAL_YTOY_8BY6;
                                    rt1.bottom = rt1.top + CARD_CY;
                                    rt1.left = START_X_2_8BY6;
                                    rt1.right = START_X_2_8BY6 + m_aMainColumn[nSelectedColumn].GetSize() * INTERVAL_XTOX_8BY6 + CARD_CX;
                                }

                                rt2.top = START_Y_1_8BY6 + (i - 8) * INTERVAL_YTOY_8BY6;
                                rt2.bottom = rt2.top + CARD_CY;
                                rt2.left = START_SOLVE_X_8BY6;
                                rt2.right = START_SOLVE_X_8BY6 + CARD_CX;
                                nRedraw = 2;
                                break;
                            } else {
                                bFoundSomething = false;
                                break;
                            }

                        } else {
                            bFoundSomething = false;
                            break;
//							nRedraw	=	1;
//							memcpy(rtInvalidateRect	,&(	m_aMainColumn[nSelectedColumn].GetMainRect()) ,sizeof(CRect));
                        }

                    }
                } //if
            } //for
            m_bAnyCardSelected = false;
            if (!bFoundSomething) {
                int nSelectedColumn = m_nSelectedColumn;
                rt1.left = m_aMainColumn[nSelectedColumn].GetLastRect().left;
                rt1.right = m_aMainColumn[nSelectedColumn].GetLastRect().right;
                rt1.top = m_aMainColumn[nSelectedColumn].GetLastRect().top;
                rt1.bottom = m_aMainColumn[nSelectedColumn].GetLastRect().bottom;
                nRedraw = 1;
            }
        } //else - has seleced card...
        //check Finish...
        if (checkAutoComplete()) {
            m_bAutoComplete = true;
            m_bShowInfo = false;
            nRedraw = 3;
        }

        int nFinish = 0;

        for (i = 8; i < 12; i++) {
            if (m_aMainColumn[i].GetSize() == 12) {
                nFinish++;
            }
        }
        if (nFinish == 4) {
            m_bWinthegame = true;
            m_bShowInfo = false;
            nRedraw = 3;
        }
        SetMouseRegion();
        if (m_bAutoComplete == true) {
            Intent intent = new Intent();
            intent.setAction(MainActivity.REFRESH_ACTIVITY);

            m_Context.sendBroadcast(intent);
        }
        return nRedraw;
    }

    public void DrawAll(Canvas canvas) {
        int i, j;
        Bitmap aBitmap;
        ///////////////////////////////////////////////////////////////
        //draw main column....
        for (j = 0; j < 4; j++)    //main column....
        {
            if (m_aMainColumn[j].GetSize() == 0) {
                aBitmap = m_aMainColumn[j].GetSelectedBitmap();
                Rect drawRect = new Rect(START_X_1_8BY6, START_Y_1_8BY6 + j * INTERVAL_YTOY_8BY6, START_X_1_8BY6 + CARD_CX, START_Y_1_8BY6 + j * INTERVAL_YTOY_8BY6 + CARD_CY);
                canvas.drawBitmap(aBitmap, null, drawRect, null);
            } else {
                for (i = 0; i < m_aMainColumn[j].GetSize(); i++) {
                    aBitmap = m_aMainColumn[j].GetBitmapOfCard(i);
                    Rect drawRect = new Rect(START_X_1_8BY6 - i * INTERVAL_XTOX_8BY6, START_Y_1_8BY6 + INTERVAL_YTOY_8BY6 * j, START_X_1_8BY6 - i * INTERVAL_XTOX_8BY6 + CARD_CX, START_Y_1_8BY6 + INTERVAL_YTOY_8BY6 * j + CARD_CY);
                    canvas.drawBitmap(aBitmap, null, drawRect, null);
                }
            }
        }

        for (j = 4; j < 8; j++)    //main column....
        {
            if (m_aMainColumn[j].GetSize() == 0) {
                aBitmap = m_aMainColumn[j].GetSelectedBitmap();
                Rect drawRect = new Rect(START_X_2_8BY6, START_Y_2_8BY6 + (j - 4) * INTERVAL_YTOY_8BY6, START_X_2_8BY6 + CARD_CX, START_Y_2_8BY6 + (j - 4) * INTERVAL_YTOY_8BY6 + CARD_CY);
                canvas.drawBitmap(aBitmap, null, drawRect, null);
            } else {
                for (i = 0; i < m_aMainColumn[j].GetSize(); i++) {
                    aBitmap = m_aMainColumn[j].GetBitmapOfCard(i);
                    Rect drawRect = new Rect(START_X_2_8BY6 + i * INTERVAL_XTOX_8BY6, START_Y_2_8BY6 + INTERVAL_YTOY_8BY6 * (j - 4), START_X_2_8BY6 + i * INTERVAL_XTOX_8BY6 + CARD_CX, START_Y_2_8BY6 + INTERVAL_YTOY_8BY6 * (j - 4) + CARD_CY);
                    canvas.drawBitmap(aBitmap, null, drawRect, null);
                }
            }
        }
        for (j = 8; j < 12; j++)    //main column....
        {
            if (m_aMainColumn[j].GetSize() == 0) {
                aBitmap = m_aMainColumn[j].GetSelectedBitmap();
                Rect drawRect = new Rect(START_SOLVE_X_8BY6, START_Y_2_8BY6 + (j - 8) * INTERVAL_YTOY_8BY6, START_SOLVE_X_8BY6 + CARD_CX, START_Y_2_8BY6 + (j - 8) * INTERVAL_YTOY_8BY6 + CARD_CY);
                canvas.drawBitmap(aBitmap, null, drawRect, null);
            } else {
                for (i = 0; i < m_aMainColumn[j].GetSize(); i++) {
                    aBitmap = m_aMainColumn[j].GetBitmapOfCard(i);
                    Rect drawRect = new Rect(START_SOLVE_X_8BY6, START_Y_2_8BY6 + (j - 8) * INTERVAL_YTOY_8BY6, START_SOLVE_X_8BY6 + CARD_CX, START_Y_2_8BY6 + (j - 8) * INTERVAL_YTOY_8BY6 + CARD_CY);
                    canvas.drawBitmap(aBitmap, null, drawRect, null);
                }
            }
        }
        if (m_bAnyCardSelected) {
            int nSelectedColumn = m_nSelectedColumn;
            Paint aPaint = new Paint();
            aPaint.setAlpha(192);
            Bitmap bufBitmap = m_aMainColumn[nSelectedColumn].GetSelectedBitmap();
            if (nSelectedColumn < 4) {
                Rect drawRect = new Rect(START_X_1_8BY6 - INTERVAL_XTOX_8BY6 * (m_aMainColumn[nSelectedColumn].GetSize() - 1), START_Y_1_8BY6 + nSelectedColumn * INTERVAL_YTOY_8BY6, START_X_1_8BY6 - INTERVAL_XTOX_8BY6 * (m_aMainColumn[nSelectedColumn].GetSize() - 1) + CARD_CX, START_Y_1_8BY6 + nSelectedColumn * INTERVAL_YTOY_8BY6 + CARD_CY);
                canvas.drawBitmap(bufBitmap, null, drawRect, aPaint);
            } else {
                Rect drawRect = new Rect(START_X_2_8BY6 + INTERVAL_XTOX_8BY6 * (m_aMainColumn[nSelectedColumn].GetSize() - 1), START_Y_2_8BY6 + (nSelectedColumn - 4) * INTERVAL_YTOY_8BY6, START_X_2_8BY6 + INTERVAL_XTOX_8BY6 * (m_aMainColumn[nSelectedColumn].GetSize() - 1) + CARD_CX, START_Y_2_8BY6 + (nSelectedColumn - 4) * INTERVAL_YTOY_8BY6 + CARD_CY);
                canvas.drawBitmap(bufBitmap, null, drawRect, aPaint);
            }
        }

        if (m_bWinthegame == true) {
            Toast toast = Toast.makeText(m_Context,R.string.textWonTheGame, Toast.LENGTH_LONG);
            toast.show();
        }
        if (m_bShowInfo == true) {
//			Bitmap infoBitmap = BitmapFactory.decodeResource(m_Context.getResources(), R.drawable.info);
//			canvas.drawBitmap(infoBitmap, 10,260, null);
        }
    }

    public boolean CheckDbClick(Point pt) {
        if ((pt.y > 260 && pt.y < 360) && m_bAutoComplete == false && m_bWinthegame == false) {
            m_bShowInfo = !m_bShowInfo;
            return true;
        } else if ((pt.y > 260 && pt.y < 360) && m_bAutoComplete == true && m_bWinthegame == false) {
            autoComplete();
            m_bAutoComplete = false;
            m_bWinthegame = true;
            return false;
        }
        return false;
    }

    public boolean checkAutoComplete() {
        boolean checkRet = true;
        for (int i = 0; i < 8; i++) {
            if (m_aMainColumn[i].CheckNumberOrder() == false) {
                checkRet = false;
                break;
            }
        }
        for (int i = 8; i < 12; i++) {
            if (m_aMainColumn[i].GetSize() == 0) {
                checkRet = false;
                break;
            }
        }
        return checkRet;
    }

    public void autoComplete() {
        AutoCompleteThread aThread = new AutoCompleteThread();
        Thread myThread = new Thread(aThread);
        myThread.start();
    }

    public class AutoCompleteThread implements Runnable {
        public void run() {
            FlowerCard aCard;
            FlowerCard bCard = null;
            while (true) {
                for (int i = 8; i < 12; i++) {
                    aCard = m_aMainColumn[i].ShowLastCard();

                    for (int j = 0; j < 8; j++) {
                        if (m_aMainColumn[j].GetSize() == 0)
                            continue;
                        bCard = m_aMainColumn[j].ShowLastCard();
                        if (bCard.GetNumber() == aCard.GetNumber() + 1) {
                            if ((bCard.GetPosition() == 1 || bCard.GetPosition() == 2) &&
                                    bCard.GetPosition() == aCard.GetPosition()) {
                                m_aMainColumn[i].PushCard(m_aMainColumn[j].PopLastCard());
                                break;
                            }
                            if (bCard.GetNumber() != 12 &&
                                    (bCard.GetPosition() == 3 || bCard.GetPosition() == 4) &&
                                    (bCard.GetPosition() == aCard.GetPosition() || (bCard.GetPosition() + aCard.GetPosition() == 7))
                                    ) {
                                m_aMainColumn[i].PushCard(m_aMainColumn[j].PopLastCard());
                                break;
                            }
                            if (bCard.GetNumber() == 12) {
                                if (i == 10 && bCard.GetPosition() == 3) {
                                    m_aMainColumn[i].PushCard(m_aMainColumn[j].PopLastCard());
                                    break;
                                } else if (i == 11 && bCard.GetPosition() == 4) {
                                    m_aMainColumn[i].PushCard(m_aMainColumn[j].PopLastCard());
                                    break;
                                }
                            }
                        }
                    }

                    Intent intent = new Intent();
                    intent.setAction(MainActivity.AutoComplete_ACTIVITY);
                    m_Context.sendBroadcast(intent);
                    Sleep(50);
                }

                if (m_aMainColumn[8].GetSize() == 12 &&
                        m_aMainColumn[9].GetSize() == 12 &&
                        m_aMainColumn[10].GetSize() == 12 &&
                        m_aMainColumn[11].GetSize() == 12)
                    break;

            }
        }
    }

    public void Sleep(long sleepmili) {
        try {
            Thread.sleep(sleepmili);
        } catch (Exception ex) {

        }

    }
    private void CalcDimension() {
        CARD_CX = system_width * 154 / 1440;
        CARD_CY = CARD_CX * 239 / 154;

        START_X_1_8BY6 = system_width * 500/1440;
        START_Y_1_8BY6 = system_height * 60/2880;
        START_X_2_8BY6 = system_width * 820/1440;
        START_Y_2_8BY6 = START_Y_1_8BY6;

        INTERVAL_XTOX_8BY6 = CARD_CX * 40/154;
        INTERVAL_YTOY_8BY6 = CARD_CY * 270/239;

        START_SOLVE_X_8BY6 = system_width * 660/1440;
        START_SOLVE_Y_8BY6 = START_Y_1_8BY6;

	/*
        private int START_X_1_8BY6 = 500;
        private int START_Y_1_8BY6 = 60;
        private int START_X_2_8BY6 = 820;
        private int START_Y_2_8BY6 = 60;

        private int INTERVAL_XTOX_8BY6 = 40;
        private int INTERVAL_YTOY_8BY6 = 270;

        private int START_SOLVE_X_8BY6 = 660;
        private int START_SOLVE_Y_8BY6 = 60;

        private int CARD_CX = 154;
        private int CARD_CY = 239;
			this is for 1440*2880
	*/
    }
}
