package com.example.lottotest.lotteries

import android.util.MalformedJsonException
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DrawResultList(api: DrawInfoApi, lotteryType: LotteryType) {
    when (val result =
        getDrawInfoList(api, lotteryType)) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
                Text("Loading")
            }
        }
        is Result.Error -> {
            Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
                Text("Failed to fetch list of draws")
            }
        }
        is Result.Success -> {
            VerticalScroller {
                Column(modifier = Modifier.padding(8.dp)) {
                    result.data.map {
                        DrawResultListItem(
                            api,
                            lotteryType,
                            it.drawIdentifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getDrawInfoList(api: DrawInfoApi, lotteryType: LotteryType): Result<List<DrawInfoListItem>> {
    val (result, setResult) = state<Result<List<DrawInfoListItem>>> { Result.Loading }

    onCommit(lotteryType.id) {
        setResult(Result.Loading)
        api.drawsList(lotteryType.id).enqueue(object : Callback<List<DrawInfoListItem>> {
            override fun onFailure(call: Call<List<DrawInfoListItem>>, t: Throwable) {
                setResult(Result.Error(t))
            }

            override fun onResponse(
                call: Call<List<DrawInfoListItem>>,
                response: Response<List<DrawInfoListItem>>
            ) {
                val body = response.body()
                if (body != null) {
                    // Because we want to display most recent results first
                    setResult(Result.Success(body.reversed()))
                } else {
                    setResult(
                        Result.Error(
                            MalformedJsonException("Got empty body")
                        )
                    )
                }
            }
        })
    }

    return result
}