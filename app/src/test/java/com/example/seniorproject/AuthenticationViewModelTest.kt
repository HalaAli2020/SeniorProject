package com.example.seniorproject

import android.net.Uri
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.repositories.UserAuthRepo
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.Factories.DaggerViewModelFactory
import org.junit.Test
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject


class AuthenticationViewModelTest {

    // lateinit var viewModel: AuthenticationViewModel




    //private val view: LoginActivity = mockk()

    var username: String? = "John"
    var profileImageUrl: Uri? = Uri.EMPTY

    var authListener: AuthenticationListener? = null

    private val disposables = CompositeDisposable()

    @MockK
    val factory= mockk<DaggerViewModelFactory>()

    val firecheck= mockkStatic(FirebaseData::class)


    var firebaseData: FirebaseData = mockk<FirebaseData>() //fails

    val repos= mockk<UserAuthRepo>()
//    var viewModel= mockkObject(AuthenticationViewModel(repo))
    // val repospy=spyk(UserAuthRepo(firebasespy))

    var email: String? = "null"
    var password: String? = "null"

    //check behavior for login function for view model. See if authlistener was called

    @Test
    fun `verify current user function for repo`()
    {

        val firemock =mockkClass(FirebaseData::class)

        every { firemock.CurrentUser()} returns mockk(relaxed = true)

        val repobehavior: BehaviorSubject<AuthenticationViewModel>


        verify(exactly=0){
            //firemock.LoginUser(email?: "null", password?: "null")

        }

        confirmVerified(firemock)
        //check subscribed calls from view model


        //assertNotEquals(firebaseData, firecheck)
        //every { firemock.CurrentUser())} returns mockk(relaxed = true)

       /*verify{
           //firemock.CurrentUser()


       }*/


        //confirmVerified(firemock)
        //Subject(repospy)
        //verify(exactly = 1){
        //    assertNotNull(firecheck.currentUser())
        // }
        //assertNull(repo.login(email?:"null", password?:"null"))

    }

    /*fun registerTest()
    {
        assertEquals(repo.register(username?:"John",email?:"john@gmail.com",password?:"password",
            profileImageUrl?: Uri.EMPTY))

    }*/


    // or unmockkObject(MockObj)




}