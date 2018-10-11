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


public class PlayFortune extends PlayGame {
    // this setting for 1440 2880
    static int system_width = 0;
    static int system_height = 0;
    static int START_X_FORTUNE = 80;
    static int START_Y_FORTUNE = 24;
    static int INTERVAL_COLUMNTOCOLUMN_FORTUNE = 350;
    static int INTERVAL_YTOY_FORTUNE = 60;
    static int HIDDEN_STARTX_FORTUNE = 10;
    static int HIDDEN_STARTY_FORTUNE = 800;
    static int BOARD_STARTX_FORTUNE = 250;
    static int BOARD_STARTY_FORTUNE = 800;
    static int BOARD_INTERVAL_FORTUNE = 45;
    static int CARD_CX = 230;//154;
    static int CARD_CY = 356;//239;
    static int CARD_CX_BOARD_LEFT = 10;

    // this is for index of column
    static final int MAIN_COLUMN1 = 0;
    static final int MAIN_COLUMN2 = 1;
    static final int MAIN_COLUMN3 = 2;
    static final int MAIN_COLUMN4 = 3;
    static final int BOARD_COLUMN_LEFT = 4;
    static final int BOARD_COLUMN_RIGHT = 5;
    static final int NULL_SELECTED = 6;
    ///
    private Context m_Context = null;
    private Deck aDeck;

    CardColumn[] mainColumn = new CardColumn[4];
    CardColumn[] solvedColumn = new CardColumn[4];
    CardColumn aHiddenColumn;
    CardColumn aBoardColumn;

    boolean anyCardSelected;
    int selectedColumn;
    FlowerCard selectedCard;
    int nCountSolve = 0;
    int nMatchCount;

    int nResultCount = 0;
    int[] nResultCards = new int[12];
    int nResultIndex = 0;
    Button aAutoButton;
    public int getResultCount() {
        return nResultCount;
    }

    public int[] getResultNumberArray() {
        return nResultCards;
    }

    public FlowerCard getCard(int index) {
        return aDeck.GetAt(index);
    }

    public PlayFortune(Context context, int nWidth, int nHeight, Deck deck) {
        m_Context = context;
        aDeck = deck;
        anyCardSelected = false;
        selectedColumn = -1;
        nCountSolve = 0;
        nMatchCount = 0;

        system_width = nWidth;
        system_height = nHeight;

        CalcDimension();

        for (int i = 0; i < 4; i++) {
            mainColumn[i] = new CardColumn(m_Context);
            solvedColumn[i] = new CardColumn(m_Context);
        }
        aHiddenColumn = new CardColumn(m_Context);
        aBoardColumn = new CardColumn(m_Context);
        nResultCount = 0;
        for (int i = 0; i < 12; i++)
            nResultCards[i] = 0;
    }

    private boolean PushSolveColumn(FlowerCard solveCard) {
        switch (nCountSolve % 8) {
            case 0:
                break;
            case 1:
                solvedColumn[0].PushCard(solveCard);
                break;
            case 2:
                break;
            case 3:
                solvedColumn[1].PushCard(solveCard);
                break;
            case 4:
                break;
            case 5:
                solvedColumn[2].PushCard(solveCard);
                break;
            case 6:
                break;
            case 7:
                solvedColumn[3].PushCard(solveCard);
                break;
        }

        nCountSolve++;

        return true;
    }

    private boolean FindNewSelectedCard(int nColumn, FlowerCard aCard) {
        if (aCard == null)
            return false;
        anyCardSelected = true;
        selectedColumn = nColumn;
        selectedCard = aCard;
        return true;
    }

    public int GetResult() {
        int nCountforMatchItem = 0;
        int i, j, k;
        for (i = 0; i < 4; i++) {
            int[] nSolvedNumber = new int[6];
            for (j = 0; j < 6; j++)
                nSolvedNumber[j] = solvedColumn[i].PopFirstCard().GetNumber();
            for (j = 0; j < 6; j++) {
                for (k = j + 1; k < 6; k++) {
                    if (nSolvedNumber[j] == nSolvedNumber[k]) {
                        nResultCards[nCountforMatchItem] = nSolvedNumber[j];
                        nCountforMatchItem++;
                        break;
                    }
                }
            }
        }
////////////////////////		
        System.out.println("count = " + nCountforMatchItem);
        for (i = 0; i < nCountforMatchItem; i++) {
            System.out.println("number = " + nResultCards[i]);
        }
///////////////		
        return nCountforMatchItem;
    }

