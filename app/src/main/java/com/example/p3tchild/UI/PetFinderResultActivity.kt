package com.example.p3tchild.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.isInvisible
import com.example.p3tchild.Model.Breed
import com.example.p3tchild.Model.SearchParams
import com.example.p3tchild.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pet_finder_result.*
import kotlinx.android.synthetic.main.list_item_breed.*
import kotlinx.serialization.json.Json

class PetFinderResultActivity : AppCompatActivity() {

    private lateinit var params: String
    private lateinit var db: FirebaseFirestore
    private lateinit var filteredList: List<Breed>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_finder_result)

        noResultsConstraintLayout.visibility = View.INVISIBLE

        params = intent.getStringExtra("params")!!.toString()
        db = FirebaseFirestore.getInstance()

        breedsListView.setOnItemClickListener{ parent, view, position, id ->
            openDetails(view)
        }

        getBreeds()
    }

    fun getBreeds(){

        val searchParams = Json.unquoted.parse(SearchParams.serializer(), params)
        val breeds = mutableListOf<Breed>()

        db.collection("Breed")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val breed = document.toObject(Breed::class.java)
                        Log.d(Constraints.TAG, document.id + " => " + breed)
                        breeds.add(breed)
                    }
                    filteredList = calculateScore(breeds, searchParams)
                    renderList(filteredList)
                } else {
                    Log.d(Constraints.TAG, "Error getting documents: ", task.exception)
                }
            }

    }

    private fun renderList(filteredList: List<Breed>) {
        explainTextView.text = "Esses são os Pets que mais combinam com você! Quanto maior o Score, mais vocês combinam!"
        progress_bar.visibility = View.GONE
        val list = filteredList.filter { it.Score!! > 0 }
        if(list.size > 0) {
            val adapter = BreedAdapter(baseContext, list)
            breedsListView.adapter = adapter
            breedsListView.divider = null
        }else{
            noResultsConstraintLayout.visibility = View.VISIBLE
        }
    }

    private fun calculateScore(breeds: List<Breed>, params: SearchParams): List<Breed> {
        var w = 2

        if(params.pets) w += 1
        if(params.children) w += 1
        if(params.deficience) w += 1

        breeds.forEach(){
            it.Score = (params.time!! * params.area!!) - ((it.MaxHeight!! * ((it.Aggressiviness!!*w) + (it.Behavior!!*w)))/5)
            if(params.allergic && it.Hypoallergenic)
                it.Score = it.Score!! + 100
        }

        val sortedBreeds = breeds.sortedByDescending { it.Score }

        return sortedBreeds
    }

    fun openDetails(view: View){
        val name = view.findViewById<TextView>(R.id.breedTextView).text.toString()

        val breed = filteredList.find { it.Name == name }
        val breedString = Json.stringify(Breed.serializer(), breed!!)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val breedDetailsFragment = BreedDetailsFragment()
        val bundle = Bundle()
        bundle.putString("breed",breedString)
        breedDetailsFragment.arguments = bundle
        ft.add(R.id.fragment_content, breedDetailsFragment)
        ft.commit()
    }
}
