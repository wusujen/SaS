package com.christine.cart;

import android.os.Bundle;
import android.view.ViewGroup;

public class CartActivity extends FooterActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.data);
        ViewGroup.inflate(CartActivity.this, R.layout.cart, vg);
    }
}