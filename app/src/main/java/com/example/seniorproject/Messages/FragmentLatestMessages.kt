package com.example.seniorproject.Messages

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.LatestMessageAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.models.LatestMessage
import com.example.seniorproject.viewModels.MessagesFragmentViewModel
import kotlinx.android.synthetic.main.m_fragment_latest_messages.*
import kotlinx.android.synthetic.main.m_fragment_latest_messages.view.*
import javax.inject.Inject

class FragmentLatestMessages : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: MessagesFragmentViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        activity?.title = "Messages"
        val view = inflater.inflate(R.layout.m_fragment_latest_messages, container, false)
        DaggerAppComponent.create().inject(this)

        myViewModel = ViewModelProvider(this, factory).get(MessagesFragmentViewModel::class.java)

        view.recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages().observe(viewLifecycleOwner, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                view.recyclerView_latest_messages.adapter?.notifyDataSetChanged()
                view.recyclerView_latest_messages.adapter  = LatestMessageAdapter(view.context, messages)
            }
        })


        return view

    }

    override fun onResume() {
        super.onResume()

        recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages().observe(this, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                recyclerView_latest_messages.adapter  =
                    context?.let { LatestMessageAdapter(it, messages) }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.fragment_new_message_menu, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.newMessage -> {
                val intent = Intent(context, NewMessage::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
