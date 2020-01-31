package com.example.seniorproject.data

data class User(val username: String?, val email: String?, val uid: String?)
{
    constructor(): this("","","")

    private fun backendID() : Int
    {
        // function will use backend to get user id
        return 1
    }


}
