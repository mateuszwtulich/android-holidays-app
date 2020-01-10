package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.programowanieaplikacjimultimedialnych.R


class MainActivity : AppCompatActivity(),MainFragment.MainFragmentListner,PostFragment.PostFragmentListner{

    private val fragment =  MainFragment.newInstance()

    override fun updateMF() {}

    override fun updatePF(bundle: Bundle) {
        Log.d("Main activity:" ,bundle.toString())
        fragment.updateData(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }

    fun addFragment(fragment: Fragment){
       supportFragmentManager.beginTransaction()
           .add(R.id.fragment_container, fragment)
           .addToBackStack(null)
           .commit()
    }

    fun replaceFragmentWithAnimation(fragment: Fragment,view :View, sharedElementName: String){
        supportFragmentManager
            .beginTransaction()
            .addSharedElement(view, sharedElementName)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

}

/*
Animacja działa ale jak zrobić czekanie na załadowanie się viewPagerów przy przejściu
Gdy działa animacja nie można ustawić pozycji viewpager podczas powrotu z posta
Shared element

*/
/*
TODO
Cień / chowanie się serachbara
Zrobienia zdjęcia i dodanie go do lub przeniesienie go do tworzenia nowego posta

Mati : Wywala błąd przy description jak da się za dużo znaków nowej lini
 */
