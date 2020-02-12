package com.example.seniorproject

import android.util.Log
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.viewModels.AuthenticationViewModel
import org.junit.Test
import org.junit.Assert.*
import io.mockk.*
import org.junit.After
import org.junit.Before

class LoginActivityTest {

    //private val view: LoginActivity = mockk()

    object MockObj {
        fun add(a: Int, b: Int) = a + b
    }

    @Before
    fun beforeTests() {
        mockkObject(MockObj)
        every { MockObj.add(1,2) } returns 55
    }

    @Test
    fun willUseMockBehaviour() {
        assertEquals(55, MockObj.add(1,2))
    }

    @After
    fun afterTests() {
        unmockkAll()
        // or unmockkObject(MockObj)
    }


}