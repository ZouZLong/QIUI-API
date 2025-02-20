package com.example.openplatform.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.openplatform.R
import com.example.openplatform.util.ToastUtil
import kotlin.math.roundToInt

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CallCounter()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CallCounter()
}

@Composable
fun CallCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    var doubleCount by rememberSaveable { mutableStateOf(0) }

    Column {
        Counter(
            count = count,
            onIncrement = { count++ },
            modifier.fillMaxWidth()
        )
        Counter(
            count = doubleCount,
            onIncrement = { doubleCount += 2 },
            modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Counter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$count",
            fontSize = 50.sp
        )
        Button(
            onClick = { onIncrement() }
        ) {
            Text(
                text = "Click me",
                fontSize = 26.sp
            )
        }
    }
}


@Composable
fun HighLevelCompose() {
    val context = LocalContext.current
    var offsetX by remember { mutableStateOf(0f) }

    Column(modifier = Modifier
        .offset { IntOffset(offsetX.roundToInt(), 0) }
        .requiredSize(200.dp)
        .background(Color.Blue)
        .verticalScroll(rememberScrollState())
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                offsetX += delta
            })
    ) {
        repeat(10) {
            Text(
                text = "Item $it",
                color = Color.White,
                fontSize = 26.sp
            )
        }
    }
}


@Composable
fun IconImage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue) // 设置背景颜色为蓝色Color = Color.White
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Icon Image",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.CenterStart)
                .border(5.dp, Color.Magenta, CircleShape)
                .clip(CircleShape)
                .rotate(180f)
        )
    }

}

@Composable
fun SimpleWidgetColumn() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        //让Column中的每个子控件平分Column在垂直方向上的空间
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "This is Text", color = Color.Blue, fontSize = 26.sp
        )

        Button(
            onClick = {
                ToastUtil.showToastCenter("单击了按钮")
            },
        ) {
            Text(
                text = "这是一个按钮", color = Color.Blue, fontSize = 26.sp
            )
        }

        TextField(value = "", onValueChange = {}, placeholder = {
            Text(text = "请输入密码", fontSize = 8.sp)
        }, colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        )
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "这个是一个图片"
        )

        AsyncImage(
            model = "https://img-blog.csdnimg.cn/20200401094829557.jpg",
            contentDescription = "这是一个网络图片"
        )

        CircularProgressIndicator(
            color = Color.Green, strokeWidth = 6.dp
        )

        LinearProgressIndicator(
            color = Color.Blue, backgroundColor = Color.Gray
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "This is Text", color = Color.Blue, fontSize = 26.sp
            )

            Button(
                onClick = {
                    ToastUtil.showToastCenter("单击了按钮")
                },
            ) {
                Text(
                    text = "这是一个按钮", color = Color.Blue, fontSize = 26.sp
                )
            }

            TextField(value = "", onValueChange = {}, placeholder = {
                Text(text = "请输入密码", fontSize = 8.sp)
            }, colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            )
            )

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "这个是一个图片"
            )

            AsyncImage(
                model = "https://img-blog.csdnimg.cn/20200401094829557.jpg",
                contentDescription = "这是一个网络图片"
            )
        }

    }
}


