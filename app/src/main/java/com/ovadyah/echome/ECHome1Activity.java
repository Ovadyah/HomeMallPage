package com.ovadyah.echome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ovadyah.echome.R;
import com.ovadyah.echome.home.HomeFragment;

public class ECHome1Activity extends AppCompatActivity {


    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, ECHome1Activity.class);
        activity.startActivity(intent);
    }

    protected int getLayoutResId() {
        return R.layout.activity_ec_home1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.initFragment();
    }


    private void initFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ec_fragment_container1);
        if (fragment == null) {
            fragment = this.createFragment();
            fm.beginTransaction().add(R.id.ec_fragment_container1, fragment).commit();
        }
    }

    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }
}
