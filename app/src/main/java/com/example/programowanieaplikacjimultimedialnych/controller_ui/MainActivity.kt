
package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(),PostFragment.PostFragmentListner {

    private val fragment = MainFragment.newInstance()


    override fun updatePF(bundle: Bundle) {
        Log.d("Main activity:", bundle.toString())
        fragment.updateData(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        replaceFragment(MainFragment.newInstance())
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(com.example.programowanieaplikacjimultimedialnych.R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragmentWithAnimation(fragment: Fragment, view: View, sharedElementName: String) {
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(view, sharedElementName)
            .replace(com.example.programowanieaplikacjimultimedialnych.R.id.fragment_container, fragment)
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) this.finish()
        else supportFragmentManager.popBackStack()
    }

}
/*
Animacja działa ale jak zrobić czekanie na załadowanie się viewPagerów przy przejściu
Gdy działa animacja nie można ustawić pozycji viewpager podczas powrotu z posta
Shared element
OnPreDraw znajduje view pagera ale nie jego dzieci !!!

*/
/*
TODO
Cień / chowanie się serachbara
Zrobienia zdjęcia i dodanie go do lub przeniesienie go do tworzenia nowego posta

Mati : Wywala błąd przy description jak da się za dużo znaków nowej lini
 */


