package com.example.login


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddEditSurveyTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(ActivitySurvey::class.java)

    @Test
    fun activitySurveyTest() {

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_addSurvey),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.edit_kos),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_survey),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("kost singgahsini Jasmin Seturan"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.edit_penyewa),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_penyewa),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("Prayer Faith Kaawoan"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.edit_noTlp),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_noTlp),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText3.perform(scrollTo(), replaceText("08643242321"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.edit_tanggalSurvey),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_tglSurvey),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText4.perform(scrollTo(), replaceText("23-6-2022"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.edit_jamSurvey),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_jamSurvey),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText5.perform(scrollTo(), replaceText("02:00"), closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
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
