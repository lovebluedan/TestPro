package com.example.testpro.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.example.testpro.R
import com.example.testpro.base.BaseActivity
import com.example.testpro.base.BaseMVVMActivity
import com.example.testpro.databinding.ActivityMainBinding
import com.example.testpro.viewmodel.MainActivityViewModel

class MainActivity: BaseMVVMActivity<ActivityMainBinding, MainActivityViewModel>(){

    var arrayList = arrayListOf<String>(
        "https://oss.chathot.me/nextvideo/avatar/DB4EBFB1-7FA4-4A6D-B9B7-27C78D8C6CBD-67632-00000EEC6AC898F1.jpg",
        "https://oss.chathot.me/nextvideo/avatar/android1000444_51e28925caba49dea58848d6adbb6d7994e54b94.jpg",
        "https://oss.chathot.me/nextvideo/avatar/886B8806-F3F7-442B-9783-4CD470747179-782-00000080CBC0867A.jpg",
        "https://oss.chathot.me/nextvideo/avatar/android1000347_b683a7753a8f0dea72d9427427c70f2f3cb786a1"
    )

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding.spannerTextView.setOnClickListener {
            (mDataBinding.root as ViewGroup).addView(
                View(this).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                    setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
            )
        }
    }





    /**
     * 图文排版 通过SpannableString
     */
    fun createMultiTextPicBySpannableString() {

    }

    /**
     * 图文排版 通过Html
     */
    fun createMultiTextPicByHtml() {

    }

    /**
     * 通过基本布局
     */
    fun createMultiTextPicByHtmlLayout() {

    }





}