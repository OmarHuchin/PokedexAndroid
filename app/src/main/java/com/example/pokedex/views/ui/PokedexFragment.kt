package com.example.pokedex.views.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.databinding.FragmentPokedexBinding
import com.example.pokedex.libraries.EndlessRecyclerOnScrollListener

import com.example.pokedex.utils.HomeErrorCodes
import com.example.pokedex.utils.NetworkMonitorUtil
import com.example.pokedex.utils.observe
import com.example.pokedex.views.adapters.PokemonListAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [PokedexFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokedexFragment : Fragment() {
    private lateinit var networkMonitor: NetworkMonitorUtil
    private lateinit var viewModel: PokedexViewModel
    private lateinit var binding: FragmentPokedexBinding
    private lateinit var adapter: PokemonListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(PokedexViewModel::class.java)

        binding = FragmentPokedexBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        networkMonitor  = NetworkMonitorUtil(requireContext())
        setUpUI()
        setListeners()
        return binding.root
    }

    private fun performRepositoryCall(view: View? = null){
        if (view != null) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)
        }
        viewModel.performRepositoryCall()
    }
    fun setUpUI() {
        adapter = PokemonListAdapter {
            val p = it
            if (p.id == 0) return@PokemonListAdapter
            viewLifecycleOwner.run{
               val action = PokedexFragmentDirections.actionPokedexFragmentToPokemonDetailFragment(p.id)
                view?.findNavController()?.navigate(action)
            }
        }
        //val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        //binding.pokemonsRecyclerView.layoutManager = manager
        binding.pokemonsRecyclerView.adapter = adapter
        val scrollListener : RecyclerView.OnScrollListener = object : EndlessRecyclerOnScrollListener(){
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        }
        binding.pokemonsRecyclerView.addOnScrollListener(scrollListener)
    }
    fun setListeners() {
        viewModel.currentPokemonList.observe(viewLifecycleOwner){
           adapter.submitList(it)
        }
    }
    fun onValidationError(errors: List<HomeErrorCodes>) {

    }
    fun onSuccessNetworkData(data: Any?) {
        if(data is Pair<*, *> && data.first is Int)
            adapter.notifyItemChanged(data.first as Int)
        Log.e("Data",data?.toString() ?: String())
    }
    override fun onResume() {
        super.onResume()
        networkMonitor.register()

    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.successOnData()){
            d->
            onSuccessNetworkData(d)
        }
        observe(viewModel.exceptionOnData()){

        }
        observe(viewModel.errorOnData()){

        }
        observe(viewModel.errorOnNetworkData()){

        }
        binding.iconProfile.setOnClickListener {
            viewLifecycleOwner.run{
                view.findNavController().navigate(R.id.action_pokedexFragment_to_profileFragment)
            }
        }
        networkMonitor.result = {
            isAvailable, type ->
            viewLifecycleOwner.run {
                viewModel.loadData(isAvailable)
            }
        }
    }
}