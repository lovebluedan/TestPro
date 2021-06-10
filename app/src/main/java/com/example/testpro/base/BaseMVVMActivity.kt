package com.example.testpro.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testpro.BaseApplication
import com.example.testpro.view.MatchingFloatView
import java.lang.reflect.ParameterizedType

/**
 * @author 林丹荣
 * 创建日期：6/10/21
 * desc：
 */
abstract class BaseMVVMActivity<T : ViewDataBinding,M:BaseViewModel>  : BaseActivity()  {

    lateinit var matchingFloatView: MatchingFloatView
    lateinit var mDataBinding: T
    lateinit var mViewModel: M
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this,getLayout())
        createViewModel()
        matchingFloatView = MatchingFloatView(this)
    }

    /**
     * 获取viewModel
     */
    protected open fun createViewModel() : M?{
        val type = javaClass.genericSuperclass
        if (type != null && type is ParameterizedType){
            val actualTypeArguments =  type.actualTypeArguments
            val tClass = actualTypeArguments[1]
            return ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(tClass as Class<M>)
        }
        return null
    }

    abstract fun getLayout():Int

    override fun onResume() {
        super.onResume()
        if (BaseApplication.isShowFloatView) {
            if (!matchingFloatView.isShow) {
                matchingFloatView.show(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        matchingFloatView.hide()
    }

}