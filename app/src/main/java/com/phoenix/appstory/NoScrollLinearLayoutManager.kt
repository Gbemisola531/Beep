package com.phoenix.appstory

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoScrollLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun canScrollHorizontally(): Boolean = false
}