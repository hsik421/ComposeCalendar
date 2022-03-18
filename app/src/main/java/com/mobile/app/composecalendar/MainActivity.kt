package com.mobile.app.composecalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.app.composecalendar.ui.theme.ComposeCalendarTheme
import com.mobile.app.composecalendar.ui.theme.Purple500
import com.mobile.app.composecalendar.ui.theme.Purple700
import com.mobile.app.composecalendar.ui.theme.Teal200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        CalendarScreen()
        RepeatScreen()
    }
}

@Composable
fun RepeatScreen() {
    val calendarViewModel = viewModel<CalendarViewModel>()
    val repeatDay by calendarViewModel.repeatDay.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        CalendarModule.WEEK_LABEL.forEach {
            Button(
                modifier = Modifier
                    .padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (repeatDay or it.second == repeatDay) {
                        Purple700
                    } else {
                        Color.White
                    }
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    calendarViewModel.setDateViewState(
                        Pair(
                            calendarViewModel.date.value.month,
                            calendarViewModel.date.value.year
                        ), calendarViewModel.repeatValue(it.second)
                    )
                }
            ) {
                Text(
                    text = it.first,
                    fontSize = 12.sp,
                    color = if (repeatDay or it.second == repeatDay) Color.White else Purple700
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(title: Pair<String, String>, onNext: () -> Unit, onPrev: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "<",
            modifier = Modifier
                .clickable(onClick = onPrev)
                .padding(20.dp, 20.dp, 20.dp, 18.dp)

        )
        Text(
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${title.first}, ")
                }
                append(title.second)
            },
            fontSize = 18.textDp(LocalDensity.current),
            color = Color.White,
        )
        Text(
            text = ">",
            modifier = Modifier
                .clickable(onClick = onNext)
                .padding(20.dp, 20.dp, 20.dp, 18.dp)
        )
    }
}

@Composable
private fun MonthHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        CalendarModule.WEEK_LABEL.forEach {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.textDp(LocalDensity.current),
                color = "66ffffff".color(),
                text = it.first
            )
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 9.dp, bottom = 15.dp),
        color = "66ffffff".color()
    )
}

@Composable
fun CalendarScreen() {
    val calendarViewModel = viewModel<CalendarViewModel>()
    val item = calendarViewModel.date.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .background(Teal200)
                .padding(bottom = 10.dp)
        ) {
            CalendarHeader(
                title = item.value.month.plus(1).toString() to item.value.year.toString(),
                onNext = {
                    calendarViewModel.onNext(0)
                },
                onPrev = {
                    calendarViewModel.onPrev(0)
                })
            MonthHeader()

            item.value.days.chunked(7).forEach {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        it.forEach { value ->
                            Day(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 15.dp),
                                value = value
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Day(modifier: Modifier, value: Date) {
    Box(
        modifier = modifier.padding(bottom = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = when (value.dateState) {
                        DateEnum.SELECT -> Purple500
                        DateEnum.TODAY -> Color.White
                        else -> Color.Transparent
                    },
                    shape = CircleShape
                )
                .width(25.dp)
                .height(25.dp)
        ) {

        }
        Text(
            text = value.day.toString(),
            color = when (value.calendarState) {
                CalendarEnum.NONE -> "33000000".color()
                CalendarEnum.TODAY -> Purple700
                else -> Color.White
            },
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 14.textDp(LocalDensity.current),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCalendarTheme {
        CalendarScreen()
    }
}