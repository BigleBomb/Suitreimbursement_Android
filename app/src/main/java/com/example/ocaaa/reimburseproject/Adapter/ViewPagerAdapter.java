package com.example.ocaaa.reimburseproject.Adapter;

/**
 * Created by Asus on 7/31/2017.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.ocaaa.reimburseproject.Fragment.CategoryFragment;
import com.example.ocaaa.reimburseproject.R;

import java.util.ArrayList;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    //integer to count number of tabs
    int tabCount;

    private ArrayList<CategoryFragment> categoryFragments = new ArrayList<>();

//    ArrayList<CategoryFragment> listFragment;

    Context context;

    //Constructor to the class
    public ViewPagerAdapter(FragmentManager fm, int tabCount, Context context) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.context = context;
        categoryFragments.add(0, new CategoryFragment());
        categoryFragments.add(1, new CategoryFragment());
        categoryFragments.add(2, new CategoryFragment());
        categoryFragments.add(3, new CategoryFragment());
    }

    @Override
    public String getPageTitle(int position) {

//        return listFragment.get(position).getCategory().getName();
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.categoryAll);
            case 1:
                return context.getResources().getString(R.string.categoryTransportation);
            case 2:
                return context.getResources().getString(R.string.categoryConsumption);
            case 3:
                return context.getResources().getString(R.string.categoryAccommodation);
        }
        return null;
    }
    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {

//        return listFragment.get(position);
        //Returning the current tabs
        switch (position) {
            case 0:
                return categoryFragments.get(0);
            case 1:
                return categoryFragments.get(1);
            case 2:
                return categoryFragments.get(2);
            case 3:
                return categoryFragments.get(3);
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        registeredFragments.remove(position);
//        super.destroyItem(container, position, object);
//    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
