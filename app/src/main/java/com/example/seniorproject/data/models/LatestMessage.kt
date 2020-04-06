package com.example.seniorproject.data.models

import android.net.Uri


data class LatestMessage(val id: String?, var text: String?, val fromID: String?, val toId: String?, var username: String?, val timestamp: Long){
    constructor(): this("","","","","",-1)
}