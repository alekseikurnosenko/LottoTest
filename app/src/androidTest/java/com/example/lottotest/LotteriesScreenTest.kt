package com.example.lottotest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.ui.test.createComposeRule
import androidx.ui.test.findBySubstring
import androidx.ui.test.findByText
import com.example.lottotest.lotteries.*
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LotteriesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val api = object : DrawInfoApi {
        override fun drawsList(lotteryId: String): Observable<List<DrawInfoListItem>> {
            return Observable.just(
                listOf(
                    DrawInfoListItem("id1"),
                    DrawInfoListItem("id2"),
                    DrawInfoListItem("id3")
                )
            )
        }

        override fun drawInfoById(
            lotteryId: String,
            drawIdentifier: String
        ): Observable<DrawInfo> {
            return Observable.just(
                DrawInfo(
                    drawIdentifier = drawIdentifier,
                    drawResult = DrawResult(
                        numbers = listOf(1, 2, 3, 4, 5, 6),
                        superNumber = 4
                    )
                )
            )
        }
    }

    @Test
    fun appIsDisplayed() {
        composeTestRule.setContent {
            LotteriesScreen(api)
        }

        findByText("Lotteries").assertExists()
        // Text search uses accessibility labels
        // For drawResult item all the texts are merged into a single item
        findBySubstring("Draw results for id3").assertExists()
    }

    @Test
    fun itemIsDisplayed() {
        composeTestRule.setContent {
            DrawResultListItem(
                api = api,
                lotteryType = LotteryType("Some", "id"),
                drawIdentifier = "id"
            )
        }

        findBySubstring("Draw results for id").assertExists()
    }
}