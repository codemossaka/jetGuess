package com.godsonpeya.jetguess

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godsonpeya.jetguess.ui.theme.JetGuessTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetGuessTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    GameScreen()
                }
            }
        }
    }
}


@Composable
fun GameScreen() {

    var min = 1
    var max = 12

    val numbers = min..max
    var generatedNumber by remember {
        mutableStateOf(Random.nextInt(min, max))
    }
    var message by remember {
        mutableStateOf("")
    }

    var ans by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Guess the value of X", fontSize = 30.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = ans.ifEmpty { "X" }, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = message, fontSize = 20.sp, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(40.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                items(numbers.toList()) { number ->
                    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Button(onClick = {
                            message = guestGame(providedNumber = number,
                                generatedNumber = generatedNumber,
                                min = min,
                                max = max)

                            if (message.contains("oura", true)) {
                                ans = number.toString()
                            }

                        },
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2771DA))) {
                            Text(text = number.toString(),
                                modifier = Modifier.padding(15.dp),
                                color = Color.White,
                                fontSize = 20.sp)
                        }
                    }
                }

            }, modifier = Modifier
                .weight(7f)
                .fillMaxWidth())

        }
    }

    if (ans.isNotEmpty()){
        DialogView(message){
            message = ""
            ans = ""
            generatedNumber = Random.nextInt(min, max)
        }
    }
}

@Composable
fun DialogView(message: String, resetGame: () -> Unit) {

    var showDialog by remember {
        mutableStateOf(true)
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { /*TODO*/ },
            title = { Text(text = "You won!") },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = {
                    resetGame.invoke()
                    showDialog = false
                }) {
                    Text(text = "Replay")
                }

            })
    }
}

fun guestGame(providedNumber: Int, generatedNumber: Int, min: Int, max: Int): String {
    while (generatedNumber != providedNumber) {
        return if (providedNumber < min || providedNumber > max) {
            "The provided number doesn't to the interval of $min to $max"
        } else {
            if (providedNumber > generatedNumber) {
                "The provided number is greater than the generated one"
            } else {
                "The provided number is less than the generated one"
            }
        }
    }
    return "Ouraaaaaaaaaaa !!!! You won!!!!!!!!"
}