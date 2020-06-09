package com.example.lottotest.lotteries

import android.util.MalformedJsonException
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DrawResultListItem(api: DrawInfoApi, lotteryType: LotteryType, drawIdentifier: String) {
    val result = getDrawInfoById(
        api,
        lotteryType.id,
        drawIdentifier
    )

    DrawResultListItemView(result)
}

@Preview("Draw result item loading")
@Composable
fun DrawResultListItemView_Loading() {
    DrawResultListItemView(Result.Loading)
}

@Preview("Draw result item error")
@Composable
fun DrawResultListItemView_Error() {
    DrawResultListItemView(
        Result.Error(
            Throwable("Error")
        )
    )
}

@Preview("Draw result item success")
@Composable
fun DrawResultListItemView_Success() {
    val drawInfo = DrawInfo(
        drawIdentifier = "drawIdentifier",
        drawResult = DrawResult(
            numbers = listOf(1, 2, 3, 4, 5, 6),
            superNumber = 99
        )
    )
    DrawResultListItemView(
        Result.Success(
            drawInfo
        )
    )
}

@Composable
fun DrawResultListItemView(result: Result<DrawInfo>) {
    Card(
        modifier = Modifier.preferredHeight(80.dp).fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            when (result) {
                Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), gravity = Alignment.Center) {
                        Text("Failed to load draw info")
                    }
                }
                is Result.Success -> {
                    val drawInfo = result.data
                    Column {
                        Text(
                            text = "Draw results for ${drawInfo.drawIdentifier}",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val drawResult = drawInfo.drawResult
                        if (drawResult == null) {
                            Text("Not available")
                        } else {
                            Row {
                                drawResult.numbers.map { number ->
                                    DrawResultNumber(
                                        number
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                drawResult.superNumber?.let {
                                    SuperNumber(
                                        superNumber = it
                                    )
                                }
                                drawResult.euroNumbers?.let {
                                    it.map { euroNumber ->
                                        EuroNumber(
                                            euroNumber = euroNumber
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawResultNumber(number: Int) {
    Box(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        border = Border(0.5.dp, Color.Black),
        gravity = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun SuperNumber(superNumber: Int) {
    Box(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        backgroundColor = Color.Yellow,
        gravity = Alignment.Center
    ) {
        Text(
            text = superNumber.toString(),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun EuroNumber(euroNumber: Int) {
    Box(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        border = Border(0.5.dp, Color.Blue),
        gravity = Alignment.Center
    ) {
        Text(
            text = euroNumber.toString(),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun getDrawInfoById(
    api: DrawInfoApi,
    lotteryId: String,
    drawIdentifier: String
): Result<DrawInfo> {
    val (result, setResult) = state<Result<DrawInfo>> { Result.Loading }

    onCommit(lotteryId, drawIdentifier) {
        api.drawInfoById(lotteryId, drawIdentifier).enqueue(object : Callback<DrawInfo> {
            override fun onFailure(call: Call<DrawInfo>, t: Throwable) {
                setResult(Result.Error(t))
            }

            override fun onResponse(
                call: Call<DrawInfo>,
                response: Response<DrawInfo>
            ) {
                val body = response.body()
                if (body != null) {
                    setResult(Result.Success(body))
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