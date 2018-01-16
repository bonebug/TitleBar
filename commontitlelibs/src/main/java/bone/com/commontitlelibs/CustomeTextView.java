package bone.com.commontitlelibs;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 功能：
 * 1.解决了设置头或中省略时的BUG
 * 2.兼容低版maxlines不起效问题
 * ＊创建者：赵然 on 2017/5/23 15:03
 * ＊
 */

public class CustomeTextView extends AppCompatTextView {

    private int mLineY;
    private int mViewWidth;

    public CustomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CustomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomeTextView(Context context) {
        super(context,null);
    }
    /**
     * 解决SDK16之后 maxlines不起作用的问题
     */
    private void init(Context  context,AttributeSet attrs) {
        // this is to overcome the calculateEllipsis bug in some versions of android, I spotted it on 4.4.4 and 4.4.3
        // see https://code.google.com/p/android/issues/detail?id=33868

        if (Build.VERSION.SDK_INT >= 16) {
            if (getMaxLines() == 1) {
                setSingleLine(true);
            }
        }
    }

}
