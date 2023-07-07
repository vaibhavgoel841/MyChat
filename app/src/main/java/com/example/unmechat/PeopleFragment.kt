package com.example.unmechat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedList
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_people.*

val auth=FirebaseAuth.getInstance()

 class PeopleFragment : Fragment(){
lateinit var mAdapter: FirestorePagingAdapter<User,UserViewHolder>

  public   val database=FirebaseFirestore.getInstance().collection("users")
         .orderBy("name", Query.Direction.ASCENDING)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun setupAdapter(){

        val options=FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(database, PagingConfig(10,2,false) ,User::class.java)
            .build()
        mAdapter=object :FirestorePagingAdapter<User,UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
val view:View=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
            return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {

                holder.bind(model){ name: String, photo: String, id: String ->
                    if(auth.uid!=id) {
                        startActivity(
                            ChatActivity.createChatActivity(
                                requireContext(),
                                id,
                                name,
                                photo
                            )
                        )
                    }
                    else
                        Toast.makeText(requireContext(),"You cannot send message to yourself",Toast.LENGTH_LONG).show()
                }
            }

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAdapter()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcView.adapter=mAdapter
        rcView.layoutManager=LinearLayoutManager(requireContext())
    }





 }