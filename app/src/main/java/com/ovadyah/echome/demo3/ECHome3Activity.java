package com.ovadyah.echome.demo3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo2.fragment.ECHome2Fragment;
import com.ovadyah.echome.demo3.fragment.ECHome3Fragment;

public class ECHome3Activity extends AppCompatActivity {

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, ECHome3Activity.class);
        activity.startActivity(intent);
    }

    protected int getLayoutResId() {
        return R.layout.activity_ec_home3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.initFragment();
    }

    private void initFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ec_fragment_container3);
        if (fragment == null) {
            fragment = this.createFragment();
            fm.beginTransaction().add(R.id.ec_fragment_container3, fragment).commit();
        }
    }

    protected Fragment createFragment() {
        return ECHome3Fragment.newInstance();
    }
}
