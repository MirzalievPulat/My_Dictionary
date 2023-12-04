package uz.frodo.mydictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.mydictionary.databinding.ItemBinding
import uz.frodo.mydictionary.db.Word

class MyAdapter(var itemClick: ItemClick):ListAdapter<Word,MyAdapter.VH>(MyDiffUtil()) {
    class VH(val binding:ItemBinding):ViewHolder(binding.root)

    class MyDiffUtil:DiffUtil.ItemCallback<Word>(){
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.word.text = item.word
        holder.binding.translation.text = item.translation

        holder.binding.item.setOnClickListener {
            itemClick.Click(item)
        }
    }

}