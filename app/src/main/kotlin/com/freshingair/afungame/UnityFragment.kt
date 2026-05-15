package com.freshingair.afungame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unity3d.player.UnityPlayer

class UnityFragment : Fragment() {
    var view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUnityPlayer = UnityPlayer(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = mUnityPlayer!!.view
        if (requireView().parent != null) {
            (requireView().parent as ViewGroup).removeAllViews()
        }
        requireView().viewTreeObserver
            .addOnWindowFocusChangeListener { has: Boolean ->
                mUnityPlayer!!.windowFocusChanged(has)
            }
        return requireView()
    }

    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mUnityPlayer!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnityPlayer!!.onStop()
    }


    companion object {
        var mUnityPlayer: UnityPlayer? = null
    }
}