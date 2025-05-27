package com.garudaproject.upbacky

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app.MainActivity
import com.example.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

  @get:Rule val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun testTextView() {
    onView(withId(R.id.text)).check(matches(withText("upbacky")))
  }
}
