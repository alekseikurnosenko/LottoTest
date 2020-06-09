package com.example.lottotest.lotteries

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.material.Scaffold
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import androidx.ui.material.TopAppBar
import androidx.ui.unit.dp

data class LotteryType(val displayName: String, val id: String)

@Composable
fun LotteriesScreen(api: DrawInfoApi) {
    val lotteries = listOf(
        LotteryType("Lotto 6aus49", "6aus49"),
        LotteryType("Eurojackpot", "eurojackpot")
    )

    Scaffold(
        topAppBar = {
            TopAppBar(
                title = { Text(text = "Lotteries") },
                elevation = 0.dp
            )
        }
    ) {
        val (currentSelection, setCurrentSelection) = state { lotteries.first() }
        val tabItems = lotteries.map { it.displayName }
        Column {
            TabRow(
                items = tabItems,
                selectedIndex = lotteries.indexOf(currentSelection)

            ) { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = currentSelection == lotteries[index],
                    onSelected = { setCurrentSelection(lotteries[index]) }
                )
            }

            DrawResultList(api, currentSelection)
        }
    }
}