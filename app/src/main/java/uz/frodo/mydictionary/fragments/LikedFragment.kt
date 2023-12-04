package uz.frodo.mydictionary.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.mydictionary.InfoActivity
import uz.frodo.mydictionary.adapter.ItemClick
import uz.frodo.mydictionary.adapter.MyAdapter
import uz.frodo.mydictionary.databinding.FragmentLikedBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Dao
import uz.frodo.mydictionary.db.Word


class LikedFragment : Fragment(), ItemClick {
    lateinit var binding: FragmentLikedBinding
    lateinit var dao: Dao
    var disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikedBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()

        disposable.add(dao.getLikedWords()
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val adapter = MyAdapter(this)
                    adapter.submitList(it)
                    binding.rvLiked.adapter = adapter
                },
                onError = {it.printStackTrace()},
                onComplete = { Toast.makeText(requireContext(), "liked words", Toast.LENGTH_SHORT).show()}
            ))


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun Click(word: Word) {
        startActivity(Intent(requireContext(),InfoActivity::class.java).putExtra("word",word))
    }
}