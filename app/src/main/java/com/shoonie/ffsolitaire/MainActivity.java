package com.shoonie.ffsolitaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    Button endButton;
    Button newButton;
    Button changeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView)findViewById(R.id.gameDisplayView);
/*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameView.setSystemDisplayMetrics(displayMetrics.widthPixels,displayMetrics.heightPixels);
*/
        endButton 	= (Button) findViewById(R.id.endButton);
        newButton	= (Button) findViewById(R.id.newButton);
        changeButton= (Button) findViewById(R.id.changeButton);

        endButton.setOnClickListener(new ClickButtonEventHandler());
        newButton.setOnClickListener(new ClickButtonEventHandler());
        changeButton.setOnClickListener(new ClickButtonEventHandler());
    }
    public void invalidateGameView()
    {
        if(gameView!=null)
            gameView.postInvalidate();
    }
    public class ClickButtonEventHandler implements OnClickListener
    {
        public void onClick(View v) {
            if(v.equals(endButton))
                finish();
            else if(v.equals(newButton))
                gameView.newGame();
            else if(v.equals(changeButton))
                gameView.changeGame();
        }
    }
}