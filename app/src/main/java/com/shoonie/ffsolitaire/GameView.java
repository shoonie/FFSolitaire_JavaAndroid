package com.shoonie.ffsolitaire;

import android.app.Activity;
import android.view.View;
import android.content.Context;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class GameView extends View {
    Context m_Context4View;
    private PlayGame aGame;
    private int gameType;   			// 0 fortune, 1 4by12 , 2 8by6
    private int nSystemWidth = 0;
    private int nSystemHeight = 0;
    long 	nTime	=	0;
    Deck aDeck;

    public GameView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        m_Context4View = context;
        gameType =	0;
        GetSystemResolution();

        aGame = new PlayFortune(m_Context4View,nSystemWidth,nSystemHeight);
        aGame.ShuffleAndInit();
        aGame.SetMouseRegion();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(new TouchEventHandler());
        m_Context4View	=	context;
        gameType		=	0;

        GetSystemResolution();

        aGame = new PlayFortune(m_Context4View,nSystemWidth,nSystemHeight);
        aGame.ShuffleAndInit();
        aGame.SetMouseRegion();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(new TouchEventHandler());
        m_Context4View	=	context;
        gameType		=	0;

        GetSystemResolution();

        aGame = new PlayFortune(m_Context4View,nSystemWidth,nSystemHeight);
        aGame.ShuffleAndInit();
        aGame.SetMouseRegion();
    }

    public void GetSystemResolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) m_Context4View.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        nSystemWidth = displayMetrics.widthPixels;
        nSystemHeight = displayMetrics.heightPixels;
    }
    @Override protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.WHITE);
        aGame.DrawAll(canvas);
    }
    public void newGame()
    {
        switch(gameType)
        {
            case 0:
                aGame 	= 	new PlayFortune(m_Context4View,nSystemWidth,nSystemHeight);
                break;
            case 1:
                aGame 	= 	new Play4By12(m_Context4View,nSystemWidth,nSystemHeight);
                break;
            case 2:
                aGame 	= 	new Play6By8(m_Context4View,nSystemWidth,nSystemHeight);
                break;
        }
        aGame.ShuffleAndInit();
        aGame.SetMouseRegion();
        invalidate();
    }
    public void changeGame()
    {
        gameType ++;
        gameType %= 3;
        newGame();
    }

    public void  Process(int nProc)
    {
        switch (nProc)
        {
            case 0:
            {
                switch(gameType)
                {
                    case 0:
                        aGame 	= 	new PlayFortune(m_Context4View,nSystemWidth,nSystemHeight);
                        break;
                    case 1:
                        aGame 	= 	new Play4By12(m_Context4View,nSystemWidth,nSystemHeight);
                        break;
                    case 2:
                        aGame 	= 	new Play6By8(m_Context4View,nSystemWidth,nSystemHeight);
                        break;
                }
                aGame.ShuffleAndInit();
                aGame.SetMouseRegion();
                break;
            }
            case 1:
                gameType ++;
                gameType %= 3;
                Process(0);
                break;
            case 2:
                ((Activity)(m_Context4View)).finish();
                break;
        }
    }
    public class TouchEventHandler implements OnTouchListener
    {
        @Override
        public boolean onTouch (View v, MotionEvent event)
        {
            Point pt = new Point((int)(event.getX()) , (int)(event.getY()));
            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                Rect rt1 = new Rect();
                Rect rt2 = new Rect();

                int ret = aGame.CheckPoint(pt, rt1, rt2);
                if(ret == 1)
                {
                    invalidate(rt1);
                }
                else if(ret == 2)
                {
                    invalidate(rt1);
                    invalidate(rt2);
                }
                else if(ret == 3)
                {
                    invalidate();
                    if(gameType == 0)
                    {
                        int nCount = aGame.getResultCount();
                        int[] CardNoArray	=	new int[12];
                        CardNoArray = aGame.getResultNumberArray();
                        makeResultDialog(nCount,CardNoArray);
                    }
                    invalidate();
                }
                if(System.currentTimeMillis() - nTime		< 330)	// double click.
                {
                    if(aGame.CheckDbClick(pt))
                        invalidate();
                }
                nTime		=	System.currentTimeMillis();
            }
            return true;
        }

    }
    public void makeResultDialog(int nCount , int[]resultNo)
    {
        ResultDialog aDlg  = new ResultDialog(m_Context4View,nCount,resultNo);
        aDlg.setCancelable(true);
        aDlg.setTitle("Today's Fortune");
        aDlg.show();
    }
}
