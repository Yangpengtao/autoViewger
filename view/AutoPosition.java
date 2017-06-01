package cn.com.oomall.kktown.activity.seller.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.com.oomall.kktown.R;

/**
 * 根据数量添加索引
 * Yang pengtao
 * Created by root on 16-8-11.
 */
public class AutoPosition extends LinearLayout {
    //存放索引View的数组
    private ImageView[] markImage;
    //ViewPager的长度
    private int length = 1;


    public AutoPosition(Context context) {
        this(context, null);
    }

    public AutoPosition(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPosition(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置当前索引的位置
     *
     * @param position 当前索引的位置
     */
    public void setCurrentPositino(int position) {
        //先将索引的所有图标替换为未选中的状态
        for (int i = 0; i < length; i++) {
            markImage[i].setImageResource(R.drawable.noselect_position);
        }
        //再将当前索引号的图标替换为选中的状态
        if (position > length - 1) {
            position = length - 1;
        }
        markImage[position].setImageResource(R.drawable.select_position);
    }

    /**
     * 初始化索引
     *
     * @param length ViewPager的长度
     */
    public void initResources(int length) {
        removeAllViews();
        this.length = length;
        //根据长度确定需要多少个索引图标
        markImage = new ImageView[length];
        for (int i = 0; i < length; i++) {
            //初始化ImageView并且将索引图标添加到this(MarkLayout)中
            markImage[i] = new ImageView(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            markImage[i].setLayoutParams(params);
            addView(markImage[i]);
        }
        //设置当前索引为0，也就是第一个页面
        setCurrentPositino(0);
    }
}