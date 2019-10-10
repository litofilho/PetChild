package com.example.p3tchild.UI

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.p3tchild.Model.Breed
import com.example.p3tchild.R
import kotlinx.android.synthetic.main.fragment_breed_details.*
import kotlinx.serialization.json.Json

class BreedDetailsFragment : Fragment() {

    private lateinit var breedString: String
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            breedString = it.getString("breed")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_breed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val breed = Json.unquoted.parse(Breed.serializer(), breedString)

        breedTextViewFragment.text = breed.Name
        resumeTextViewFragment.text = breed.Resume

        closeButtonFragment.setOnClickListener { view ->
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BreedDetailsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
