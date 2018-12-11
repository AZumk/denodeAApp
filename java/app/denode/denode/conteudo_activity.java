package app.denode.denode;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import dbManager.bancoController;
import helpers.AdapterConteudo;
import helpers.AdapterHomeTarefas;

import static android.media.CamcorderProfile.get;
import static app.denode.denode.R.id.empty2;
import static app.denode.denode.R.id.empty3;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.idTarefa;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;

public class conteudo_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conteudo_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(40, 40);

        Menu menu = bnve.getMenu();
        MenuItem item = menu.findItem(R.id.menu_conteudo).setIcon(R.drawable.leituraicoselect);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:

                                Intent goToHome = new Intent(conteudo_activity.this, home_activity.class);
                                startActivity(goToHome);
                                break;
                            case R.id.menu_agenda:

                                Intent goToAgenda = new Intent(conteudo_activity.this, agenda_activity.class);
                                startActivity(goToAgenda);
                                break;
                            case R.id.menu_diario:

                                Intent goToDiario = new Intent(conteudo_activity.this, diarioHabitos_activity.class);
                                startActivity(goToDiario);
                                break;
                            case R.id.menu_conteudo:

                                Intent goToConteudo = new Intent(conteudo_activity.this, conteudo_activity.class);
                                startActivity(goToConteudo);
                                break;
                            case R.id.menu_perfil:

                                Intent goToPerfil = new Intent(conteudo_activity.this, perfil_activity.class);
                                startActivity(goToPerfil);
                                break;
                        }
                        return true;
                    }
                });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new allPosts(), "Todos");
        adapter.addFragment(new salvosPosts(), "Favoritos");
        adapter.addFragment(new lidosPosts(), "Arquivados");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
