package com.example.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Pag1();
            case 1: return new Pag2();
            //case 2: return new Tab3();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override    public CharSequence getPageTitle(int position) {        switch (position){
        case 0: return "Arquivos";
        case 1: return "Enviar";
        default: return null;
    }
    }
}
