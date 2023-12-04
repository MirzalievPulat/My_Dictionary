package uz.frodo.mydictionary.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.mydictionary.AddActivity
import uz.frodo.mydictionary.R
import uz.frodo.mydictionary.adapter.CategoryAdapter
import uz.frodo.mydictionary.adapter.ItemClick
import uz.frodo.mydictionary.adapter.WordAdapter
import uz.frodo.mydictionary.databinding.DeleteDialogBinding
import uz.frodo.mydictionary.databinding.EtidDialogBinding
import uz.frodo.mydictionary.databinding.FirstDialogBinding
import uz.frodo.mydictionary.databinding.FragmentWordBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Dao
import uz.frodo.mydictionary.db.Word


class WordFragment : Fragment(),ItemClick {
   lateinit var binding: FragmentWordBinding
    var disposable = CompositeDisposable()
    lateinit var dao: Dao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordBinding.inflate(layoutInflater)

        dao = AppDatabase.getInstance(requireContext()).dao()

        disposable.add(dao.getAllWords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val adapter = WordAdapter(this)
                    adapter.submitList(it)
                    binding.wordRv.adapter = adapter
                }
            ))


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun Click(word: Word) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val first = FirstDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setView(first.root)
        first.firstChange.setOnClickListener {

            startActivity(Intent(requireContext(),AddActivity::class.java).putExtra("word",word))
            dialog.dismiss()
        }
        first.firstDelete.setOnClickListener {
            val d = AlertDialog.Builder(requireContext()).create()
            val delete = DeleteDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            d.setView(delete.root)
            delete.yes.setOnClickListener {

                dao.deleteWord(word)
                d.dismiss()

            }

            delete.no.setOnClickListener {
                d.dismiss()
            }
            d.show()

            dialog.dismiss()
        }
        dialog.show()
    }
}