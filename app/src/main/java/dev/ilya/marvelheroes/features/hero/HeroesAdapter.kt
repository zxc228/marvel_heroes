package dev.ilya.marvelheroes.features.hero

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.ilya.marvelheroes.R
import dev.ilya.marvelheroes.databinding.CellHeroBinding

class HeroesAdapter(
    val callback :(Hero)->Unit
) : RecyclerView.Adapter<HeroesAdapter.HeroViewHolder>(){
    private var items :List<Hero> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder =
        parent.context
            .let(LayoutInflater::from)
            .let { CellHeroBinding.inflate(it, parent, false) }
            .let(::HeroViewHolder)
            .apply {
                binding.root.setOnClickListener {
                    callback(items[adapterPosition])
                }
            }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val item = items[position]
        holder.binding.name.text = item.name
        Glide
            .with(holder.binding.image)
            .load(item.images.md)
            .placeholder(R.drawable.no_portrait)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onViewRecycled(holder: HeroViewHolder) {
        super.onViewRecycled(holder)
        Glide
            .with(holder.binding.image)
            .clear(holder.binding.image)
    }

    fun setItems(items :List<Hero>){
        this.items = items
        notifyDataSetChanged()
    }

    class HeroViewHolder(val binding :CellHeroBinding) :RecyclerView.ViewHolder(binding.root)
}