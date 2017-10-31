/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 Manabu Shimobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gome.com.gometextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ExpandableTextView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    public static final int SHOWTYPE_TEXT = 0;
    public static final int SHOWTYPE_BUTTON = 1;
    public static final int SHOWTYPE_CUSTOM = 2;

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 8;

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 400;

    /* The default alpha value when the animation starts */
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    protected TextView mTv;

    protected ImageButton mButton; // Button to expand/collapse
    private TextView controllText;
    /**
     * 自定义展开区域的容器
     */
    private FrameLayout customContainer;

    private ViewGroup normalContainer;

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;

    private int mTextHeightWithMaxLines;

    private int mMaxCollapsedLines;

    private int mMarginBetweenTxtAndBottom;

    private Drawable mExpandDrawable;

    private Drawable mCollapseDrawable;

    private int mAnimationDuration;

    private float mAnimAlphaStart;

    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;
    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;

    private int mPosition;

    private int showType;
    /**
     *  展示文字点击是否可以触发展开或收起操作
     */
    private boolean  isTextClickToExpand;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }


    @Override
    public void onClick(View view) {
        if (getControllLay().getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        updateControllLay();

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }

        // mark that the animation is in progress
        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - mTv.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                applyAlphaAnimation(mTv, mAnimAlphaStart);
                if (mListener != null) {
                    mListener.onAnimStart(mTv, !mCollapsed,showType,getControllView());
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                // notify the listener
                if (mListener != null) {
                    mListener.onAnimComplete(mTv, !mCollapsed,showType,getControllView());
                    mListener.onExpandStateChanged(mTv, !mCollapsed,showType,getControllView());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    /**
     * 获取 控制样式为：文本时  文本组件
     * @return
     */
    public TextView  getControllText(){

        return controllText;
    }

    /**
     * 获取控制布局中的对应view
     * @return
     */
    private View getControllView(){
        switch (showType){
            case SHOWTYPE_TEXT:
                return controllText;
            case SHOWTYPE_BUTTON:
                return mButton;
            case  SHOWTYPE_CUSTOM:
                return customContainer.getChildAt(0);
        }
        //never  catch!!
        return null;
    }
    /**
     * 获取 控制样式为：button时  ImageButton组件
     * @return
     */
    public ImageButton getControllButton(){
        return mButton;
    }
    /**
     * 返回要操作的控制器布局
     * @return
     */
    private View getControllLay() {
        if (showType == SHOWTYPE_CUSTOM) {
            return customContainer;
        } else {
            return normalContainer;
        }

    }

    /**
     * showtype 为custom时  设置自定义的控制view样式
     */
    public void addCustomControllerView(View  view){
        if (showType != SHOWTYPE_CUSTOM){
            Log.w(TAG,"nong shalei..."+"showType="+showType);
            return ;
        }
        if (view.getParent() != null){
            ((ViewGroup)view.getParent()).removeView(view);
        }
        customContainer.removeAllViews();
        customContainer.addView(view);
        customContainer.setOnClickListener(this);

    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed

        getControllLay().setVisibility(View.GONE);
        mTv.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Saves the text height w/ max lines
        mTextHeightWithMaxLines = getRealTextViewHeight(mTv);

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
        }
        getControllLay().setVisibility(View.VISIBLE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTv.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
                }
            });
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(@Nullable CharSequence text) {
        mRelayout = true;
        mTv.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        updateControllLay();
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        if (mTv == null) {
            return "";
        }
        return mTv.getText();
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = a.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = a.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = a.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mExpandDrawable = a.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
        mCollapseDrawable = a.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
        showType = a.getInt(R.styleable.ExpandableTextView_showType, SHOWTYPE_TEXT);
        isTextClickToExpand = a.getBoolean(R.styleable.ExpandableTextView_isTextClickToExtend,false);


        if (mExpandDrawable == null) {
            mExpandDrawable = getDrawable(getContext(), R.drawable.ic_expand_more_black_12dp);
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = getDrawable(getContext(), R.drawable.ic_expand_less_black_12dp);
        }

        a.recycle();

        inflate(getContext(), R.layout.expandable_textview, this);
        normalContainer = (ViewGroup) findViewById(R.id.expand_normalcontroll_lay);
        switch (showType) {
            case SHOWTYPE_TEXT:
                normalContainer.setVisibility(VISIBLE);
                controllText = (TextView) findViewById(R.id.expandable_controll_tv);
                controllText.setVisibility(VISIBLE);
                controllText.setText("查看全部");
                controllText.setOnClickListener(this);
                break;
            case SHOWTYPE_BUTTON:
                normalContainer.setVisibility(VISIBLE);
                mButton = (ImageButton) findViewById(R.id.expandable_controll_button);
                mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
                mButton.setVisibility(VISIBLE);
                mButton.setOnClickListener(this);
                break;
            case SHOWTYPE_CUSTOM:
                customContainer = (FrameLayout) findViewById(R.id.custom_expandable_lay);
                customContainer.setVisibility(VISIBLE);
                break;
        }
        mTv = (TextView) findViewById(R.id.expandable_tv);
        if (isTextClickToExpand){

            mTv.setOnClickListener(this);
        }

        // default visibility is gone
        setVisibility(GONE);
    }


    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            // make it instant
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    /**
     * 更新控制布局
     */
    private void updateControllLay() {
        switch (showType) {
            case SHOWTYPE_TEXT:
                controllText.setText(mCollapsed ? "查看全部" : "收起");
                break;
            case SHOWTYPE_BUTTON:
                mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
                break;
            case SHOWTYPE_CUSTOM:
                // TODO: 2017/6/8
                break;
        }
    }

    /**
     * 当前view展示样式
     * @return
     */
    public int getShowType() {
        return showType;
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         * @param showType 当前view展示样式类型
         * @param controllView 当前的控制布局view SHOWTYPE_TEXT--TextView SHOWTYPE_BUTTON----ImageButton SHOWTYPE_CUSTOM---添加的自定义view
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded,int showType,View controllView);
        void  onAnimStart(TextView textView, boolean isExpanded,int showType,View controllView);
        void  onAnimComplete(TextView textView, boolean isExpanded,int showType,View controllView);
    }

}