    public boolean CheckDbClick(Point pt) {
        boolean bRedraw = false;
        Rect rtBoardColumn;
        rtBoardColumn = aBoardColumn.GetFirstRect();
        rtBoardColumn.right = aBoardColumn.GetLastRect().right;
        int nCount = aBoardColumn.GetSize();
        if (nCount > 2 &&
                aHiddenColumn.GetSize() == 0) {
            for (int i = 0; i < nCount; i++) {
                aHiddenColumn.PushCard(aBoardColumn.PopLastCard());
            }
            bRedraw = true;
        }
        if (bRedraw)
            SetMouseRegion();
        return bRedraw;
    }


    public int CheckPoint(Point pt, Rect rt1, Rect rt2) {
        int nRedraw = 0;
        boolean bSelected = false;
        boolean bFinish = false;

        rt1.top = 0;
        rt2.top = 0;

        for (int i = 0; i < 6; i++)    //0~3 main column, 4 first card of board 5 last card of board
        {
            //if(GetColumn(NAME_BOARD_COLUMN).GetSize() == 1 && i== 5)
            //	break;
            if ((i < 4 && mainColumn[i].GetLastRect().contains(pt.x, pt.y))
                    || (i == 4 && aBoardColumn.GetFirstRect().contains(pt.x, pt.y))
                    || (i == 5 && aBoardColumn.GetLastRect().contains(pt.x, pt.y))) {
                if (!anyCardSelected) {
                    if (i < 4) {
                        bSelected = FindNewSelectedCard(i, mainColumn[i].ShowLastCard());
                        rt1.top = mainColumn[i].GetSize() * INTERVAL_YTOY_FORTUNE + 60;
                        rt1.left = START_X_FORTUNE + i * INTERVAL_COLUMNTOCOLUMN_FORTUNE + 80;
                        rt1.right = rt1.left + CARD_CX;
                        rt1.bottom = rt1.top + CARD_CY;


                    } else if (i == 4) {
                        bSelected = FindNewSelectedCard(i, aBoardColumn.ShowFirstCard());
                        rt1.top = BOARD_STARTY_FORTUNE;
                        rt1.left = BOARD_STARTX_FORTUNE;
                        rt1.bottom = rt1.top + CARD_CY;
                        rt1.right = rt1.left + CARD_CX;
                    } else if (i == 5) {
                        bSelected = FindNewSelectedCard(i, aBoardColumn.ShowLastCard());
                        rt1.top = BOARD_STARTY_FORTUNE;
                        rt1.left = BOARD_STARTX_FORTUNE + (aBoardColumn.GetSize() - 1) * BOARD_INTERVAL_FORTUNE;
                        rt1.bottom = rt1.top + CARD_CY;
                        rt1.right = rt1.left + CARD_CX;
                    }

                    nRedraw = 1;

                } else {
                    int nNewSelectedColumnNumber = i;
                    if (nNewSelectedColumnNumber != selectedColumn) {
                        //compare with it ...
                        int nOldSelectedNumber = selectedCard.GetNumber();
                        int nOldSelectedPosition = selectedCard.GetPosition();
                        int nNewSelectedNumber = 0;
                        int nNewSelectedPosition = 0;
                        if (i < 4 && mainColumn[i].ShowLastCard() != null) {
                            nNewSelectedNumber = mainColumn[i].ShowLastCard().GetNumber();
                            nNewSelectedPosition = mainColumn[i].ShowLastCard().GetPosition();
                        } else if (i == 4 && aBoardColumn.ShowFirstCard() != null){
                            nNewSelectedNumber = aBoardColumn.ShowFirstCard().GetNumber();
                            nNewSelectedPosition = aBoardColumn.ShowFirstCard().GetPosition();
                        }
                        else if (i == 5 && aBoardColumn.ShowLastCard() != null) {
                            nNewSelectedNumber = aBoardColumn.ShowLastCard().GetNumber();
                            nNewSelectedPosition = aBoardColumn.ShowLastCard().GetPosition();
                        }
                        if (nOldSelectedNumber == nNewSelectedNumber && nOldSelectedPosition != nNewSelectedPosition) {
                            ////////
                            String s = String.format("%d - %d Good Job!!!\n",
                                    nOldSelectedNumber,
                                    nNewSelectedNumber);
                            System.out.println(s);
                            ////////
                            FlowerCard aTempCard = null;
                            //pop & push new
                            if (i < 4) {
                                rt1.top = START_Y_FORTUNE;
                                rt1.left = START_X_FORTUNE + i * INTERVAL_COLUMNTOCOLUMN_FORTUNE;
                                rt1.bottom = rt1.top + 5 * INTERVAL_YTOY_FORTUNE + CARD_CY;
                                rt1.right = rt1.left + CARD_CX;
                                aTempCard = mainColumn[i].PopLastCard();
                            } else if (i == 4) {
                                rt1.top = BOARD_STARTY_FORTUNE;
                                rt1.left = BOARD_STARTX_FORTUNE;
                                rt1.bottom = rt1.top + CARD_CY;
                                rt1.right = rt1.left + (aBoardColumn.GetSize() + 1) * BOARD_INTERVAL_FORTUNE + CARD_CY;

                                aTempCard = aBoardColumn.PopFirstCard();
                            } else if (i == 5) {
                                rt1.top = BOARD_STARTY_FORTUNE;
                                rt1.left = BOARD_STARTX_FORTUNE;
                                rt1.bottom = rt1.top + CARD_CY;
                                rt1.right = rt1.left + (aBoardColumn.GetSize() + 1) * BOARD_INTERVAL_FORTUNE + CARD_CY;

                                aTempCard = aBoardColumn.PopLastCard();
                            }
                            if (aTempCard != null)
                                PushSolveColumn(aTempCard);
                            //pop & push old

                            if (selectedColumn < 4) {
                                rt2.top = START_Y_FORTUNE;
                                rt2.bottom = rt2.top + 5 * INTERVAL_YTOY_FORTUNE + CARD_CY;
                                rt2.left = START_X_FORTUNE + selectedColumn * INTERVAL_COLUMNTOCOLUMN_FORTUNE;
                                rt2.right = rt2.left + CARD_CX;

                                aTempCard = mainColumn[selectedColumn].PopLastCard();//PopMainColumn(nOldSelectedColumnNumber);
                            } else if (selectedColumn == 4) {
                                rt2.top = BOARD_STARTY_FORTUNE;
                                rt2.left = BOARD_STARTX_FORTUNE;
                                rt2.bottom = rt2.top + CARD_CY;
                                rt2.right = rt2.left + (aBoardColumn.GetSize()) * BOARD_INTERVAL_FORTUNE + CARD_CY;

                                aTempCard = aBoardColumn.PopFirstCard();
                            } else if (selectedColumn == 5) {
                                rt2.top = BOARD_STARTY_FORTUNE;
                                rt2.left = BOARD_STARTX_FORTUNE;
                                rt2.bottom = rt2.top + CARD_CY;
                                rt2.right = rt2.left + (aBoardColumn.GetSize()) * BOARD_INTERVAL_FORTUNE + CARD_CY;

                                aTempCard = aBoardColumn.PopLastCard();
                            }
                            if (aTempCard != null)
                                PushSolveColumn(aTempCard);

                            nRedraw = 2;
                            nMatchCount++;
                            if (nMatchCount == 24) {
                                nMatchCount = 0;
                                ;
                                bFinish = true;
                            }
                            anyCardSelected = false;
                        } else {
                            String s = String.format("%d - %d Not pare!!!",
                                    nOldSelectedNumber,
                                    nNewSelectedNumber);
                            System.out.println(s);
                            nRedraw = 1;
                        }
                    } else {
                        nRedraw = 1;
                    }
                }
            }
        }
        // board card last & before last....
        if (anyCardSelected &&
                aBoardColumn.GetLastBeforeRect().contains(pt.x, pt.y) &&
                selectedColumn == 5 &&
                aBoardColumn.GetSize() > 2
                ) {
            int nOldSelectedNumber = aBoardColumn.ShowLastCard().GetNumber();
            int nNewSelectedNumber = aBoardColumn.ShowLastBeforeCard().GetNumber();
            if (nOldSelectedNumber == nNewSelectedNumber) {
                ////////
                String s = String.format("%d - %d Not pare!!!",
                        nOldSelectedNumber,
                        nNewSelectedNumber);
                System.out.println(s);


                rt1.top = BOARD_STARTY_FORTUNE;
                rt1.left = HIDDEN_STARTX_FORTUNE;
                rt1.bottom = rt1.top + CARD_CY;
                rt1.right = rt1.left + BOARD_STARTX_FORTUNE + (aBoardColumn.GetSize()) * BOARD_INTERVAL_FORTUNE + CARD_CX;


                ////////
                FlowerCard aTempCard;
                //pop & push new
                aTempCard = aBoardColumn.PopLastCard();
                PushSolveColumn(aTempCard);
                //pop & push old
                aTempCard = aBoardColumn.PopLastCard();
                PushSolveColumn(aTempCard);

                anyCardSelected = false;
                nRedraw = 1;
                nMatchCount++;
                if (nMatchCount == 24) {
                    nMatchCount = 0;
                    bFinish = true;
                }
            }
        }

        ///////////////////
        if (anyCardSelected) {
            nRedraw = 1;

//			SELECTED_COLUMN SelectedColumn	=	GetSelectedColumn();
            if (selectedColumn < 4) {
                rt1.top = START_Y_FORTUNE + (mainColumn[selectedColumn].GetSize() - 1) * INTERVAL_YTOY_FORTUNE;
                rt1.left = START_X_FORTUNE + selectedColumn * INTERVAL_COLUMNTOCOLUMN_FORTUNE;
                rt1.bottom = rt1.top + CARD_CY;
                rt1.right = rt1.left + CARD_CX;

            } else if (selectedColumn == 4) {
                rt1.top = BOARD_STARTY_FORTUNE;
                rt1.left = BOARD_STARTX_FORTUNE;
                rt1.bottom = rt1.top + CARD_CY;
                rt1.right = rt1.left + CARD_CX_BOARD_LEFT;//CARD_CX;	// for first card of board is small size.
            } else if (selectedColumn == 5) {
                rt1.top = BOARD_STARTY_FORTUNE;
                rt1.left = BOARD_STARTX_FORTUNE + (aBoardColumn.GetSize() - 1) * BOARD_INTERVAL_FORTUNE;
                rt1.bottom = rt1.top + CARD_CY;
                rt1.right = rt1.left + CARD_CX;
            }
            anyCardSelected = bSelected;
//			SetHavingSelectedCard(bSelected);
        }
        if (aHiddenColumn.GetLastRect().contains(pt.x, pt.y)) {
            FlowerCard aTempCard;
            aTempCard = aHiddenColumn.PopLastCard();//PopHiddenColumn();
            if (aTempCard != null)
                aBoardColumn.PushCard(aTempCard);//PushBoardColumn(pTemp);
            rt1.top = BOARD_STARTY_FORTUNE;
            rt1.left = HIDDEN_STARTX_FORTUNE;
            rt1.bottom = rt1.top + CARD_CY;
            rt1.right = rt1.left + BOARD_STARTX_FORTUNE + (aBoardColumn.GetSize() + 1) * BOARD_INTERVAL_FORTUNE + CARD_CY;

            nRedraw = 1;
        }
        if (nRedraw == 1 || nRedraw == 2)
            SetMouseRegion();
        if (bFinish) {
            nRedraw = 3;
            System.out.println("Finish all!!");


            for (int i = 0; i < 12; i++)
                nResultCards[i] = 0;

            nResultCount = GetResult();
        }
        return nRedraw;
    }

