package com.corvo.demo

import android.os.Bundle
import com.corvo.demo.base.BaseActivity
import com.corvo.demo.fragment.MainFragment
import com.corvo.demo.fragment.TaskAddOrEditFragment
import com.corvo.demo.helper.Constants.Companion.EDIT_KEY

class MainActivity : BaseActivity() {


    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun setupViews() {
        val fragment = MainFragment()
        pushRightToLeftFragment(R.id.full_screen_container, fragment, "MainFragment")
    }
}