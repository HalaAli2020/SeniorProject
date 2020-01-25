package com.example.seniorproject.model

class User(private val username: String, private val email: String, private val password: String)
{
    private val userID : Int?

    init {
        userID = backendID()
    }
    fun getID(): Int? {
        return userID
    }
    fun getUserName(): String{
        return username
    }
    fun getemail(): String{
        return email
    }
    fun getpass() : String{
        return password
    }
    private fun backendID() : Int
    {
        // function will use backend to get user id
        return 1
    }


}
