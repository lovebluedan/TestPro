package com.example.testpro

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

/**
 * @author 林丹荣
 * 创建日期：6/10/21
 * desc：
 */
class BaseApplication : Application() {

    companion object{
        private const val TAG = "BaseApplication"
        var isShowFloatView = false
    }
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        Log.d(TAG, "onCreate: ${ MMKV.getRootDir()}")
    }

}