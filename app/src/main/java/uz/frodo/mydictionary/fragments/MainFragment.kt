package uz.frodo.mydictionary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.mydictionary.R
import uz.frodo.mydictionary.adapter.ViewPagerAdapter
import uz.frodo.mydictionary.databinding.FragmentMainBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Dao


class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var dao: Dao
    var disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()

        disposables.add(dao.getAllCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    binding.viewPager.adapter = ViewPagerAdapter(requireActivity(),it)
                    TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
                        tab.text = it[position].name
                    }.attach()
                },
                onError = { println(it) },
                onComplete = {  }
            ))

        return binding.root
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}