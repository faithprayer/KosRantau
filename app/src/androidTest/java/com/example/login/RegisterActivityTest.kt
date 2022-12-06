package com.example.login


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
        val neumorphButton = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutRegUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test15"), closeSoftKeyboard())

        val neumorphButton2 = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutNomorHandphone),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("123456"), closeSoftKeyboard())

        val neumorphButton3 = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("test123@gmail.com"), closeSoftKeyboard())

        val neumorphButton4 = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutTanggalLahir),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("20-05-2040"), closeSoftKeyboard())

        val neumorphButton5 = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("1234"), closeSoftKeyboard())



        val neumorphButton6 = onView(
            allOf(
                withId(R.id.btnRegistrasi), withText("Registrasi"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withId(R.id.layoutRegister),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        neumorphButton6.perform(click())
        onView(isRoot()).perform(waitFor(3000))
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

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