    public String GetFortune(int nNumber) {
        String result = "";
        switch (nNumber) {
            case 1:
                result = "News,There would be some news Today.";
                break;
            case 2:
                result = "Lover,You would meet a lover.";
                break;
            case 3:
                result = "Picnic,You would go a picnic today.";
                break;
            case 4:
                result = "Annoying,Some annoying things would happen to you today, Be careful.";
                break;
            case 5:
                result = "Noodles,You would have some noodles.";
                break;
            case 6:
                result = "Joy,You would enjoy something ";
                break;
            case 7:
                result = "Fortune, You would get a unexpected fortune!";
                break;
            case 8:
                result = "Money,You would spend a big money, you would buy something you want!";
                break;
            case 9:
                result = "Alcohol,You would drink beer or something, Don't drink too much!";
                break;
            case 10:
                result = "Worry,Something to worry would happen. Don't Worry too much!";
                break;
            case 11:
                result = "Money!You would win a lotto or something, Send me a little~";
                break;
            case 12:
                result = "Guest,You would have some one who visit you.";
                break;
            default:
                result = "Nothing";
                break;
        }
        return result;

    }

    public void DrawAll(Canvas canvas) {
        int i, j;
        Bitmap bitmap;
        ///////////////////////////////////////////////////////////////
        //draw main column....
        for (j = 0; j < 4; j++)    //main column....
        {
            for (i = 0; i < mainColumn[j].GetSize(); i++) {

                if (i == mainColumn[j].GetSize() - 1)
                    bitmap = mainColumn[j].GetBitmapOfCard(i);
                else
                    bitmap = mainColumn[j].GetHiddenBitmap();
                /////////////////////////////
                Rect dstRect = new Rect(START_X_FORTUNE + j * INTERVAL_COLUMNTOCOLUMN_FORTUNE, START_Y_FORTUNE + i * INTERVAL_YTOY_FORTUNE, START_X_FORTUNE + j * INTERVAL_COLUMNTOCOLUMN_FORTUNE + CARD_CX, START_Y_FORTUNE + i * INTERVAL_YTOY_FORTUNE + CARD_CY);
                canvas.drawBitmap(bitmap, null, dstRect, null);
                //////////////////////////////////////////////////////////
            }
        }

        ///////////////////////////////////////////////////////////////////
        //		Draw	Hidden Set.
        if (aHiddenColumn.GetSize() != 0) {
            bitmap = aHiddenColumn.GetHiddenBitmap();
            Rect dstRect = new Rect(HIDDEN_STARTX_FORTUNE, HIDDEN_STARTY_FORTUNE, HIDDEN_STARTX_FORTUNE + CARD_CX, HIDDEN_STARTY_FORTUNE + CARD_CY);
            canvas.drawBitmap(bitmap, null, dstRect, null);
        }
        ///////////////////////////////////////////////////////////////////
        //		Draw	Board Set
        int nBoardSize = aBoardColumn.GetSize();

        for (i = 0; i < nBoardSize; i++) {
            bitmap = (aBoardColumn.GetBitmapOfCard(i));
            Rect dstRect;
            if( i>= 1) {
                if (i == nBoardSize - 1)
                    dstRect = new Rect(BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * i + CARD_CX, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * i + CARD_CX * 2, BOARD_STARTY_FORTUNE + CARD_CY);
                else
                    dstRect = new Rect(BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * i + CARD_CX/2, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * i + CARD_CX/2 + CARD_CX , BOARD_STARTY_FORTUNE + CARD_CY);
            }
            else // only one card is opened
                dstRect = new Rect(BOARD_STARTX_FORTUNE  , BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE +  CARD_CX , BOARD_STARTY_FORTUNE + CARD_CY);
            canvas.drawBitmap(bitmap, null, dstRect, null);
        }
        ///////////////////////////////////////////////////////////////
        if (anyCardSelected) {
            Bitmap bufBitmap = aHiddenColumn.GetSelectedBitmap();
            Paint aPaint = new Paint();
            aPaint.setAlpha(192);
            if (selectedColumn < 4) {
                Rect dstRect = new Rect(START_X_FORTUNE + INTERVAL_COLUMNTOCOLUMN_FORTUNE * selectedColumn, (mainColumn[selectedColumn].GetSize() - 1) * INTERVAL_YTOY_FORTUNE + START_Y_FORTUNE,
                        START_X_FORTUNE + INTERVAL_COLUMNTOCOLUMN_FORTUNE * selectedColumn + CARD_CX, (mainColumn[selectedColumn].GetSize() - 1) * INTERVAL_YTOY_FORTUNE + CARD_CY + START_Y_FORTUNE);
                canvas.drawBitmap(bufBitmap, null, dstRect, aPaint);
            }
            else if (selectedColumn == 4)
            {
                if (aBoardColumn.GetSize() >= 3) {
                    Rect dstRect = new Rect(BOARD_STARTX_FORTUNE, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE  + CARD_CX/2, BOARD_STARTY_FORTUNE + CARD_CY);
                    canvas.drawBitmap(bufBitmap, null, dstRect, aPaint);
                } else {
                    Rect dstRect = new Rect(BOARD_STARTX_FORTUNE, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + CARD_CX, BOARD_STARTY_FORTUNE + CARD_CY);
                    canvas.drawBitmap(bufBitmap, null, dstRect, aPaint);
                }
            } else if (selectedColumn == 5) {
                if (aBoardColumn.GetSize() == 1) {
                    Rect dstRect = new Rect(BOARD_STARTX_FORTUNE, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + CARD_CX, BOARD_STARTY_FORTUNE + CARD_CY);
                    canvas.drawBitmap(bufBitmap, null, dstRect, aPaint);
                }
                else {
                    Rect dstRect = new Rect(BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * (aBoardColumn.GetSize() - 1) + CARD_CX, BOARD_STARTY_FORTUNE, BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * (aBoardColumn.GetSize() - 1) + CARD_CX * 2, BOARD_STARTY_FORTUNE + CARD_CY);
                    canvas.drawBitmap(bufBitmap, null, dstRect, aPaint);
                }
            }

        }
    }

