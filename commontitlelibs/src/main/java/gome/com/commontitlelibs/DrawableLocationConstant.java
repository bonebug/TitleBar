package gome.com.commontitlelibs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 功能：
 * ＊创建者：赵然 on 2017/10/31 16:31
 * ＊
 */

public class DrawableLocationConstant {
    /**
     * 图片位置
     */
    public static  final int LEFT = 0;
    public static  final int CENTER = 1;
    public static  final int RIGHT = 2;
    public static  final int CENTER_RIGHT = 3;

    @IntDef({LEFT,CENTER,RIGHT,CENTER_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawableLocation{}
}
