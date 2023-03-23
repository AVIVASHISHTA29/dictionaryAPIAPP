package com.example.assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val fragment = MyListFragment()
        val bundle = Bundle()
        bundle.putString("audioUrl", intent.getStringExtra("audioUrl"))
        bundle.putString("word", intent.getStringExtra("word"))
        bundle.putString("meanings", intent.getStringExtra("meanings"))

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.list_container, fragment)
            .commit()


    }
}