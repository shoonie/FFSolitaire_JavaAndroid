package com.shoonie.ffsolitaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import	android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    Button endButton;
    Button newButton;
    Button changeButton;
    Button autoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView)findViewById(R.id.gameDisplayView);

        endButton 	= (Button) findViewById(R.id.endButton);
        newButton	= (Button) findViewById(R.id.newButton);
        changeButton= (Button) findViewById(R.id.changeButton);
        autoButton = (Button) findViewById(R.id.autoButton);

        autoButton.setEnabled(false);
        autoButton.setVisibility(View.INVISIBLE);

        endButton.setOnClickListener(new ClickButtonEventHandler());
        newButton.setOnClickListener(new ClickButtonEventHandler());
        changeButton.setOnClickListener(new ClickButtonEventHandler());
        autoButton.setOnClickListener(new ClickButtonEventHandler());
    }

    public static String REFRESH_ACTIVITY = "com.shoonie.action.REFRESH_UI";
    public static String AutoComplete_ACTIVITY = "com.shoonie.action.AutoComplete";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == REFRESH_ACTIVITY) {
                autoButton.setEnabled(true);
                autoButton.setVisibility(View.VISIBLE);
            }
            else if(intent.getAction() == AutoComplete_ACTIVITY) {
                invalidateGameView();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        autoButton.setEnabled(false);
        autoButton.setVisibility(View.INVISIBLE);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(REFRESH_ACTIVITY);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(AutoComplete_ACTIVITY);
        this.registerReceiver(broadcastReceiver, filter1);
        this.registerReceiver(broadcastReceiver, filter2);
    }

    @Override
    public void onPause() {
        super.onPause();

        this.unregisterReceiver(broadcastReceiver);
    }

    public void invalidateGameView()
    {
        if(gameView!=null)
            gameView.InvalidateAllView();
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
            else if(v.equals(autoButton))
                gameView.autoComplete();
        }
    }
}