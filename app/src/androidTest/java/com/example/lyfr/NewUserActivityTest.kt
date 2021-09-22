package com.example.lyfr


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NewUserActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun newUserActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.buttonCreateNewUser), withText("New User"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.etName),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Rick"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etZip),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("84103"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.etDate),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    13
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("22"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.etHeight),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("89"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.etWeight),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    18
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("222"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.buttonSaveProfile), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    20
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
