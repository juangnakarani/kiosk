package com.juangnakarani.kiosk;

import android.view.View;

interface IClickListener {
    public void onClick(View view,int position);
    public void onLongClick(View view, int position);
}
