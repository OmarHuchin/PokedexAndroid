package com.example.pokedex.views.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.BaseFragment
import com.example.pokedex.HomeActivity
import com.example.pokedex.LoginActivity
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokedexBinding
import com.example.pokedex.databinding.FragmentProfileBinding
import com.example.pokedex.utils.startActivityNewRoot
import com.example.pokedex.views.CustomAlertDialog

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initObservers()
        return binding.root
    }
    fun initObservers(){
        viewModel.successOnData().observe(viewLifecycleOwner){
            val positive = requireContext().getString(R.string.button_accept)
            val negative = requireContext().getString(R.string.button_cancel)
            val title = requireContext().getString(R.string.close_session_title)
            val message = requireContext().getString(R.string.close_session_message)
            val alert = createAlert(requireActivity(),title,message)
            alert.setPositiveButton(positive){
                viewModel.logout()
                requireActivity().startActivityNewRoot<LoginActivity> {  }
            }
            alert.setNegativeButton(negative){}
            alert.show()
        }
    }
}