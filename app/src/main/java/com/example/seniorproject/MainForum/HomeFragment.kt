package com.example.seniorproject.MainForum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Login.LoginActivity
import com.example.seniorproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.seniorproject.model.User
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main_forum.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.post_rv.*
import kotlinx.android.synthetic.main.post_rv.view.*
import kotlinx.android.synthetic.main.post_rv.view.post_title


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    companion object{
        var currentUser: User? = null
    }
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.post_recyclerView.adapter = adapter
        fetchCurrentUser()
        listenForPosts()



        view.new_post_btn.setOnClickListener {
            performNewPost()
        }


        return view
    }

    private fun listenForPosts(){
        val reference = FirebaseDatabase.getInstance().getReference("/posts")

        reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost = p0.getValue(com.example.seniorproject.model.Post::class.java)

                if(newPost!=null) {
                    Log.d("ForumACT", newPost?.text)
                    adapter.add(PostFrag(newPost.title, newPost.text))
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun performNewPost(){
        val title = new_post_title.text.toString()
        val text = new_post_text.text.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if(title.isNotEmpty() && text.isNotEmpty()) {
            val post = PostFrag(title, text)

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }
        }
    }

    private fun dummyData(view: View){
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(PostFrag("1", "TEXT1"))
        adapter.add(PostFrag("2", "TEXT2"))
        adapter.add(PostFrag("3", "TEXT3"))

        view.post_recyclerView.adapter = adapter
    }


    private fun fetchCurrentUser(){
        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.username}")
                val usernameForum = currentUser?.username
                username_forum.text = "Welcome " + usernameForum
            }
        })
    }



}



data class PostFrag(val title: String, val text: String): Item<GroupieViewHolder>(){
    constructor(): this("","")
    override fun getLayout(): Int {
        return R.layout.post_rv
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.post_text.text = text
        viewHolder.itemView.post_title.text = title
    }

}





