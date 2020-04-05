package com.example.seniorproject.data.models

//model class for an application user
data class User(
    var username: String?, val email: String?, var uid: String?, var profileImageUrl: String?
) {
    constructor() : this("", "", "", "")

    var posts = mutableListOf<Post>()
    private var admin: Boolean = true
    private var subscriptions: HashMap<String, String> = hashMapOf()

    //mapping of user data to the firebase database
    fun toMap(): Map<String?, Any?> {
        return mapOf(
            "Username" to username,
            "email" to email,
            "uid" to uid,
            "profileImageUrl" to profileImageUrl,
            "Admin" to admin,
            "Subscriptions" to subscriptions,
            "Posts" to posts

        )

    }

}