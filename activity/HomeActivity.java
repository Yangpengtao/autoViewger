package cn.com.oomall.kktown.activity.seller;


/**
 * 商家首页
 * Yang pengtao
 */
public class HomeActivity extends BaseSellerActivity implements AutoViewPager.ItemClickListion {

    private final String TAG = " seller.HomeActivity";

    private AutoViewPager mVp;
    private AutoPosition mPostion;
    private ArrayList<String> str = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();
        findViewById();
        mVp.setClickListion(this);
        mVp.setData(getSupportFragmentManager(), str, mPostion, llContent, vpContent);
        mVp.addOnPageChangeListener(new MyPageChangeListener());

    }


    private void initData() {
        if (SpUtil.getInstance().getLoginAccount(this).contains("159")){
            startActivity(BindingPhoneActivity.class);
        }
        str.add("0");
        str.add("21");
        str.add("23");
        str.add("33");
        str.add("24");
        str.add("53");
        str.add("61");
        str.add("5.5");
        str.add("11");
        str.add("32");
        str.add("4.5");
        str.add("4.3");
        str.add("2.3");
    }


    private void findViewById() {
        mVp = (AutoViewPager) findViewById(R.id.vp);
        mPostion = (AutoPosition) findViewById(R.id.position);
        llContent = (LinearLayout) findViewById(R.id.llContent);
        vpContent = (RelativeLayout) findViewById(R.id.vpContent);
        centerImg = (ImageView) findViewById(R.id.centerImg);
    }

    @Override
    public void itemClick(int position) {
        str.set(str.size() - 1, "3.4");
        str.add("2.3");
        str.add("2.3");
        str.add("4");
        str.add("43");
        mVp.RefreshData(position, str, mPostion, getSupportFragmentManager(), llContent, vpContent);
    }

    


    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPostion.setCurrentPositino(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


  
}
