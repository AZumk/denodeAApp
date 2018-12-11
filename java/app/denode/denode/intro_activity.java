package app.denode.denode;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class intro_activity extends FragmentActivity {

    ImageView progress;
    TextView pular;
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_activity);


        progress = (ImageView)findViewById(R.id.progressoIntro);
        pular = (TextView)findViewById(R.id.pular);

        pular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(intro_activity.this, home_activity.class);
                startActivity(intent);
            }
        });
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(position == 0) {
                        progress.setImageResource(R.drawable.ic_dots1);

                    } else if (position == 1){
                        progress.setImageResource(R.drawable.ic_dots2);

                    } else{
                        progress.setImageResource(R.drawable.ic_dots3);

                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        @Override
        public void onBackPressed() {
            if (mPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            } else {
                // Otherwise, select the previous step.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        }

        /**
         * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
         * sequence.
         */
        private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
            public ScreenSlidePagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                if(position == 0) {
                    //progress.setImageResource(R.drawable.ic_dots1);
                    return new intro_frag1();
                } else if (position == 1){
                    //progress.setImageResource(R.drawable.ic_dots2);
                    return new intro_frag2();
                } else{
                    //progress.setImageResource(R.drawable.ic_dots3);
                    return new intro_frag3();
                }
            }

            @Override
            public int getCount() {
                return NUM_PAGES;
            }
        }
    }


