package com.phoenix.appstory.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.appstory.model.Story

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(story: Story)
}