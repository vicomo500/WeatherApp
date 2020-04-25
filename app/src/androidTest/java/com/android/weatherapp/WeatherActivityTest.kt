package com.android.weatherapp

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.weatherapp.views.WeatherActivity
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherActivityTest {
    @get:Rule
    var activityRule: ActivityTestRule<WeatherActivity>
            = ActivityTestRule(WeatherActivity::class.java)

    @Test fun checkListView_initiallyNotDisplayed() {
        onView(withId(R.id.listview)).check(matches(isDisplayed()))
    }

//    @Test
//    fun queryWeatherApi() {
//        // type city
//        onView(withId(R.id.searchview))
//            .perform(click())
//            .perform(clearText(), typeText("London"))
//            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
//        //progress bar
//        onView(withId(R.id.progressbar)).check(matches(isDisplayed()))
//    }
}
