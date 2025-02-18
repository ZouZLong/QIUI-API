package com.example.openplatform.activity.equipment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class Gen3Activity01 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleWidgetColumn()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleWidgetColumn()
}

@Composable
fun SimpleWidgetColumn() {
    Column {
        Text(text = "This is Text")
    }
}


