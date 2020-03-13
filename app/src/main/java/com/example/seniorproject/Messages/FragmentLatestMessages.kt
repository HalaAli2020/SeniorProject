package com.example.seniorproject.Messages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ListViewModel
import javax.inject.Inject
import androidx.lifecycle.*
import com.example.seniorproject.viewModels.MessagesFragmentViewModel
import kotlinx.android.synthetic.main.m_fragment_latest_messages.view.*

class FragmentLatestMessages : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: MessagesFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "Messages"
        val view = inflater.inflate(R.layout.m_fragment_latest_messages, container, false)
        val factory = InjectorUtils.provideMessagesFragmentViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(MessagesFragmentViewModel::class.java)

        view.new_message_btn123.setOnClickListener {
            val intent = Intent(context, NewMessage::class.java)
            startActivity(intent)
        }

        return view

    }

}
