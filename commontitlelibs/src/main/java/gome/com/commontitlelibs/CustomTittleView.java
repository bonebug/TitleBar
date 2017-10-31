package gome.com.commontitlelibs;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gome.com.gometextview.GomeTextView;


/**
 * 公用的title
 *
 * @author 赵然
 */
public final class CustomTittleView extends ConstraintLayout {
    /**
     * todo 默认的左侧图片ID   一般为返回按钮 不同APP集成需修改图片
     */
    private int defaultLeftDrawableID = R.drawable.icon_back_white;
    private GomeTextView mLeftTextView, mCenterTextView, mRightTextView, mCenterRightView,mCenterLeftView;
    /**
     * 自定义的tittle 对应的view
     */
    private ViewGroup mCustomTittle;
    /**
     * 标记当前是否是自定义的tittle
     */
    private boolean isCustomTittle = false;

    /**
     * 左侧文字的图片
     */
    private Drawable leftTextDrawable;

    /**
     * 自定义的titleview
     */
    private View customView;



    public CustomTittleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.base_title, this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTittleView, 0, 0);

        String leftTextStr = a.getString(R.styleable.CustomTittleView_leftTextText);
        float leftTextSize = a.getDimension(R.styleable.CustomTittleView_leftTextTextSize,16);

        String centerTextStr = a.getString(R.styleable.CustomTittleView_centerTextText);
        float centerTextSize = a.getDimension(R.styleable.CustomTittleView_centerTextTextSize,18);

        String rightTextStr = a.getString(R.styleable.CustomTittleView_rightTextText);
        float rightTextSize = a.getDimension(R.styleable.CustomTittleView_rightTextTextSize,16);

        String centerRightTextStr = a.getString(R.styleable.CustomTittleView_centerRightText);
        float centerRightTextSize = a.getDimension(R.styleable.CustomTittleView_centerRightTextTextSize,12);

        String centerLeftTextStr = a.getString(R.styleable.CustomTittleView_centerRightText);
        float centerLeftTextSize = a.getDimension(R.styleable.CustomTittleView_centerRightTextTextSize,12);

        Drawable  leftTextDrawable = a.getDrawable(R.styleable.CustomTittleView_leftTextDrawable);


        a.recycle();

        init();


        //设置左边的文字
        if (!TextUtils.isEmpty(leftTextStr)) {
            mLeftTextView.setText(leftTextStr);
        }
        mLeftTextView.setTextSize(leftTextSize);
        //设置右边的文字
        if (!TextUtils.isEmpty(rightTextStr)) {
            mRightTextView.setText(rightTextStr);
        }
        mRightTextView.setTextSize(rightTextSize);

        //设置中间文字
        if (!TextUtils.isEmpty(centerTextStr)) {
            mCenterTextView.setText(centerTextStr);
        }
        mCenterTextView.setTextSize(centerTextSize);

        if (!TextUtils.isEmpty(centerRightTextStr)) {
            mCenterRightView.setText(centerRightTextStr);
        }
        mCenterRightView.setTextSize(centerRightTextSize);

        if (!TextUtils.isEmpty(centerLeftTextStr)){
            mCenterLeftView.setText(centerLeftTextStr);
        }
        mCenterLeftView.setTextSize(centerLeftTextSize);

        //设置左侧的图片
        if (leftTextDrawable != null) {
            setTextViewDrawable(DrawableLocationConstant.LEFT, leftTextDrawable, DrawableLocationConstant.LEFT);
        }

    }

    /**
     * 获取整体view 在此可以设置背景
     *
     * @return
     */
    public CustomTittleView getTittleView() {
        return this;
    }

    /**
     * 页面初始化
     */
    private void init() {
        mLeftTextView =  findViewById(R.id.base_left);
        mCenterTextView =  findViewById(R.id.centertext);
        mRightTextView =  findViewById(R.id.base_right);
        mCustomTittle =  findViewById(R.id.fl_basetittle_content);
        mCenterRightView =  findViewById(R.id.centertext_right);
        mCenterLeftView =  findViewById(R.id.centertext_left);
    }

    /**
     * 设置自定义的title view
     *
     * @param view
     */
    public void setCustomTittleView(@NonNull View view) {
        if (view != null) {
            showCustomTittle();
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeAllViews();
            }
            mCustomTittle.addView(view);
            this.customView = view;
        }
    }

    /**
     * 显示自定义tittle
     */
    public void showCustomTittle() {
        isCustomTittle = true;
        mCustomTittle.setVisibility(View.VISIBLE);

        mLeftTextView.setVisibility(GONE);
        mCenterTextView.setVisibility(GONE);
        mCenterRightView.setVisibility(GONE);
        mRightTextView.setVisibility(GONE);
        mCenterLeftView.setVisibility(GONE);
    }

    /**
     * 返回自定义的view
     *
     * @return
     */
    public View getCustomView() {
        return customView;
    }

    /**
     * 显示公用tittle
     */
    public void showCommonTittle() {
        mCustomTittle.setVisibility(View.GONE);
    }

    /**
     * 获取中间偏右侧的文本view
     *
     * @return
     */
    public TextView getCenterRightView() {
        return mCenterRightView;
    }


    public GomeTextView getmCenterLeftView() {
        return mCenterLeftView;
    }

    /**
     * 设置 公共tittle 显示
     */
    private void setViewVisiable(@DrawableLocationConstant.DrawableLocation int type) {
        setVisibility(VISIBLE);
        if (!isCustomTittle) {
            switch (type) {
                case DrawableLocationConstant.LEFT:
                    mLeftTextView.setVisibility(VISIBLE);
                    break;
                case DrawableLocationConstant.CENTER:
                    mCenterTextView.setVisibility(VISIBLE);
                    break;
                case DrawableLocationConstant.RIGHT:
                    mRightTextView.setVisibility(VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 设置文本的图片
     *
     * @param viewType         修改视图的位置  0:左边 1:中间  2:右边
     * @param drawable         图片
     * @param drawableLocation 0: left 1:right
     *                         图片位置
     */
    public void setTextViewDrawable(int viewType, Drawable drawable,@DrawableLocationConstant.DrawableLocation int drawableLocation) {
        drawable = getAvailableDrawable(drawable);
        setViewVisiable(viewType);
        switch (viewType) {
            case DrawableLocationConstant.LEFT:
                setTextViewDrawableWithLocation(mLeftTextView, drawable, drawableLocation);
                break;
            case DrawableLocationConstant.CENTER:
                setTextViewDrawableWithLocation(mCenterTextView, drawable, drawableLocation);
                break;
            case DrawableLocationConstant.RIGHT:
                setTextViewDrawableWithLocation(mRightTextView, drawable, drawableLocation);
                break;
            default:
                break;
        }

    }

    /**
     * 设置右侧文本的左侧的图片
     *
     * @param viewType         修改视图的位置  0:左边 1:中间  2:右边
     * @param drawableId       图片ID
     * @param drawableLocation 0: left 1:right
     *                         图片位置
     */
    public void setTextViewDrawableResource(int viewType, @DrawableRes int drawableId, @DrawableLocationConstant.DrawableLocation int drawableLocation) {
        Drawable drawable = getViewDrawable(drawableId);
        setViewVisiable(viewType);
        switch (viewType) {
            case DrawableLocationConstant.LEFT:
                setTextViewDrawableWithLocation(mLeftTextView, drawable, drawableLocation);
                break;
            case DrawableLocationConstant.CENTER:
                setTextViewDrawableWithLocation(mCenterTextView, drawable, drawableLocation);
                break;
            case DrawableLocationConstant.RIGHT:
                setTextViewDrawableWithLocation(mRightTextView, drawable, drawableLocation);
                break;
            default:
                break;
        }

    }

    /**
     * 根据图片位置 设置图片
     *
     * @param view
     * @param drawable
     * @param drawableLocation
     */
    private void setTextViewDrawableWithLocation(TextView view, Drawable drawable,@DrawableLocationConstant.DrawableLocation int drawableLocation) {
        switch (drawableLocation) {
            case DrawableLocationConstant.LEFT:
                view.setCompoundDrawables(drawable, null, null, null); // 设置左图标
                break;
            case DrawableLocationConstant.RIGHT:
                view.setCompoundDrawables(null, null, drawable, null); // 设置右图标
                break;
            default:
                break;

        }

    }

    /**
     * 设置左侧文本的颜色
     *
     * @param colorId eg: R.color.blue
     */
    public void setLeftTextColorResourceId(@ColorRes int colorId) {
        setTextColor(mLeftTextView, colorId);
    }


    /**
     * 设置中间文本的颜色
     *
     * @param colorId eg: R.color.blue
     */
    public void setCenterTextColorResourceId(@ColorRes int colorId) {

        setTextColor(mCenterTextView, colorId);

    }

    /**
     * /**
     * 设置右侧文本的颜色
     *
     * @param colorId eg: R.color.blue
     */
    public void setRightTextColorResourceId(@ColorRes int colorId) {

        setTextColor(mRightTextView, colorId);

    }


    /**
     * 设置中右侧文本的文字颜色
     *
     * @param resourceID eg:R.color.black
     */
    public void setCenterRightViewTextColorResourceId(@ColorRes int resourceID) {
        setTextColor(mCenterRightView, resourceID);
    }



    /**
     * 设置文本颜色的方法最终调用类
     *
     * @param view            设置颜色的文本控件
     * @param colorResourceID 颜色对应的资源值  eg:R.color.blue
     */
    private void setTextColor(TextView view, @ColorRes int colorResourceID) {
        view.setTextColor(ContextCompat.getColor(getContext(), colorResourceID));
    }

    /**
     * 设置TestView时需要先获取drawable 这里根据ID构建Drawable
     *
     * @param drawableID
     * @return
     */
    private Drawable getViewDrawable(@DrawableRes int drawableID) {

        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableID);

        return getAvailableDrawable(drawable);
    }

    /**
     * 设置drawable大小
     *
     * @param drawable
     * @return
     */
    private Drawable getAvailableDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }


    /**
     * 获取左侧的view
     *
     * @return
     */
    public TextView getLeftView() {
        return mLeftTextView;
    }

    /**
     * 获取右侧的view
     *
     * @return
     */
    public TextView getRightView() {
        mRightTextView.setVisibility(VISIBLE);
        return mRightTextView;
    }

    /**
     * 获取中间view
     *
     * @return
     */
    public TextView getCenterView() {
        return mCenterTextView;
    }

    /**
     * 获取中右侧的文本控件
     *
     * @return
     */
    public TextView getCenterRightTextView() {
        return mCenterRightView;
    }

    /**
     * 设置左侧的文字
     *
     * @param resouceID 文字对应的ID
     */

    public void setLeftTittleText(@StringRes int resouceID) {
        setTextViewText(mLeftTextView, resouceID);
    }

    /**
     * 设置左侧文字
     *
     * @param str
     */
    public void setLeftTittleText(String str) {
        setTextViewText(mLeftTextView, str);
    }

    /**
     * 设置右侧文本
     *
     * @param resouceID 文本对应的ID
     */
    public void setRightTitleText(@StringRes int resouceID) {
        setTextViewText(mRightTextView, resouceID);
    }

    /**
     * 设置右侧文本文字
     *
     * @param str
     */
    public void setRightTitleText(String str) {
        setTextViewText(mRightTextView, str);

    }

    /**
     * 设置中部文本
     *
     * @param resouceID
     */
    public void setCenterTitleText(@StringRes int resouceID) {
        setTextViewText(mCenterTextView, resouceID);
    }

    /**
     * 设置中部文本
     *
     * @param str
     */
    public void setCenterTitleText(String str) {
        setTextViewText(mCenterTextView, str);
    }

    /**
     * 设置中间右侧的文字文本
     *
     * @param content
     */
    public void setCenterRightViewText(String content) {
        setTextViewText(mCenterRightView, content);

    }

    /**
     * 设置中间右侧的文字文本
     *
     * @param contentID
     */
    public void setCenterRightViewText(@StringRes int contentID) {
        setTextViewText(mCenterRightView, contentID);

    }

    /**
     * 设置中间左侧的文字文本
     * @param context
     */
    public void  setCenterLeftViewText(String context){
        setTextViewText(mCenterLeftView,context);
    }

    /**
     * 设置中间左侧的文字文本
     * @param contentID
     */

    public void  setCenterLeftViewText(@StringRes int contentID){
        setTextViewText(mCenterLeftView,contentID);
    }

    /**
     * 设置文本的文字
     *
     * @param view
     * @param str
     */
    private void setTextViewText(TextView view, String str) {
        setVisibility(VISIBLE);
        view.setText(str);
        view.setVisibility(VISIBLE);
        if (TextUtils.isEmpty(str)){
            view.setVisibility(GONE);
        }
    }

    /**
     * 设置文本的文字
     *
     * @param view
     * @param strID
     */
    private void setTextViewText(TextView view, @StringRes int strID) {
        setVisibility(VISIBLE);
        view.setVisibility(VISIBLE);
        view.setText(getResources().getString(strID));
    }

    /**
     * 左侧的图片
     *
     * @param drawableID 图片对应的ID
     */
    public void setLeftViewLeftDrawable(@DrawableRes int drawableID) {
        setVisibility(View.VISIBLE);
        setTextViewDrawableResource(DrawableLocationConstant.LEFT, drawableID, DrawableLocationConstant.LEFT); // 设置左图标
    }

    public void setLeftViewTextAndLeftDrawable(String str,int drawableID) {
        setTextViewText(mLeftTextView,str);
        setTextViewDrawableResource(DrawableLocationConstant.LEFT, drawableID, DrawableLocationConstant.LEFT); // 设置左图标

    }

    /**
     * 设置右侧文本的图片 默认图片在左侧
     *
     * @param drawableID 图片对应的ID
     */
    public void setRightViewRightDrawable(@DrawableRes int drawableID) {
        setVisibility(View.VISIBLE);
        setTextViewDrawableResource(DrawableLocationConstant.RIGHT, drawableID, DrawableLocationConstant.RIGHT); // 设置左图标
    }

    /**
     * 设置右侧文本 及 对应图标(图标默认在左侧)
     *
     * @param str      文本内容
     * @param drawableID 文本对应的ID
     */

    public void setRightTextAndLeftDrawable(String str, @DrawableRes int drawableID) {

        setVisibility(View.VISIBLE);

        setRightTitleText(str);

        setTextViewDrawableResource(DrawableLocationConstant.RIGHT, drawableID, DrawableLocationConstant.LEFT); // 设置左图标

    }

    /**
     * 设置中间文本的右侧图片
     *
     * @param drawableID
     */
    public void setCenterViewRightDrawable(@DrawableRes int drawableID) {

        setTextViewDrawableResource(DrawableLocationConstant.CENTER, drawableID, DrawableLocationConstant.RIGHT);
    }

    public void setCenterViewTextAndRightDrawable(String  str, int drawableID) {
        setTextViewText(mCenterRightView, str);
        setTextViewDrawableResource(DrawableLocationConstant.CENTER, drawableID, DrawableLocationConstant.RIGHT);
    }


    /**
     * 显示默认的普通tittle   左侧返回按钮 点击返回  中间展示文字
     *
     * @param str
     */
    public void showDefaultTittle(String  str) {
        setVisibility(VISIBLE);
        setTextViewText(mCenterTextView, str);
        setLeftViewLeftDrawable(defaultLeftDrawableID);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/5/19 此处根据不同APP处理 关闭页面 收起键盘
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale =  getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private int sp2px( int spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
