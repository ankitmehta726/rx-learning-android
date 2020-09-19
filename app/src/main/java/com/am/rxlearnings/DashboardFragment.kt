package com.am.rxlearnings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAsyncTask.setOnClickListener {
            navigateToAsyncFragment()
        }
    }

    // Navigate to the AsyncFragment using navigation controller actions defined in the navigation
    // graph.
    private fun navigateToAsyncFragment(){
        Navigation.findNavController(view!!)
            .navigate(R.id.action_dashboardFragment_to_asyncFragment)
    }
}