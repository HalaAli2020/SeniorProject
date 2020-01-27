package com.example.seniorproject.MainForum


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.seniorproject.LoginActivity

import com.example.seniorproject.R
import com.example.seniorproject.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val logout_Button = view.findViewById<Button>(R.id.logout_button)
        logout_Button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
                /*.addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful*/
            //can't

                val intent = Intent(getActivity(), LoginActivity::class.java)
                startActivity(intent)
        }

        return view
    }

}
