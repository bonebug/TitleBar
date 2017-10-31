package gome.com.gometextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 功能：
 * 1.解决了设置头或中省略时的BUG
 * 2.兼容低版maxlines不起效问题
 * 3.回行异常问题
 * ＊创建者：赵然 on 2017/5/23 15:03
 * ＊
 */

public class GomeTextView extends AppCompatTextView {

    private int mLineY;
    private int mViewWidth;
    /**
     * 是否改变字母或数字换行样式问题
     */

    private boolean isChangeCharOrNumNewLineStyle = false;

    public GomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public GomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public GomeTextView(Context context) {
        super(context,null);
    }
    /**
     * 解决SDK16之后 maxlines不起作用的问题
     */
    private void init(Context  context,AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GomeTextView);
        isChangeCharOrNumNewLineStyle = a.getBoolean(R.styleable.GomeTextView_isChangeCharOrNumNewLineStyle,false);
        // this is to overcome the calculateEllipsis bug in some versions of android, I spotted it on 4.4.4 and 4.4.3
        // see https://code.google.com/p/android/issues/detail?id=33868

        if (Build.VERSION.SDK_INT >= 16) {
            if (getMaxLines() == 1) {
                setSingleLine(true);
            }
        }
    }


//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        TextPaint paint = getPaint();
//        paint.setColor(getCurrentTextColor());
//        paint.drawableState = getDrawableState();
//        mViewWidth = getMeasuredWidth();
//        String text = getText().toString();
//        mLineY = 0;
//        mLineY += getTextSize();
//        Layout layout = getLayout();
//
//        // layout.getLayout()在4.4.3出现NullPointerException
//        if (layout == null) {
//            return;
//        }
//
//        Paint.FontMetrics fm = paint.getFontMetrics();
//
//        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
//        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());
//
//        for (int i = 0; i < layout.getLineCount(); i++) {
//            int lineStart = layout.getLineStart(i);
//            int lineEnd = layout.getLineEnd(i);
//            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
//            String line = text.substring(lineStart, lineEnd);
//            if (needScale(line) && i < layout.getLineCount() - 1) {
//                drawScaledText(canvas, lineStart, line, width);
//            } else {
//                canvas.drawText(line, 0, mLineY, paint);
//            }
//            mLineY += textHeight;
//        }
//    }
//
//    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
//        float x = 0;
//        if (isFirstLineOfParagraph(lineStart, line)) {
//            String blanks = "  ";
//            canvas.drawText(blanks, x, mLineY, getPaint());
//            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
//            x += bw;
//
//            line = line.substring(3);
//        }
//
//        int gapCount = line.length() - 1;
//        int i = 0;
//        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
//            String substring = line.substring(0, 2);
//            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
//            canvas.drawText(substring, x, mLineY, getPaint());
//            x += cw;
//            i += 2;
//        }
//
//        float d = (mViewWidth - lineWidth) / gapCount;
//        for (; i < line.length(); i++) {
//            String c = String.valueOf(line.charAt(i));
//            float cw = StaticLayout.getDesiredWidth(c, getPaint());
//            canvas.drawText(c, x, mLineY, getPaint());
//            x += cw + d;
//        }
//    }
//
//    private boolean isFirstLineOfParagraph(int lineStart, String line) {
//        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
//    }
//
//    private boolean needScale(String line) {
//        if (line == null || line.length() == 0) {
//            return false;
//        } else {
//            return line.charAt(line.length() - 1) != '\n';
//        }
//    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isChangeCharOrNumNewLineStyle) {
            //需要改变时则暂时修改为半角变全椒模式
            super.setText(StringBQUtils.bj2qj(text.toString()), type);
        } else {
            super.setText(text, type);
        }
    }
}
