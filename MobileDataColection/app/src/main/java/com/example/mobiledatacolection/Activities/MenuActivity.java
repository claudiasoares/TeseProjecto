package com.example.mobiledatacolection.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobiledatacolection.Fragmentos.MenuFragment;
import com.example.mobiledatacolection.Fragmentos.NotificationFragment;
import com.example.mobiledatacolection.Fragmentos.SmsFragment;
import com.example.mobiledatacolection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private Button loadForm;
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_menu:
                                openFragment(MenuFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_forms:
                                openFragment(SmsFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_loadforms:
                                openFragment(NotificationFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //initComponents();
        
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void initComponents() {
        loadForm = (Button) findViewById(R.id.carregarformulario) ;
        loadForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadForm(v);
            }
        });
    }

    private void loadForm(View v) {

    }


}
