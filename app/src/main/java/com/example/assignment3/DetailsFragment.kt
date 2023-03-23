package com.example.assignment3

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class DetailsFragment : Fragment() {
    // Initialize variables
    private lateinit var recyclerViewDefinitions: RecyclerView
    private lateinit var recyclerViewDefinitionExamples: RecyclerView
    private lateinit var recyclerViewSynonyms: RecyclerView
    private lateinit var recyclerViewAntonnyms: RecyclerView
    private lateinit var PartOfSpeech: TextView
    private lateinit var DefinitionsHeading: TextView
    private lateinit var DefinitionExamples: TextView
    private lateinit var SynonymsHeading: TextView
    private lateinit var AntonymsHeading: TextView
    private lateinit var dataList: ArrayList<JSONObject>
    private lateinit var dataList2: ArrayList<String>
    private lateinit var dataList3: ArrayList<String>
    private lateinit var dataList4: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        // Initialize UI elements
        recyclerViewDefinitions = view.findViewById(R.id.rv_definitions)
        recyclerViewDefinitionExamples = view.findViewById(R.id.rv_definition_examples)
        recyclerViewSynonyms = view.findViewById(R.id.rv_synonyms)
        recyclerViewAntonnyms = view.findViewById(R.id.rv_antonyms)

        PartOfSpeech = view.findViewById(R.id.tv_part_of_speech)
        DefinitionsHeading = view.findViewById(R.id.tv_definitions)
        DefinitionExamples = view.findViewById(R.id.tv_definition_examples)
        SynonymsHeading = view.findViewById(R.id.tv_synonyms)
        AntonymsHeading = view.findViewById(R.id.tv_antonyms)
        // Set text for TextViews
        val details = JSONObject(arguments?.getString("details"))
        val definitions = details.getJSONArray("definitions")
        val synonyms = details.getJSONArray("synonyms")
        val antonyms = details.getJSONArray("antonyms")
        Log.i(TAG, "antonyms: $antonyms")

        PartOfSpeech.text = "${details.getString("partOfSpeech").capitalize()}"
        DefinitionsHeading.text = "Definitions"

        if(definitions.length()>0){
            dataList = ArrayList()

            for (i in 0 until definitions.length()) {
                val jsonObject = definitions.getJSONObject(i)
                val definition = jsonObject.getString("definition")
                dataList.add(jsonObject)
            }


            recyclerViewDefinitions.layoutManager = LinearLayoutManager(activity)
            recyclerViewDefinitions.adapter = DetailsFragment.DetailsAdapter2(
                dataList,
            )


        }


        if(synonyms.length()>0){
            SynonymsHeading.text = "Synonyms"
            dataList2 = ArrayList()

            for (i in 0 until synonyms.length()) {
                val item = synonyms.getString(i)
                dataList2.add((i+1).toString() +". "+item.capitalize())
            }

            recyclerViewSynonyms.layoutManager = LinearLayoutManager(activity)
            recyclerViewSynonyms.adapter = DetailsFragment.DetailsAdapter(
                dataList2,
                object : DetailsFragment.DetailsAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.i(ContentValues.TAG, "onItemClick: " + definitions[position].toString())
                    }
                })

        }

        if(antonyms.length()>0) {

            AntonymsHeading.text = "Antonyms"
            dataList3 = ArrayList()

            for (i in 0 until antonyms.length()) {
                val item = antonyms.getString(i)
                dataList3.add((i + 1).toString() + ". " + item.capitalize())
            }

            recyclerViewAntonnyms.layoutManager = LinearLayoutManager(activity)
            recyclerViewAntonnyms.adapter = DetailsFragment.DetailsAdapter(
                dataList3,
                object : DetailsFragment.DetailsAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.i(ContentValues.TAG, "onItemClick: " + definitions[position].toString())
                    }
                })


        }


        return view
    }

     class DetailsAdapter(private val dataList: ArrayList<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<DetailsFragment.DetailsAdapter.ViewHolder>() {
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item, parent, false)
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

    class DetailsAdapter2(private val dataList: ArrayList<JSONObject>) : RecyclerView.Adapter<DetailsFragment.DetailsAdapter2.ViewHolder>() {


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv_definition: TextView = itemView.findViewById(R.id.textView)
            val tv_example: TextView = itemView.findViewById(R.id.tv_example)
            val tv_synonyms: TextView = itemView.findViewById(R.id.tv_synonyms)
            val tv_antonyms: TextView = itemView.findViewById(R.id.tv_antonyms)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.definition_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = dataList[position]
            holder.tv_definition.text = (position+1).toString()+". " +data.getString("definition")
            if(data.has("example")){
                holder.tv_example.text = "Example: "+ data.getString("example")
            } else{
                holder.tv_example.visibility=View.GONE
            }
            if (data.getJSONArray("synonyms").length()>0){
                val synonyms = data.getJSONArray("synonyms")

                val list = mutableListOf<String>()
                for (i in 0 until synonyms.length()) {
                    list.add(synonyms.getString(i))
                }

                val joinedString = list.joinToString(", ")
                holder.tv_synonyms.text = "Synonyms: "+ joinedString
            }
            else{
                holder.tv_synonyms.visibility=View.GONE
            }

            if (data.getJSONArray("antonyms").length()>0){
                val antonyms = data.getJSONArray("antonyms")

                val list = mutableListOf<String>()
                for (i in 0 until antonyms.length()) {
                    list.add(antonyms.getString(i))
                }

                val joinedString = list.joinToString(", ")
                holder.tv_antonyms.text = "Antonyms: "+ joinedString
            }
            else{
                holder.tv_antonyms.visibility = View.GONE
            }


        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }


    companion object {
        fun newInstance(data: String): DetailsFragment {
            val args = Bundle()
            args.putString("data",data)
            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
