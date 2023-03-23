package com.example.assignment3

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


class MyListFragment : Fragment() {
    // Initialize variables

    private lateinit var recyclerView: RecyclerView
    private lateinit var audioButton: Button
    private lateinit var headingTextView: TextView
    private lateinit var dataList: ArrayList<String>
    private lateinit var mediaPlayer: MediaPlayer


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_list, container, false)
        // Initialize UI elements

        recyclerView = view.findViewById(R.id.recyclerView)
        audioButton = view.findViewById(R.id.audioButton)
        headingTextView = view.findViewById(R.id.headingTextView)

        val audioUrl = arguments?.getString("audioUrl")
        val word = arguments?.getString("word")

        val mediaPlayer = MediaPlayer()


        val meanings = JSONArray(arguments?.getString("meanings"))
        Log.i(TAG, "meanings"+meanings)
        Log.i(TAG, "audioUrl: "+ audioUrl)

        headingTextView.text = word?.capitalize()

        audioButton.setOnClickListener {

            Log.i(TAG, "audioUrlaudioUrl: "+audioUrl)
            if (audioUrl!=null && audioUrl?.length!! >0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

                try {

                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(audioUrl)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        Toast.makeText(context, "Audio started playing..", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                        Toast.makeText(context, "Stopping Audio...", Toast.LENGTH_SHORT).show()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else{
                Toast.makeText(context, "No Audio Url", Toast.LENGTH_SHORT).show()
            }
        }
        dataList = ArrayList()

        for (i in 0 until meanings.length()) {
            val jsonObject = meanings.getJSONObject(i)
            val partOfSpeech = jsonObject.getString("partOfSpeech")
            dataList.add((i+1).toString() +". "+partOfSpeech.capitalize())
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MyListAdapter(dataList, object : MyListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, DetailsActivity::class.java)
                Log.i(TAG, "onItemClick: "+meanings[position].toString())
                intent.putExtra("details", meanings[position].toString())
                startActivity(intent)
            }
        })



        return view
    }



    // Adapter for RecyclerView
    private class MyListAdapter(private val dataList: ArrayList<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.part_of_speech_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = dataList[position]
            holder.textView.text = data
            holder.itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}