    public boolean SetMouseRegion() {
        boolean bRet = true;
        for (int i = 0; i < 4; i++)
            mainColumn[i].SetLastRect(START_X_FORTUNE + i * INTERVAL_COLUMNTOCOLUMN_FORTUNE,
                    INTERVAL_YTOY_FORTUNE * mainColumn[i].GetSize(),
                    CARD_CX,
                    CARD_CY);

        if (aHiddenColumn.GetSize() > 0)
            aHiddenColumn.SetLastRect(HIDDEN_STARTX_FORTUNE, HIDDEN_STARTY_FORTUNE, CARD_CX, CARD_CY);

        if (aBoardColumn.GetSize() == 1)
            aBoardColumn.SetLastRect(BOARD_STARTX_FORTUNE, BOARD_STARTY_FORTUNE, CARD_CX, CARD_CY);

        if (aBoardColumn.GetSize() > 1) {
            aBoardColumn.SetFirstRect(BOARD_STARTX_FORTUNE, BOARD_STARTY_FORTUNE, BOARD_INTERVAL_FORTUNE + CARD_CX/2, CARD_CY);
            aBoardColumn.SetLastRect(BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * (aBoardColumn.GetSize() - 1) + CARD_CX, BOARD_STARTY_FORTUNE, CARD_CX, CARD_CY);

            if (anyCardSelected && aBoardColumn.GetSize() >= 3 && (selectedCard == aBoardColumn.ShowLastCard()))
            {
                int xSize =  + BOARD_INTERVAL_FORTUNE * (aBoardColumn.GetSize() + 1) + CARD_CX /2;
                aBoardColumn.SetLastBeforeRect(BOARD_STARTX_FORTUNE + BOARD_INTERVAL_FORTUNE * (aBoardColumn.GetSize() - 2) + CARD_CX/2,   BOARD_STARTY_FORTUNE,  xSize, CARD_CY);
            }
        }
        return bRet;
    }

