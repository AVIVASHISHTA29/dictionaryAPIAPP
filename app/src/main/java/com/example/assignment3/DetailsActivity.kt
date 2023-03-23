package com.example.assignment3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val fragment = DetailsFragment()
        val bundle = Bundle()
        bundle.putString("details", intent.getStringExtra("details"))

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }
}