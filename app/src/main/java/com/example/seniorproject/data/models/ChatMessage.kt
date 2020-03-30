package com.example.seniorproject.data.models

data class ChatMessage(val id: String, var text: String?, val fromID: String, val toId: String, val timestamp: Long){
    //constructor(): this("","","","",-1)
}