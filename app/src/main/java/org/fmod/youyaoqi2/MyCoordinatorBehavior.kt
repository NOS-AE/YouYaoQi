package org.fmod.youyaoqi2

import android.animation.Animator
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.MotionSpec
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.DescendantOffsetUtils
import org.fmod.youyaoqi2.R
import kotlin.math.IEEErem
import kotlin.math.log

class MyCoordinatorBehavior<T: View>: CoordinatorLayout.Behavior<T>{
    private var isAnimated = false
    private var stopAlphaPosition = 0f
    private var startAlphaPosition = 0f
    private var alphaDistance = 0f
    private var animator: ViewPropertyAnimator? = null
    private var isShowing = false

    private val interpolator = LinearInterpolator()

    constructor():super()
    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet)

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        val translationY = dependency.top.toFloat()
        val tran = Math.abs(translationY)

        if(tran >= stopAlphaPosition && isShowing){
            hide(child)
        }else if(tran < stopAlphaPosition && !isShowing){
            show(child)
        }

        /*if(tran > startAlphaPosition && tran < stopAlphaPosition){
            child.alpha = 1 - (tran - startAlphaPosition) / alphaDistance
        }*/
        child.translationY = translationY
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        /*if(!isAnimated){
            if(dy >= 0 && child.visibility == View.VISIBLE){
                hide(child)
            }else if(dy <= 0 && child.visibility == View.GONE){
                show(child)
            }
        }*/
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        return if(dependency is AppBarLayout){
            if(dependency.height != 0) {
                stopAlphaPosition = dependency.height.toFloat() * 2 / 3
                startAlphaPosition = dependency.height.toFloat() * 1 / 4
                alphaDistance = stopAlphaPosition - startAlphaPosition
            }
            true
        }else false
    }

    private fun hide(v: View){
        isShowing = false
        //if(v.visibility == View.INVISIBLE)
        //    return
        animator?.cancel()
        animator = v.animate().alpha(0f).setInterpolator(interpolator).setDuration(200).setListener(object :MyAnimatorListener() {
            override fun onAnimationStart(animation: Animator?) {
                isAnimated = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                //v.visibility = View.INVISIBLE
                isAnimated = false
            }
        })
        animator?.start()
    }

    private fun show(v: View){
        isShowing = true
        //if(v.visibility == View.VISIBLE)
         //   return
        animator?.cancel()
        animator = v.animate().alpha(1f).setInterpolator(interpolator).setDuration(200).setListener(object :MyAnimatorListener(){
            override fun onAnimationStart(animation: Animator?) {
            //    v.visibility = View.VISIBLE
                isAnimated = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimated = false
            }
        })
        animator?.start()
    }
}