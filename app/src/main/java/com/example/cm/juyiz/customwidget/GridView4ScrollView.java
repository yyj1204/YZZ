package com.example.cm.juyiz.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决GridView在ScrollView中嵌套时只显示一行的问题
 * @author Administrator
 *
 */
public class GridView4ScrollView extends GridView {
	public GridView4ScrollView(Context context) {
		super(context);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	/**
	 * 重写该方法，重新计算GridView高度
	 * 累计所有行，计算出GridView的最大高度
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(
			Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
