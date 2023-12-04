package uz.frodo.mydictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.frodo.mydictionary.databinding.Item2Binding
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Word

class WordAdapter(val itemClick: ItemClick):ListAdapter<Word,WordAdapter.WordVH>(WDiffUtil()) {
    class WordVH(val binding: Item2Binding): RecyclerView.ViewHolder(binding.root)

    class WDiffUtil: DiffUtil.ItemCallback<Word>(){
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordVH {
        val binding = Item2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WordVH(binding)
    }

    override fun onBindViewHolder(holder: WordVH, position: Int) {
        val item = getItem(position)
        holder.binding.item2Word.text = item.word
        holder.binding.item2Translation.text = item.translation

        holder.binding.item2Image.setOnClickListener {
            itemClick.Click(item)
        }
    }
}