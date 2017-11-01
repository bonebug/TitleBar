# TitleBar
# 使用ConstraintLayout简单封装的一个TitleBar控件。
**控件组成：五个TextView 分别为 A  AB  B  BC C 五部分 其中控件B永远剧中 详细效果请看Demo**
<br>
封装出发点：过往titleBar封装，一般控件的层级都比较高，即使使用相对布局依旧改变不了这种结果，布局稍微复杂一点就会令层级更多，
然而谷歌发布约束布局后就很好的解决了这个问题，这里只是五个TextView就基本满足了日常的需求，没有复杂的设计，也没有特殊的功能，
仅仅是使用了约束布局的一些特性， 真要说起来也只是个Demo级的控件，代码中存在的不优雅，脏代码欢迎大家指正！


## 项目结构
|项目名|描述|
|---|---|
|app|TitleBar示例代码
|commontitlelibs| 封装的TitleBar控件
|commonutilslibs| 存在一些简单的常用工具类，此处现在还不完善里面东西比较少 后续会扩充
|gometextview| TextView相关的一个组件库，目前含有偷的一个可展开的TextView,一个简单的处理了就API带来的一些BUG（这个控件很简单不细说了 瞅一眼就知道了） 
**以下为TitleBar的相关属性**

<br>
## 常量类--注解代替枚举类而已 ---主要操作的是哪个控件/哪个位置
|常量名|描述|
|ComponentLocationConstant.LEFT | 左侧位置
|ComponentLocationConstant.CENTER | 中间位置
|ComponentLocationConstant.RIGHT | 右侧位置

## 公共方法
|方法名|描述
|---|---|
|setCustomTittleView() |  设置自定义Title--当前view不满足的一些情况下可以使用此方法，仅留作扩展。。
|showCustomTittle() | 设置自定义Title 后需调用此方法展示自定义的title
|getCustomView() | 返回设置的自定义的Title
|showCommonTittle() | 显示公用title
|getLeftView() | 获取左侧View 
|getCenterLeftView() | 获取中偏左的控件
|getCenterView() |获取中间控件
|getCenterRightView() | 获取中偏右侧的控件
|getRightView() | 获取右侧控件

|setTextViewDrawableResource()|  设置指定位置控件指定位置的图片
|setTextColor()| 设置指定位置控件的文本颜色
|setViewText() | 设置指定位置控件的文本

| | 

|startAutoPlay() | 开启自动轮播--- 需设置可自动轮播
|stopAutoPlay() | 关闭自动轮播
|setEndLoop() | 设置是否无限轮播
|setNavigatorParams() | 设置导航点的位置
|setNavigatorPointsBackgroundColors() | 设置导航点的背景色
|setNavigatorPointsBackgroundDrawable() | 设置导航点的背景图
|setNavigatorPointsGaps() | 设置导航点间距
|setNavigatorPointsRadius() | 设置导航点的半径
|setPageTransformer() | 设置页面切换动画
|setPageChangeListener() | 设置页面切换监听

## 方法（BannerView---RecyclerView + SnapHelper）
|方法名|描述
|---|---|
|setNeedNavigator |  设置是否需要导航点
|addItemType() | 添加item布局类型
|getAdapter() | 获取banner的adapter
|isAutoPlay() | 获取当前banner是否在自动轮播
|setAutoPlay() | 设置banner是否自动轮播
|startAutoPlay() | 开启自动轮播--- 需设置可自动轮播
|stopAutoPlay() | 关闭自动轮播
|setEndLoop() | 设置是否无限轮播
|setNavigatorParams() | 设置导航点的位置
|setNavigatorPointsBackgroundColors() | 设置导航点的背景色
|setNavigatorPointsBackgroundDrawable() | 设置导航点的背景图
|setNavigatorPointsGaps() | 设置导航点间距
|setNavigatorPointsRadius() | 设置导航点的半径
|setPageTransformer() | 设置页面切换动画
|setPageChangeListener() | 设置页面切换监听




## 使用步骤

#### Step ... 看Demo

#### v1.0.0
* 添加基本功能





