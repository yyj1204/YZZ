/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {

	public PullToRefreshScrollView(Context context) {
		super(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	private ScrollView scrollView;
	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalScrollViewSDK9(context);
		} else {
			scrollView = new ScrollView(context, attrs);
		}
		scrollView.setId(R.id.scrollview);
		mScrollView = scrollView;//定义一个ScrollView的成员变量
		return scrollView;
	}
	//获得下拉刷新中的ScrollView对象
	public ScrollView getPtfScrollView()
	{
		return mScrollView;
	}
	//定义一个滑动到一定高度的方法。
	public void scrollTo(int height)
	{
		mScrollView.scrollTo(0, height);
	}
	//平滑的滑动
	public void mySmoothScrollTo(int height)
	{
		mScrollView.smoothScrollTo(0, height);
	}
	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@TargetApi(9)
	final class InternalScrollViewSDK9 extends ScrollView {

		public InternalScrollViewSDK9(Context context)
		{
			super(context);
		}
		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt)
		{
			//5.确定监听的时机，并且确定要传递的参数
			if (listener != null)
			{
				listener.onFloatScroll(t);
			}
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
	//1.定义监听接口
	public interface OnFloatScrollListener
	{
		//4.定义抽象方法
		void onFloatScroll(int height);
	}
	//2.定义接口变量
	private OnFloatScrollListener listener;
	private ScrollView mScrollView;
	//3.定义设置监听接口方法
	public void setOnFloatScrollListener(OnFloatScrollListener l)
	{
		listener = l;
	}
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
								   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

		// Does all of the hard work...
		OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
				getScrollRange(), isTouchEvent);

		return returnValue;
	}

	/**
	 * Taken from the AOSP ScrollView source
	 */
	private int getScrollRange() {
		int scrollRange = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
		}
		return scrollRange;
	}
}

