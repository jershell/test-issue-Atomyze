package com.github.templateapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Input(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
    maxLines: Int = Int.MAX_VALUE,
    background: Color = Color.White,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var hasFocus by remember { mutableStateOf(false) }
    val defaultKeyboardActions = remember {
        KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    }
    Column {

        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(background)
                .heightIn(min = 42.dp)
                .padding(horizontal = 16.dp)
                .then(modifier),
            Alignment.CenterStart
        ) {
            Box {
                if (value.text.isEmpty() && !placeholder.isNullOrEmpty()) {
                    Text(placeholder, style = TextStyle(color = Color.LightGray))
                }

                BasicTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    enabled = enabled,
                    maxLines = maxLines,
                    singleLine = singleLine,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions ?: defaultKeyboardActions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            hasFocus = focusState.isFocused
                            onFocusChanged(hasFocus)
                        },
                    textStyle = TextStyle(color = Color.DarkGray),
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(Color.Black),
                )
            }
        }
    }
}