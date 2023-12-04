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
import uz.frodo.mydictionary.databinding.FragmentMainInsideBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Dao
import uz.frodo.mydictionary.db.Word


private const val ARG_PARAM1 = "param1"


class MainInsideFragment : Fragment(), ItemClick {
    lateinit var binding: FragmentMainInsideBinding
    private var param1: Category? = null
    lateinit var dao: Dao
    var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Category
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainInsideBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()

        disposable.add(dao.getWordsByCategory(param1!!.id)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val adapter = MyAdapter(this)
                    adapter.submitList(it)
                    binding.rv.adapter = adapter
                },
                onError = {it.printStackTrace()},
                onComplete = { Toast.makeText(requireContext(), "${param1?.name} words", Toast.LENGTH_SHORT).show()}
            ))

        return binding.root
    }

    companion object {

        fun newInstance(param1: Category) =
            MainInsideFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }


    override fun Click(word: Word) {
        startActivity(Intent(requireContext(), InfoActivity::class.java).putExtra("word",word))
    }
}