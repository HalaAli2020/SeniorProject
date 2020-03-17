package com.example.seniorproject.Messages

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.Nullable
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ListViewModel
import javax.inject.Inject
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Adapters.LatestMessageAdapter
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.data.models.LatestMessage
import com.example.seniorproject.viewModels.MessagesFragmentViewModel
import kotlinx.android.synthetic.main.m_fragment_latest_messages.*
import kotlinx.android.synthetic.main.m_fragment_latest_messages.view.*
import kotlinx.android.synthetic.main.m_fragment_latest_messages.view.recyclerView_latest_messages

class FragmentLatestMessages : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: MessagesFragmentViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        activity?.title = "Messages"
        val view = inflater.inflate(R.layout.m_fragment_latest_messages, container, false)
        val factory = InjectorUtils.provideMessagesFragmentViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(MessagesFragmentViewModel::class.java)

        view.new_message_btn123.setOnClickListener {
            val intent = Intent(context, NewMessage::class.java)
            startActivity(intent)
        }

        view.recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages()?.observe(this, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                view.recyclerView_latest_messages.adapter  = LatestMessageAdapter(view.context, messages)
            }
        })


        return view

    }

    override fun onResume() {
        super.onResume()


        recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages()?.observe(this, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                recyclerView_latest_messages.adapter  =
                    context?.let { LatestMessageAdapter(it, messages) }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_new_message_menu, menu)
    }

}
