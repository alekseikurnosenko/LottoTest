package com.example.lottotest

import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxSize
import androidx.ui.material.Scaffold
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview

enum class LotteryType {
    First,
    Second
}

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
}

@Model
data class LotteriesScreenState(
    val draws: Map<LotteryType, Result<List<Int>>>
)

@Preview
@Composable
fun LotteriesScreen() {
    val state = LotteriesScreenState(
        mapOf(
            LotteryType.First to Result.Loading,
            LotteryType.Second to Result.Loading
        )
    )
    
    Scaffold(
        topAppBar = {
            TopAppBar(
                title = { Text(text = "Lotteries") }
            )
        }
    ) {
        val (currentSelection, setCurrentSelection) = state { LotteryType.First }
        val tabItems = LotteryType.values().map { it.name }
        Column {
            TabRow(items = tabItems, selectedIndex = 0) { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = currentSelection.ordinal == index,
                    onSelected = { setCurrentSelection(LotteryType.values()[index]) }
                )
            }
            when (val result = state.draws.getValue(currentSelection)) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
                        Text("Loading")
                    }
                }
                is Result.Error -> Text("Error")
                is Result.Success -> {
                    VerticalScroller {
                        Column {
                            result.data.map { item ->
                                Row {
                                    Text("Get")
                                    Text("Sum")
                                    Text(item.toString())
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}