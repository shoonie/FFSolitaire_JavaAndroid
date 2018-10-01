package com.shoonie.ffsolitaire;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog extends Dialog
{
	private Context	m_context4Dialog;
	private int   	nResultCount;
	private int[] 	resultNo;
	private int 	nCurrentCount;
	private int 	nButtonState;	// 0 all off 1 all on  2 next off 3 prev off
	ImageView cardView;
	TextView textView;
	Button PrevButton;
	Button NextButton;
	Button EndButton;
	public ResultDialog(Context context,int nCount , int[] paramResultNo){
		super(context);
		m_context4Dialog	=	context;
		nResultCount		=	nCount;
		resultNo			=	paramResultNo;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultdialoglayout);
		cardView = (ImageView) findViewById(R.id.cardView);
		textView = (TextView) findViewById(R.id.textResultAll);
		PrevButton = (Button) findViewById(R.id.prevbutton);
		NextButton = (Button) findViewById(R.id.nextbutton);
		EndButton = (Button) findViewById(R.id.finishbutton);
		nCurrentCount = 0;
		switch (nResultCount) {
			case 0:
			case 1:
				nButtonState = 0;
				break;
			case 2:
			default:
				nButtonState = 3;
				break;
		}

		if (nResultCount == 0) {
			cardView.setVisibility(View.INVISIBLE);
            textView.setText(R.string.fortune00);
		} else {
			DrawFortuneCard(resultNo[nCurrentCount]);
		}
		SetAllButton();

		PrevButton.setOnClickListener(new ClickButtonEventHandler());
		NextButton.setOnClickListener(new ClickButtonEventHandler());
		EndButton.setOnClickListener(new ClickButtonEventHandler());
	}
	public class ClickButtonEventHandler implements View.OnClickListener
	{
		public void onClick(View v) {
			if(v.equals(EndButton))
				dismiss();
			else if(v.equals(NextButton))
			{
				if(nCurrentCount<nResultCount-1)
					nCurrentCount++;
				if(nCurrentCount == nResultCount-1 )
					nButtonState	=	2;
				else if(nCurrentCount <nResultCount-1)
					nButtonState =1;
			}
			else if(v.equals(PrevButton))
			{
				if(nCurrentCount>=1)
					nCurrentCount--;
				if(nCurrentCount == 0 )
					nButtonState	=	3;
				else if(nCurrentCount >= 1)
					nButtonState =1;
			}
			DrawFortuneCard(resultNo[nCurrentCount]);
			SetAllButton();
		}
	}
	public void 	SetAllButton()
	{
		switch (nButtonState) {
			case 0:
				PrevButton.setEnabled(false);
				NextButton.setEnabled(false);
				break;
			case 2:
				PrevButton.setEnabled(true);
				NextButton.setEnabled(false);
				break;
			case 3:
				PrevButton.setEnabled(false);
				NextButton.setEnabled(true);
				break;
			default:
				PrevButton.setEnabled(true);
				NextButton.setEnabled(true);
				break;
		}
	}
	public void DrawFortuneCard(int nNumber)
	{
		FlowerCard aCard = new FlowerCard((nNumber-1)*4, m_context4Dialog);
		cardView.setImageBitmap(aCard.GetImage());
		textView.setText(R.string.fortune01 + nNumber - 1);
		cardView.setVisibility(View.VISIBLE);
		textView.setVisibility(View.VISIBLE);
	}
}


