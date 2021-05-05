package com.nabilmh.myapplication

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nabilmh.myapplication.ui.theme.MyApplicationTheme

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
    val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // onNameChange is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Greeting(model: HelloViewModel ) {
    val name: State<String> = model.name.observeAsState("")
    HelloContent(name = name.value, onNameChange = { model.onNameChange(it) })
}


@ExperimentalPagerApi
@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        AnnouncementSection(name)
        Spacer(modifier = Modifier.padding(top = 8.dp))
        BannerSection(name = "asdas")
    }
}

@Composable
fun AnnouncementSection(name: String) {

    Surface(color = Color(red = 185, green = 4,blue = 60)) {
        Column {
//        val showHs  by remember { mutableStateOf(false) }
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

@Composable
private fun PageIndicator(modifier: Modifier = Modifier, currentPage: Int, numberOfPages: Int) {
    Row(modifier = modifier) {
        repeat(times = numberOfPages) { page ->
            val circleColor = if (page == currentPage) {
                MaterialTheme.colors.onBackground
            } else {
                MaterialTheme.colors.onBackground
            }
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(shape = CircleShape)
                    .background(color = circleColor)
                    .size(8.dp)
            )
        }
    }
}


@Composable
fun ShimmerSample(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(3) {
            PlaceHolder()
            Spacer(modifier = Modifier.height(20.dp))

        }
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountdownTime(running: Boolean, timeInMillis: Long) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text((timeInMillis / 60_000).toString().padStart(2, '0'), style = MaterialTheme.typography.h2, fontWeight = FontWeight.Bold)
        }
        Text(":", style = MaterialTheme.typography.h2, fontWeight = FontWeight.Bold)
        Column {
            Text(((timeInMillis % 60_000) / 1000).toString().padStart(2, '0'), style = MaterialTheme.typography.h2, fontWeight = FontWeight.Bold)
        }
        Text(((timeInMillis % 60_000) % 1000).toString().padStart(3, '0'), style = MaterialTheme.typography.h4, modifier = Modifier.offset(y = 10.dp))
    }
}

@Composable
fun TimeArrow(up: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(75.dp)
            .height(75.dp)
    ) {
        Icon(
            imageVector = if (up) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   val viewModel = HelloViewModel()

    MyApplicationTheme {
        Greeting(viewModel)
    }
}