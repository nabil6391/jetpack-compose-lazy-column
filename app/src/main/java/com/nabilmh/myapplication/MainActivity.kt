package com.nabilmh.myapplication

import FlipClock
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nabilmh.myapplication.ui.theme.MyApplicationTheme
import com.nabilmh.myapplication.ui.theme.Shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil
import kotlin.math.max

class MainActivity : ComponentActivity() {
    private val viewModel: HelloViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(viewModel)
                }
            }
        }
    }
}

class HelloViewModel : ViewModel() {
    data class SectionItem(val id: String, var state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading))

    var sectionList = mutableListOf<SectionItem>()

    var endTime = SystemClock.uptimeMillis() + DateUtils.MINUTE_IN_MILLIS

    sealed class ViewState {
        object Loading : ViewState()
        data class Loaded<T>(val data: T) : ViewState()
        object NotAvailable : ViewState()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            sectionList.add(SectionItem("A"))
            sectionList.add(SectionItem("B"))
            sectionList.add(SectionItem("C"))
            sectionList.add(SectionItem("D"))
            delay(5000)

            sectionList.firstOrNull { it.id == "A" }?.state?.value = ViewState.Loaded(listOf("A"))

            endTime = SystemClock.uptimeMillis() + DateUtils.MINUTE_IN_MILLIS
            sectionList.firstOrNull { it.id == "B" }?.state?.value = ViewState.Loaded(endTime)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Greeting(model: HelloViewModel) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(count = model.sectionList.size, itemContent = { item ->
            val sectionItem = model.sectionList[item]
            Log.d("COMPOSE", "This get rendered $item ${sectionItem.id}")
            when (sectionItem.id) {
                "A" -> {
                    val projectsLiveData = sectionItem.state.collectAsState()
                    ProjectSection(state = projectsLiveData.value)
                }
                "B" -> {
                    val stateData = sectionItem.state.collectAsState()
                    Flipper(stateData.value)
                }
                "C" -> {
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    AnnouncementSection()
                }
                "D" -> {
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    BannerSection(name = "asdas")
                }
                else -> {

                }
            }
        })
    }
}

@Composable
fun Flipper(state: HelloViewModel.ViewState?) {
    when (state) {
        HelloViewModel.ViewState.Loading -> {
            Shimmer(elementHeight = 100.dp, content = {
                ShimmerElement(modifier = it)
            })
        }
        is HelloViewModel.ViewState.Loaded<*> -> {
            val endTime: Long = state.data as Long
            var remainingSeconds by remember { mutableStateOf(0) }

            fun updateRemainingTime() {
                remainingSeconds = ceil(max(endTime - SystemClock.uptimeMillis(), 0L).toFloat() / 1000F).toInt()
            }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.Main) {
                    updateRemainingTime()
                    while (remainingSeconds > 0) {
                        updateRemainingTime()
                        delay(400L)
                        Log.d("log", "Countdown  $endTime $remainingSeconds")
                    }
                }
            }

            if (remainingSeconds == 0) {
                Spacer(modifier = Modifier)
            } else {
                FlipClock(seconds = remainingSeconds)
            }
        }
        HelloViewModel.ViewState.NotAvailable -> {
            Text(text = "Empty")
        }
    }

}


@Composable
fun AnnouncementSection() {
    Surface(color = Color(red = 185, green = 4, blue = 60), modifier = Modifier.fillMaxWidth()) {
        Column {
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                color = Color.White,
                text = "Lets celebrate love. Create custom gifts",
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Text(
                color = Color.White,
                text = "Up to 70% OFF | Use code VDAY2021",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}

@ExperimentalPagerApi
@Composable
fun BannerSection(name: String) {
    // Display 10 items
    val pagerState = rememberPagerState(
        pageCount = 3,
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth(),
    ) { page ->
        Column {
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Image(
                painter = rememberGlidePainter("https://picsum.photos/300/300"),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
    }
    HorizontalPagerIndicator(
        pagerState = pagerState,
        modifier = Modifier
            .padding(16.dp),
    )
}


@ExperimentalPagerApi
@Composable
fun ProjectSection(state: HelloViewModel.ViewState?) {
    when (state) {
        HelloViewModel.ViewState.Loading -> {
            Shimmer(elementHeight = 100.dp, content = {
                ShimmerElement(modifier = it)
            })
        }
        is HelloViewModel.ViewState.Loaded<*> -> {
            AnnouncementSection()
        }
        HelloViewModel.ViewState.NotAvailable -> {
            Text(text = "Empty")
        }
    }
}

@Composable
fun ShimmerElement(modifier: Modifier) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(32.dp)
        )
    }
}

@Composable
fun PlaceHolder(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ImagePlaceHolder()
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            repeat(3) {
                LinePlaceHolder()
                Spacer(modifier = Modifier.height(10.dp))
            }
            LinePlaceHolder(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
fun LinePlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(color = Color.LightGray)
    )
}

@Composable
fun ImagePlaceHolder() {
    Box(modifier = Modifier
        .size(110.dp)
        .background(color = Color.LightGray))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   val viewModel = HelloViewModel()

    MyApplicationTheme {
        Greeting(viewModel)
    }
}