    public boolean ShuffleAndInit() {
        int i;

        for (i = 0; i < 4; i++) {
            mainColumn[i].CleanCard();
            solvedColumn[i].CleanCard();
        }
        aHiddenColumn.CleanCard();
        aBoardColumn.CleanCard();

        nMatchCount = 0;
        aDeck.Shuffle();

        for (i = 0; i < 20; i++)
            mainColumn[i % 4].PushCard(getCard(i));

        for (i = 20; i < 48; i++)
            aHiddenColumn.PushCard(getCard(i));

        return true;
    }
    private void CalcDimension()
    {
        CARD_CX = system_width * 230 / 1440;
        CARD_CY = CARD_CX *239 /154;

        START_X_FORTUNE = system_width * 80 / 1440;
        START_Y_FORTUNE = system_height * 24 / 2880;

        INTERVAL_COLUMNTOCOLUMN_FORTUNE = CARD_CX*350 / 230;
        INTERVAL_YTOY_FORTUNE = CARD_CY * 60 /356;

        HIDDEN_STARTX_FORTUNE = system_width * 10 / 1440;
        HIDDEN_STARTY_FORTUNE = system_height * 800 / 2880;

        BOARD_STARTX_FORTUNE = system_width * 250 / 1440;
        BOARD_STARTY_FORTUNE = HIDDEN_STARTY_FORTUNE;
        BOARD_INTERVAL_FORTUNE = CARD_CX * 25 /230;

        CARD_CX_BOARD_LEFT = CARD_CX *10 /230;

/*
        static int system_width = 0;
        static int system_height = 0;
        static int START_X_FORTUNE = 80;
        static int START_Y_FORTUNE = 24;
        static int INTERVAL_COLUMNTOCOLUMN_FORTUNE = 350;
        static int INTERVAL_YTOY_FORTUNE = 60;
        static int HIDDEN_STARTX_FORTUNE = 10;
        static int HIDDEN_STARTY_FORTUNE = 800;
        static int BOARD_STARTX_FORTUNE = 250;
        static int BOARD_STARTY_FORTUNE = 800;
        static int BOARD_INTERVAL_FORTUNE = 45;
        static int CARD_CX = 230;//154;
        static int CARD_CY = 356;//239;
        static int CARD_CX_BOARD_LEFT = 10;
        this is for 1440*2880
*/
    }
}
