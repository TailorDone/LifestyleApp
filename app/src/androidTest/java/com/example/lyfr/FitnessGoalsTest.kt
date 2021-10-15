package com.example.lyfr


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FitnessGoalsTest {

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
    fun fitnessGoalsTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.buttonCreateNewUser), withText("New com.example.lyfr.User"),
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
        appCompatEditText.perform(replaceText("R"), closeSoftKeyboard())

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
        appCompatEditText4.perform(replaceText("80"), closeSoftKeyboard())

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

        val materialButton3 = onView(
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
        materialButton3.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.ibFitnessGoals),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tableLayout),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

//        val imageButton = onView(
//            allOf(
//                withId(R.id.image_preview),
//                withParent(withParent(withId(android.R.id.content))),
//                isDisplayed()
//            )
//        )
//        imageButton.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tvCaloriesNeeded), withText("Daily Caloric Goal"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Daily Caloric Goal")))

        val textView2 = onView(
            allOf(
                withId(R.id.tvBMR), withText("BMR"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("BMR")))

        val tableRow = onView(
            allOf(
                withId(R.id.trWeightGoal),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        tableRow.check(matches(isDisplayed()))

        val radioGroup = onView(
            allOf(
                withId(R.id.rgWeightGoals),
                withParent(
                    allOf(
                        withId(R.id.trWeightGoal),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        radioGroup.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.textView9), withText("LifeStyle:"),
                withParent(
                    allOf(
                        withId(R.id.tableRow),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("LifeStyle:")))

        val button = onView(
            allOf(
                withId(R.id.buttonCalculate), withText("CALCULATE"),
                withParent(
                    allOf(
                        withId(R.id.trSubmit),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val seekBar = onView(
            allOf(
                withId(R.id.seekBarLifeStyle),
                withParent(
                    allOf(
                        withId(R.id.trLifestyle),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        seekBar.check(matches(isDisplayed()))
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
