package com.example.p3tchild.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RadioButton
import android.widget.SeekBar
import com.example.p3tchild.Model.SearchParams
import com.example.p3tchild.R
import kotlinx.android.synthetic.main.activity_pet_finder_form.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

class PetFinderFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_finder_form)

        area_SeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                areaValueTextView.text = "$i/${area_SeekBar.max}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        time_SeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                timeValueTextView.text = "$i/${time_SeekBar.max}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

    }

    @UnstableDefault
    fun submit(view: View) {

        val searchParams = SearchParams()
        if(deficience_RadioGroup.checkedRadioButtonId == yes_RadioButton.id)
            searchParams.deficience = true

        val home = findViewById<RadioButton>(home_RadioGroup.checkedRadioButtonId)
        searchParams.home = home.text.toString()
        searchParams.area = area_SeekBar.progress
        searchParams.children = children_Switch.isChecked
        searchParams.allergic = allergic_Switch.isChecked
        searchParams.pets = pets_Switch.isChecked
        searchParams.time = time_SeekBar.progress

        val params = Json.stringify(SearchParams.serializer(), searchParams)

        val intent = Intent(this, PetFinderResultActivity::class.java)
        intent.putExtra("params", params)
        startActivity(intent)

    }
}
