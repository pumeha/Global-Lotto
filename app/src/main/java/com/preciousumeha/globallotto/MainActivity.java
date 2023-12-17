package com.preciousumeha.globallotto;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.logo_good);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        NavHostFragment hostFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_mainActivity);
        if (hostFragment != null){
            navController  = hostFragment.getNavController();
         this.setupBottomNavMenu(navController);
        }

    }
    private void setupBottomNavMenu(NavController navController){
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigationView);
        if (bottomNavView != null) {
            NavigationUI.setupWithNavController(bottomNavView, navController);
        }
    }



    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.fragment_mainActivity);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       // finishAffinity();
//       startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//       // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//        if (getIntent().getBooleanExtra("EXIT",false)){
//            finish();
//        }
    }
}
