package com.example.pokedex.views.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedex.BaseFragment
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.databinding.FragmentPokedexBinding
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.utils.NetworkMonitorUtil
import com.example.pokedex.utils.observe

private const val ARG_ID = "id"

/**
 * A simple [Fragment] subclass.
 * Use the [PokemonDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonDetailFragment : BaseFragment() {
    private var pokemonId: Int = 0
    private lateinit var viewModel: PokemonDetailViewModel
    private lateinit var binding: FragmentPokemonDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonId = it.getInt(ARG_ID)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(PokemonDetailViewModel::class.java)
        viewModel.load(pokemonId)
        binding = FragmentPokemonDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initObservers()
        return binding.root
    }
    fun initObservers(){
        observe(viewModel.exceptionOnData()){
            onExceptionData(it)
        }
        observe(viewModel.errorOnData()){
            onErrorData(it)
        }
        observe(viewModel.errorOnNetworkData()){
            onExceptionData(it)
        }
        observe(viewModel.pokemon){
            if (it is Pokemon){
                Glide.with( binding.pokemonDetailImageView.context).load(it.coverImage)
                    .into( binding.pokemonDetailImageView)
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pokemonId Id of pokemon.
         * @return A new instance of fragment PokemonDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pokemonId: Int) =
                PokemonDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ID, pokemonId)
                    }
                }
    }
}