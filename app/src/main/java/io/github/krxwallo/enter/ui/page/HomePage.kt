package io.github.krxwallo.enter.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.krxwallo.enter.ui.theme.EnterTheme


@Composable
fun HomePage(modifier: Modifier = Modifier) = Column(modifier.padding(16.dp)) {
    IpEdit(Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(16.dp))
    DoorView()
}

@Preview
@Composable
fun HomePagePreview() = EnterTheme(darkTheme = true) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        HomePage(Modifier.fillMaxSize())
    }
}