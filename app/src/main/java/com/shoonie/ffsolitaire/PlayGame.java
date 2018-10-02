package com.shoonie.ffsolitaire;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class PlayGame {
		public int getResultCount()
		{
			return 0;
		}
		public int[] getResultNumberArray()
		{
			return null;
		}
		public abstract void	DrawAll(Canvas canvas);
		public abstract boolean	CheckDbClick(Point pt);
		public abstract boolean	SetMouseRegion();
		public abstract boolean	ShuffleAndInit();
		public abstract int		CheckPoint(Point pt, Rect rt1, Rect rt2);
		public void autoComplete(){}
}
