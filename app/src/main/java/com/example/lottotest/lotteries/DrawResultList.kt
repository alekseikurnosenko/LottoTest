package com.example.lottotest.lotteries

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
import io.reactivex.android.schedulers.AndroidSchedulers

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
        val disposable = api.drawsList(lotteryType.id)
            .map<Result<List<DrawInfoListItem>>> { Result.Success(it.reversed()) }
            .onErrorReturn { Result.Error(it) }
            .startWith(Result.Loading)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                setResult(result)
            }

        onDispose {
            disposable.dispose()
        }
    }

    return result
}