package com.corvo.demo.base

import android.app.Activity
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.corvo.demo.App
import com.corvo.demo.R
import com.corvo.demo.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*


abstract class BaseActivity  : AppCompatActivity(){

    var cardColor: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as? App)?.let {
            it.baseActivity = this
        }



        resetTitles()
        setContentView(getLayout())
        setupViews()

    }



    fun initToaster(){
        close?.setOnClickListener {
            errorText?.text = ""
            toaster!!.animate()?.translationY(-toaster.height.toFloat())?.setDuration(300)?.start()
        }
    }
    fun showErrorMessage(msg : String?){

            if (msg?.isNotEmpty() == true && msg != "null"){
                toaster?.background = this.resources.getDrawable(R.color.colorRed)
                toaster?.animate()?.translationY(0f)?.setDuration(300)?.start()
                toaster?.setOnClickListener {

                }
                errorText?.text = msg ?: resources.getString(R.string.something_get_error)

                initToaster()
            }


    }
    fun showSuccessMessage(msg : String?){
        Log.d("asdddmessage"," ${msg}")

        if (msg?.isNotEmpty() == true && msg != "null"){
            toaster?.background = this.resources.getDrawable(R.color.colorSuccessGreen)
            toaster?.animate()?.translationY(0f)?.setDuration(300)?.start()
            toaster?.setOnClickListener {

            }
            errorText?.text = msg ?: resources.getString(R.string.something_get_error)
            initToaster()
        }
    }
    fun hideErrorMessage(){
        if(toaster?.height?.toFloat() ?: 0F > 0){
            errorText?.text = ""
            toaster?.animate()?.translationY(-toaster.height.toFloat())?.setDuration(300)?.start()
        }

    }




    fun pushRightToLeftFragment(layoutId : Int, fragment : BaseFragment, tag: String){
        hideKeyboard()
       // pref!!.setLastFragmentTag(tag)

        window.statusBarColor = resources.getColor(R.color.colorBlue)
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
            .add(layoutId, fragment)
            .addToBackStack(tag)
            .commit()

    }


    fun pushFragment(layoutId : Int, fragment : BaseFragment, tag : String, args: Bundle){
        hideKeyboard()
      //  pref!!.setLastFragmentTag(tag)
        fragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack("")

            .commit()

    }

    fun pushFragment(layoutId : Int, fragment : BaseFragment, tag : String, args: Bundle, status: String){
        hideKeyboard()
        //pref!!.setLastFragmentTag(tag)
        fragment.arguments = args
        if(status=="default"){
            window.statusBarColor = resources.getColor(R.color.colorBlue)
        }else if (status=="success"){
            window.statusBarColor = resources.getColor(R.color.colorSuccessGreen)
        }else if(status=="custom"){
            if (cardColor!=null){
                window.statusBarColor = this.cardColor!!
            }
        }
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack(tag)
            .commit()

        //Log.d("frag", tag)
    }

    fun pushFragment(layoutId : Int, fragment : BaseFragment, tag : String, status: String){
        hideKeyboard()
        //pref!!.setLastFragmentTag(tag)
        if(status=="default"){
            window.statusBarColor = resources.getColor(R.color.colorBlue)
        }else if (status=="success"){
            window.statusBarColor = resources.getColor(R.color.colorSuccessGreen)
        }else if(status=="custom"){
            if (cardColor!=null){
                window.statusBarColor = this.cardColor!!
            }
        }
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack("")
            .commit()

    }


    fun getResString(resId : Int) : String {
        return resources.getString(resId)
    }

    fun getIcon(resId : Int) : Drawable? {

        return resources.getDrawable(resId)
    }


    fun showBottomSheetDialog(title : String, list : List<String>, clickedListener : ((Int) -> Unit)){

        val dialog = BaseSimpleBottomSheetDialog()
        dialog.title = title
        dialog.items = list
        dialog.clicked = {

            position ->
            dialog.dismissAllowingStateLoss()
            clickedListener(position)
        }
        dialog.show(supportFragmentManager,"bottomsheet")
    }



    abstract fun getLayout() : Int
    abstract fun setupViews()


    override fun onBackPressed() {
        hideKeyboard()
        onBack()
        if (supportFragmentManager.findFragmentById(R.id.full_screen_container) is MainFragment){
           finish()
        }else super.onBackPressed()
    }

    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
                inputManager.hideSoftInputFromInputMethod(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }
    }

    fun hideSoftKeyboard(view: View?) {
        if (view != null) {
            val inputManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }






    fun hideKeyboard() {
        this.hideErrorMessage()

        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it

        if(true){

            view?.clearFocus()
            view?.isSelected = false
        }

        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    open fun resetTitles() {
        try {
            val info: ActivityInfo = packageManager.getActivityInfo(
                componentName,
                PackageManager.GET_META_DATA
            )
            if (info.labelRes != 0) {
                setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    fun onBack() {
        for (fragment in supportFragmentManager.fragments){
            for (childFragment in fragment.childFragmentManager.fragments){
                (childFragment as? BaseFragment)?.postOnBack()
            }
            (fragment as? BaseFragment)?.postOnBack()

        }
    }


}