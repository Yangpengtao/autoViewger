package cn.com.oomall.kktown.activity.seller.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.com.oomall.kktown.R;
import cn.com.oomall.kktown.Utils.LogPrinter;

/**
 * 首页顶部动态添加fragment布局
 * Yang pengtao
 * Created by root on 16-8-11.
 */
public class AutoViewPager extends ViewPager {

    private final String TAG = "AutoViewPager->>>";
    public static final String MORE = "更多";

    private Context mContext;
    public MyPagerAdapter myPagerAdapter;

    private List<ItemFragment> fragments;


    private static SparseArray<ArrayList<HomeItemView>> sparseArray;
    private static SparseArray<ArrayList<String>> sparseArrayStr;
//    private static ArrayList<ItemFragment> fragmentSparseArray;

    public AutoViewPager(Context context) {
        super(context);
        this.mContext = context;
        sparseArray = new SparseArray<>();
        sparseArrayStr = new SparseArray<>();
//        fragmentSparseArray = new ArrayList<>();
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        sparseArray = new SparseArray<>();
        sparseArrayStr = new SparseArray<>();
//        fragmentSparseArray = new ArrayList<>();

    }

    /**
     * 外部调用
     *
     * @param fm
     * @param str
     */
    public void setData(FragmentManager fm, ArrayList<String> str, AutoPosition position, LinearLayout llContent, RelativeLayout vpContent) {

        if (!str.get(str.size() - 1).equals(MORE)) str.add(MORE);
        int strSize = str.size();
        if (strSize <= 6) {
            vpContent.setVisibility(View.GONE);
            llContent.setVisibility(View.VISIBLE);
            sizeSmall(llContent, strSize, str);
        } else {
            llContent.setVisibility(View.GONE);
            vpContent.setVisibility(View.VISIBLE);
            sizeBig(fm, str, position);
        }
    }

    //当size小于6的时候
    private void sizeSmall(LinearLayout llContent, int size, ArrayList<String> str) {
        sparseArrayStr.clear();
        sparseArray.clear();
        llContent.removeAllViews();

        int count = 1;
        if (size > 3) {
            count = 2;
        }
        //创建存放view的集合
        ArrayList<HomeItemView> items = new ArrayList<>();

        //添加布局
        for (int i = 0; i < count; i++) {
            LinearLayout child = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.ome_viewpager_item_row, null);
            llContent.addView(child);
            for (int j = 0; j < child.getChildCount(); j++) {
                HomeItemView itemView = (HomeItemView) child.getChildAt(j);
                items.add(itemView);
            }
        }
        sparseArray.append(0, items);
        sparseArrayStr.append(0, str);
        //为布局添加数据
        for (int i = 0; i < size; i++) {
            HomeItemView homeItemView = items.get(i);
            if (str.get(i).equals(MORE)) {
                homeItemView.setImgVisible(true).setTopTvVisible(false).setTopImg(R.drawable.home_add).setBottomText(str.get(i)).setBottomTvTopMargin(0, 20, 0, 0);
                homeItemView.setTag(str.get(i));
            } else {
                homeItemView.setTopText(str.get(i)).setImgVisible(false).setBottomText("今日总访客").setTopText(str.get(i));
            }
            homeItemView.setTag(R.id.home_item_page, 0);
            homeItemView.setOnClickListener(new MyItemClick());
        }
    }

    //当size大于6的时候
    private void sizeBig(FragmentManager fm, ArrayList<String> str, AutoPosition position) {
        sparseArrayStr.clear();
        sparseArray.clear();
        ArrayList<ArrayList<String>> list = analyzeData(str);
        int count = list.size();
        if (count == 1) position.setVisibility(View.GONE);
        position.initResources(count);
        fragments = new ArrayList<>();
        /**
         * 把整理好的数据。分发给fagment
         */
        for (int i = 0; i < count; i++) {
            ArrayList<String> childList = list.get(i);
            ItemFragment itemFragment = new ItemFragment(childList, i);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", childList);
            bundle.putInt("position",i);
            itemFragment.setArguments(bundle);
            fragments.add(itemFragment);
//            fragmentSparseArray.add(itemFragment);
            sparseArrayStr.append(i, childList);
        }
        myPagerAdapter = new MyPagerAdapter(fm, fragments);
        setAdapter(myPagerAdapter);
        LogPrinter.e(TAG, str.size() + "---------" + "----" + myPagerAdapter.getCount());
    }


    /**
     * 把数据整理成九个一组
     *
     * @param str
     * @return
     */
    private ArrayList<ArrayList<String>> analyzeData(ArrayList<String> str) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> child = new ArrayList<>();
        for (int i = 0; i < str.size(); i++) {
            child.add(str.get(i));
            if ((i + 1) % 9 == 0 && i != 0) {
                list.add(child);
                child = new ArrayList<>();
            }
        }
        if (child.size() != 0 && child.size() != 9) {
            list.add(child);
        }
        return list;
    }

    public void RefreshData(int postion, ArrayList<String> str, AutoPosition position, FragmentManager fm, LinearLayout llContent, RelativeLayout vpContent) {
        if (!str.get(str.size() - 1).equals(MORE)) str.add(MORE);
        if (str.size() <= 6) {
            llContent.setVisibility(View.VISIBLE);
            vpContent.setVisibility(View.GONE);
            sizeSmall(llContent, str.size(), str);
            return;
        }
        llContent.setVisibility(View.GONE);
        vpContent.setVisibility(View.VISIBLE);
        sparseArray.clear();
        sparseArrayStr.clear();
        if (fragments == null) fragments = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            fm.beginTransaction().remove(fragments.get(i));
            fm.beginTransaction().commit();
        }
        fragments.clear();


        ArrayList<ArrayList<String>> list = analyzeData(str);
        int count = list.size();
        if (count == 1)
            position.setVisibility(View.GONE);
        else
            position.setVisibility(View.VISIBLE);
        position.initResources(count);
        position.setCurrentPositino(postion);
        /**
         * 把整理好的数据。分发给fagment
         */
        for (int i = 0; i < count; i++) {
            ArrayList<String> childList = list.get(i);
            ItemFragment itemFragments = new ItemFragment(childList, i);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", childList);
            bundle.putInt("position",i);
            itemFragments.setArguments(bundle);
            fragments.add(itemFragments);
            sparseArrayStr.append(i, childList);
        }
        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter(fm, fragments);
            setAdapter(myPagerAdapter);
        } else
            myPagerAdapter.setData(fragments);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        private final String TAG = "MyPagerAdapter--->>";
        private List<ItemFragment> mFragments;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public MyPagerAdapter(FragmentManager fm, List<ItemFragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            LogPrinter.e(TAG, "-----getItem---");
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        public void setData(List<ItemFragment> fragments) {
            this.mFragments = fragments;
            notifyDataSetChanged();
        }
    }


    @SuppressLint("ValidFragment")
    public static class ItemFragment extends Fragment {
        private final String TAG = "ItemFragment--->>";
        ArrayList<HomeItemView> mListView;


        private ArrayList<String> mData;

        private int mPosition;

        public ItemFragment() {
        }

        public ItemFragment(ArrayList<String> data, int position) {
            this.mData = data;
            this.mPosition = position;
        }


        View v;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.home_viewpager_item, container, false);
            mListView = new ArrayList<>();
