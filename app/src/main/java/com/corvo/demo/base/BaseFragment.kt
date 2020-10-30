package com.corvo.demo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.corvo.demo.MainActivity

abstract class BaseFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(getLayout(),container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }



    fun getBaseActivity(run : (BaseActivity) -> (Unit)) {

        (activity as? BaseActivity).let {
            it?.let { it1 ->
                run(it1)
            }
        }
    }
    abstract fun getLayout() : Int
    abstract fun setupViews()
    abstract fun getTitle() : String
    open fun updateCashedCardList() {}

    open fun postOnBack() {}



}