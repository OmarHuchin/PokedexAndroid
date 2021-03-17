package com.example.pokedex.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.data.models.Pokemon


class PokemonListAdapter(private val listener: (Pokemon) -> Unit):androidx.recyclerview.widget.ListAdapter<Pokemon, PokemonListAdapter.ViewHolder>(
    DiffUtilCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_list, parent, false)
        return ViewHolder((view))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),listener)
    }
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var titleTextView:AppCompatTextView
        private var coverImageView:ImageView
        private var cardView: CardView

        init {
            coverImageView = view.findViewById(R.id.pokemon_imageView)
            titleTextView = view.findViewById(R.id.pokemon_title_textView)
            cardView = view.findViewById(R.id.cardView)
        }
        fun bind(pokemon: Pokemon, listener: (Pokemon) -> Unit){
            titleTextView.text = pokemon.name
            cardView.setOnClickListener {
                listener(pokemon)
            }
            if(!pokemon.coverImage.isNullOrEmpty()){ //!pokemon.sprites?.front_default.isNullOrEmpty()
                val circularProgressDrawable = CircularProgressDrawable(coverImageView.context)
                circularProgressDrawable.backgroundColor = R.color.primaryColor
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()
                Glide.with(coverImageView.context).load(pokemon.coverImage)
                    .into(coverImageView)
            }

        }
    }


}
private object DiffUtilCallback:DiffUtil.ItemCallback<Pokemon>(){
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem == newItem

}