//            if (getArguments().getStringArrayList("data") != null)
//                mData = getArguments().getStringArrayList("data");
//            else
//                mData = sparseArrayStr.get(mPosition);
            mPosition=getArguments().getInt("position");
            getAllChild(v);
            setData(sparseArrayStr.get(mPosition));
            return v;
        }


        private void getAllChild(View v) {

            LinearLayout cotent = (LinearLayout) v;
            int childCount = cotent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                LinearLayout llChild = (LinearLayout) cotent.getChildAt(i);
                int llChildCount = llChild.getChildCount();
                for (int j = 0; j < llChildCount; j++) {
                    HomeItemView childAt = (HomeItemView) llChild.getChildAt(j);
                    mListView.add(childAt);
                }
            }
            sparseArray.append(mPosition, mListView);
        }


        public void setData(ArrayList<String> data) {
//            LogPrinter.e("------------------------");
            if (data == null) return;
            int size = data.size();
            for (int i = 0; i < size; i++) {
                HomeItemView item = mListView.get(i);
                if (data.get(i).equals(MORE)) {
                    item.setImgVisible(true).setTopTvVisible(false).setTopImg(R.drawable.home_add).setBottomText(data.get(i)).setBottomTvTopMargin(0, 20, 0, 0);
                    item.setTag(data.get(i));
                } else {
                    item.setTopText(data.get(i)).setImgVisible(false).setBottomText("今日总访客").setTopText(data.get(i));
                }
                item.setTag(R.id.home_item_page, mPosition);
                item.setOnClickListener(new MyItemClick());
            }
            sparseArrayStr.append(mPosition, data);
        }


    }

    private static class MyItemClick implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() != null) {
                String tag = (String) view.getTag();
                if (tag.equals(MORE)) {
                    int position = (int) view.getTag(R.id.home_item_page);
                    clickListion.itemClick(position);
                }
            }
        }
    }

    private static ItemClickListion clickListion;

    public void setClickListion(ItemClickListion clickListion) {
        this.clickListion = clickListion;
    }

    public interface ItemClickListion {
        public void itemClick(int position);
    }


}
