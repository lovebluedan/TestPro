package com.example.testpro.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import com.example.testpro.BaseApplication
import com.example.testpro.R
import com.example.testpro.base.BaseActivity
import com.example.testpro.base.BaseMVVMActivity
import com.example.testpro.databinding.ActivityScrollingBinding
import com.example.testpro.viewmodel.ScrollingViewModel
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : BaseMVVMActivity<ActivityScrollingBinding,ScrollingViewModel>() {

    override fun getLayout(): Int  = R.layout.activity_scrolling

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        mDataBinding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                BaseApplication.isShowFloatView = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
