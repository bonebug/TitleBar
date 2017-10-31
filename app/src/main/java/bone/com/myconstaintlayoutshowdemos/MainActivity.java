package bone.com.myconstaintlayoutshowdemos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import gome.com.commontitlelibs.CustomTittleView;
import gome.com.commonutilslibs.DensityUtil;

public class MainActivity extends AppCompatActivity {
    private CustomTittleView tittleView;
    private String centerTitle = "谁都会疯狂但是分身乏术地方";
    private String leftTitle = "返回";
    private String rightTitle = "提交";


    private EditText leftTextET, centerTextET, rightTextET, centerRightTextET, centerLeftTextET;
    private Button leftTextBtn, centerTextBtn, rightTextBtn, centerRightTextBtn, centerLeftTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏


        tittleView = findViewById(R.id.ctv_title);
        tittleView.setCenterTitleText(centerTitle);
        tittleView.setLeftTittleText(leftTitle);
        tittleView.setBackgroundColor(Color.GRAY);
        tittleView.setLeftViewLeftDrawable(R.drawable.ic_message_back_arrow);
        tittleView.getLeftView().setCompoundDrawablePadding(DensityUtil.dip2px(MainActivity.this,10));

        tittleView.setCenterLeftViewText("关闭");
        leftTextET = findViewById(R.id.et_leftviewtext);
        leftTextBtn = findViewById(R.id.btn_leftviewtext_commit);

        centerLeftTextBtn = findViewById(R.id.btn_centerleftviewtext_commit);
        centerLeftTextET = findViewById(R.id.et_centerleftviewtext);

        centerTextBtn = findViewById(R.id.btn_centerviewtext_commit);
        centerTextET = findViewById(R.id.et_centerviewtext);

        centerRightTextBtn = findViewById(R.id.btn_centerrightviewtext_commit);
        centerRightTextET = findViewById(R.id.et_centerrightviewtext);


        rightTextBtn = findViewById(R.id.btn_rightviewtext_commit);
        rightTextET = findViewById(R.id.et_rightviewtext);


        initClick();

    }

    /**
     * 处理点击事件
     */
    private void initClick() {
        leftTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittleView.setLeftTittleText(leftTextET.getText().toString().trim());
            }
        });

        centerLeftTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittleView.setCenterLeftViewText(centerLeftTextET.getText().toString().trim());
            }
        });

        centerTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittleView.setCenterTitleText(centerTextET.getText().toString().trim());
            }
        });

        centerRightTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittleView.setCenterRightViewText(centerRightTextET.getText().toString().trim());
            }
        });

        rightTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittleView.setRightTitleText(rightTextET.getText().toString().trim());
            }
        });

    }
}
