package com.learning.assignmentocbc

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkUsernameAlertMessageAppearAtTheStart(){
        onView(withId(R.id.loginUsernameAlert)).check(matches(withText("Username is required")))
    }

    @Test
    fun checkUsernameAlertMessageChangedUponTyping(){
        onView(withId(R.id.loginUsername)).perform(typeText("a"))
        onView(withId(R.id.loginUsernameAlert)).check(matches(withText("")))
    }

    @Test
    fun checkUsernameAlertMessageAppearClearing(){
        onView(withId(R.id.loginUsername)).perform(typeText("today"))
        onView(withId(R.id.loginUsername)).perform(clearText())
        onView(withId(R.id.loginUsernameAlert)).check(matches(withText("Username is required")))
    }

    @Test
    fun checkPasswordAlertMessageAppearAtTheStart(){
        onView(withId(R.id.loginPasswordAlert)).check(matches(withText("Password is required")))
    }

    @Test
    fun checkPasswordAlertMessageChangedUponTyping(){
        onView(withId(R.id.loginPassword)).perform(typeText("a"))
        onView(withId(R.id.loginPasswordAlert)).check(matches(withText("")))
    }

    @Test
    fun checkPasswordAlertMessageAppearClearing(){
        onView(withId(R.id.loginPassword)).perform(typeText("today"))
        onView(withId(R.id.loginPassword)).perform(clearText())
        onView(withId(R.id.loginPasswordAlert)).check(matches(withText("Password is required")))
    }




}