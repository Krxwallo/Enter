package io.github.krxwallo.enter.ui.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import io.github.krxwallo.enter.net.APIClient
import io.github.krxwallo.enter.setCachedIp

private fun isValidIpAddress(ip: String): Boolean {
    val ipRegex = """^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$""".toRegex()
    return ipRegex.matches(ip)
}

@Composable
fun IpEdit(modifier: Modifier = Modifier) {
    var ip by remember {
        mutableStateOf(APIClient.ip)
    }
    var error by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = ip,
        modifier = modifier,
        onValueChange = {
            if (it.contains(Regex("[a-zA-Z]"))) return@OutlinedTextField
            ip = it
            error = !isValidIpAddress(it)
            if (!error) {
                APIClient.ip = it
                setCachedIp(it)
            }
        },
        label = {
            Text(text = "IP Address of HomeMatic CCU3")
        },
        isError = error,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        placeholder = {
            Text("192.168.10.100", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
    )
}

@Composable
@Preview
fun IpEditPreview() {
    IpEdit(Modifier.fillMaxWidth())
}