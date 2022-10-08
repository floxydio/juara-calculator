package com.example.juaracalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juaracalculator.ui.theme.JuaraCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuaraCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen() {
    val focusManager = LocalFocusManager.current
    var amountInput by remember{
        mutableStateOf("")
    }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val output = calculateTip(amount)

    var tipInput by remember{
        mutableStateOf("")
    }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount,tipPercent)
    var roundUp by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text(stringResource(R.string.app_title),fontSize = 24.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))
        EditNumberField(label = R.string.amount,amountInput, onValueChange = {amountInput = it}, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ), keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down)}))
        Spacer(modifier = Modifier.height(20.dp))
        EditNumberField(label = R.string.how_service,tipInput, onValueChange = {tipInput = it}, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ), keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()})
        )

        RoundTheTipRow(roundUp = roundUp, onRoundUpChange = {roundUp = it} )

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(id = R.string.tipamount, tip), modifier = Modifier.align(Alignment.CenterHorizontally),fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EditNumberField(@StringRes label: Int, value: String, onValueChange: (String) -> Unit, keyboardOptions: KeyboardOptions, keyboardActions: KeyboardActions) {

    TextField(value = value,
        label = {
                Text(stringResource(id = label),modifier = Modifier.fillMaxWidth())
        },
        onValueChange = onValueChange,keyboardOptions = keyboardOptions,keyboardActions = keyboardActions,singleLine = true)
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(48.dp),verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.amount))
            Switch(checked = roundUp, onCheckedChange = onRoundUpChange, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End))
        }


}



private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JuaraCalculatorTheme {
        TipTimeScreen()
    }
}