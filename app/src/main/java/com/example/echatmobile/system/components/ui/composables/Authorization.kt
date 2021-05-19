package com.example.echatmobile.system.components.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Authorization(
    title: String? = null,
    mainButtonText: String? = null,
    onMainButtonClick: (Pair<String, String>) -> Unit,
    centerTextButtonText: String? = null,
    onCenterTextButtonClick: (() -> Unit)? = null,
    bottomTextButtonText: String? = null,
    onBottomTextButtonClick: (() -> Unit)? = null,
    isMainButtonClickable: Boolean = true
) {
    val passwordTransformation = VisualTransformation {
        TransformedText(
            AnnotatedString(
                String(it.map { '*' }.toCharArray()),
                it.spanStyles,
                it.paragraphStyles
            ), OffsetMapping.Identity
        )
    }

    val usernameState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
    val passwordDisplayState = remember {
        mutableStateOf(passwordTransformation)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(elevation = 5.dp) {
            Box(
                modifier = Modifier
                    .padding(45.dp)
                    .fillMaxWidth(0.75f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    title?.let {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp),
                            text = it,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        value = usernameState.value,
                        onValueChange = { usernameState.value = it },
                        placeholder = { Text("username") }
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        placeholder = { Text("password") },
                        trailingIcon = {
                            PasswordToggleButton {
                                if (it) {
                                    passwordDisplayState.value = passwordTransformation
                                } else {
                                    passwordDisplayState.value = VisualTransformation.None
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(autoCorrect = false),
                        visualTransformation = passwordDisplayState.value
                    )

                    centerTextButtonText?.let {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp)
                                .clickable { onCenterTextButtonClick?.invoke() },
                            text = "Forgot password?",
                            textAlign = TextAlign.End,
                            color = Color.Gray,
                        )
                    }

                    AuthorizationButton(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(vertical = 7.dp),
                        onClick = {
                            onMainButtonClick(usernameState.value to passwordState.value)
                        },
                        text = mainButtonText,
                        clickable = isMainButtonClickable
                    )

                    bottomTextButtonText?.let {
                        Text(
                            modifier = Modifier.clickable { onBottomTextButtonClick?.invoke() },
                            text = it,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun AuthorizationButton(
    modifier: Modifier,
    clickable: Boolean,
    text: String?,
    onClick: () -> Unit
) {
    val color = if (clickable) {
        Color.Unspecified
    } else {
        Color.Gray
    }

    Button(
        modifier = modifier.background(color),
        onClick = {
            if (clickable) {
                onClick()
            }
        }
    ) {
        text?.let { Text(it) }
    }
}

@Composable
fun PasswordToggleButton(
    onToggle: ((Boolean) -> Unit)? = null
) {
    val iconVectorState = remember {
        mutableStateOf(Icons.Default.Password)
    }
    val checkedState = remember {
        mutableStateOf(true)
    }

    IconToggleButton(
        checked = checkedState.value,
        onCheckedChange = {
            checkedState.value = it
            if (it) {
                iconVectorState.value = Icons.Default.Password
            } else {
                iconVectorState.value = Icons.Default.RemoveRedEye
            }
            onToggle?.invoke(it)
        }) {
        Icon(
            imageVector = iconVectorState.value,
            contentDescription = "Password visibility toggle",
        )
    }
}