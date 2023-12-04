package uz.frodo.mydictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.frodo.mydictionary.databinding.Item2Binding
import uz.frodo.mydictionary.databinding.ItemBinding
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Word

class CategoryAdapter(val categoryClick: CategoryClick):ListAdapter<Category,CategoryAdapter.CategoryVH>(CDiffUtil()) {
    class CategoryVH(val binding: Item2Binding): RecyclerView.ViewHolder(binding.root)

    class CDiffUtil: DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = Item2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)
        holder.binding.item2Word.text = item.name
        holder.binding.item2Translation.visibility = View.GONE

        holder.binding.item2Image.setOnClickListener {
            categoryClick.click(item)
        }
    }
}