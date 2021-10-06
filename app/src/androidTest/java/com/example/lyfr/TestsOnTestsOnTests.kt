package com.example.lyfr

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class TestsOnTestsOnTests {

    @Test
    fun scrollToButton_andClick() {
        click(scroll = true) {
            allOf {
                id(R.id.login_button)
                text(R.string.login_button_text)
            }
        }
    }

    @Test
    fun matchParent_blockMatching_example() {
        displayed {
            parent(R.id.parent) {
                id(R.id.username)
                id(R.id.password)
                id(R.id.login_button)
            }
        }
    }

    @Test
    fun matchParent_example() {
        onView(
            allOf(isDescendantOf(withId(R.id.parent)), withId(R.id.username))
        )
            .check(matches(isDisplayed()))
        onView(
            allOf(isDescendantOf(withId(R.id.parent)), withId(R.id.password))
        )
            .check(matches(isDisplayed()))
        onView(
            allOf(isDescendantOf(withId(R.id.parent)), withId(R.id.login_button))
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun matchParent_combining_example() {
        displayed {
            allOf {
                parent {
                    id(R.id.parent)
                }
                id(R.id.username)
            }
        }
    }

    @Test
    fun descendant_block_example() {
        displayed {
            allOf {
                descendant {
                    id(R.id.username)
                }
                id(R.id.parent)
            }
        }
    }

    @Test
    fun recyclerView_example() {
        recyclerView(R.id.recycler_view) {
            sizeIs(10)
            atPosition(3) {
                displayed {
                    id(R.id.item_description)
                    text(R.string.description_text)
                    text("Item header text")
                }
            }
        }
    }

    @Test
    fun recyclerView_textInput_example() {
        recyclerView(R.id.recycler_view) {
            atPosition(0) {
                typeText(R.id.editText, "Position 0")
            }

            atPosition(1) {
                typeText(R.id.editText, "Position 1")
            }
        }
    }

    @Test
    fun recyclerView_swipeLeft_example() {
        recyclerView(R.id.recycler_view {
            atPosition(0) {
                swipeLeft()
            }

            atPosition(1) {
                swipeRight()
            }
        }
    }

    @Test
    fun whenClickingOnItem1_shouldShowCorrectText() {
        menu {
            onItem(R.string.item_1) {
                click()
            }
        }

        displayed {
            text(R.string.item_1_selected)
        }
    }


    @Test
    fun intentMatcherTest() {
        val WHATS_PACKAGE_NAME = "com.whatsapp"
        val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
        Intents.init()
        matchIntent {
            action(Intent.ACTION_VIEW)
            url(PLAY_STORE_URL + WHATS_PACKAGE_NAME)
            result {
                ok()
            }
        }

        click {
            id(R.id.btn_start_activity)
        }

        matchIntent {
            action(Intent.ACTION_VIEW)
            url(PLAY_STORE_URL + WHATS_PACKAGE_NAME)
        }

        Intents.release()
    }

    @Test
    fun intentMatcherTest() {
        val WHATS_PACKAGE_NAME = "com.whatsapp"
        val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
        Intents.init()

        val matcher = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(Uri.parse(PLAY_STORE_URL + WHATS_PACKAGE_NAME))
        )
        val result = ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(result)

        click {
            id(R.id.btn_start_activity)
        }

        intended(matcher)

        Intents.release()
    }

    @Test
    fun backgroundColorTest() {
        displayed {
            allOf {
                id(R.id.view_background)
                background(R.drawable.ic_android)
            }
        }

        displayed {
            background(R.color.colorAccent)
        }
    }
}