package com.example.androidchallenge.ui.series.fragment

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.androidchallenge.R
import com.example.androidchallenge.Util.Companion.withIndex
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.data.source.FakeSeriesRepository
import com.example.androidchallenge.di.AndroidChallengeModule
import com.example.androidchallenge.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@MediumTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AndroidChallengeModule::class)
@HiltAndroidTest
class SeriesDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var seriesRepository: SeriesRepository

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    private fun setRequestStatus(status: Status) {
        (seriesRepository as FakeSeriesRepository).status = status
    }

    @Test
    fun seriesDetailFragment_loading() {
        setRequestStatus(Status.LOADING)
        val bundle = SeriesDetailsFragmentArgs(0).toBundle()
        launchFragmentInHiltContainer<SeriesDetailsFragment>(bundle)

        Espresso.onView(withId(R.id.loader))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun seriesDetailFragment_success() {
        setRequestStatus(Status.SUCCESS)
        val bundle = SeriesDetailsFragmentArgs(0).toBundle()
        launchFragmentInHiltContainer<SeriesDetailsFragment>(bundle)

        Espresso.onView(withId(R.id.tv_series_name))
            .check(ViewAssertions.matches(withText("Vikings")))
    }

    @Test
    fun seriesDetailFragment_open_episode() {
        setRequestStatus(Status.SUCCESS)
        val bundle = SeriesDetailsFragmentArgs(0).toBundle()
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<SeriesDetailsFragment>(bundle, navController) {
            navController.setCurrentDestination(R.id.seriesDetailsFragment)
        }

        Espresso.onView(withIndex(withId(R.id.cv_episode), 0)).perform(ViewActions.click())
        Assert.assertEquals(navController.currentDestination!!.id, R.id.episodeDetailsFragment)
    }
}
