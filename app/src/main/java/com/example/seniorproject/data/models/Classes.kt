package com.example.seniorproject.data.models

import android.net.Uri


//CRN class
class CRN(var name : String, var communityImage: Uri?)
{
    constructor(): this("", Uri.EMPTY)
    var subscribed : Boolean = false
}