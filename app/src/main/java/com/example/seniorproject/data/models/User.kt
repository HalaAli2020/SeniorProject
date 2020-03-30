package com.example.seniorproject.data.models


data class User(
    var username: String?, val email: String?, var uid: String?, var profileImageUrl: String?
) {
    constructor() : this("", "", "", "")

    var Posts = mutableListOf<Post>()
    //lateinit var Subscriptions : List<String>
    var Admin: Boolean = true
    var Subscriptions: HashMap<String, String> = hashMapOf()

    init {


    }

    fun toMap(): Map<String?, Any?> {
        return mapOf(
            "Username" to username,
            "email" to email,
            "uid" to uid,
            "profileImageUrl" to profileImageUrl,
            "Admin" to Admin,
            "Subscriptions" to Subscriptions,
            "Posts" to Posts

        )

    }

}