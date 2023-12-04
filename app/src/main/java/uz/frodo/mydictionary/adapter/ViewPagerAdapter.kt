package uz.frodo.mydictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.fragments.MainInsideFragment

class ViewPagerAdapter(fa:FragmentActivity,var list: List<Category>):FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return MainInsideFragment.newInstance(list[position])
    }
}