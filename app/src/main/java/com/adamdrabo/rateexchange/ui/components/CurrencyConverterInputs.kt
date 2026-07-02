package com.adamdrabo.rateexchange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdrabo.rateexchange.ui.theme.AppColors

@Composable
fun CurrencyConverterInputs(
    userAmount: String,
    resultat: String,
    sourceLabel: String,
    targetLabel: String,
    sourceCurrency: String,
    targetCurrency: String,
    onAmountChange: (String) -> Unit,
    onSwapClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(15.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Text(
                    text = sourceLabel,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600)
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicTextField(
                        value = userAmount,
                        onValueChange = onAmountChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(750),
                            letterSpacing = (-0.4).sp,
                            fontFeatureSettings = "tnum",
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
                    )
                    Text(
                        text = sourceCurrency,
                        style = TextStyle(fontFamily = FontFamily.Default, fontSize = 13.sp, fontWeight = FontWeight(680)),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        IconButton(
            onClick = onSwapClick,
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                .border(1.dp, AppColors.BlueAccent, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Inverser",
                tint = AppColors.BlueAccent,
                modifier = Modifier.size(22.dp)
            )
        }


        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(15.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Text(
                    text = targetLabel,
                    style = TextStyle(fontFamily = FontFamily.Default, fontSize = 12.sp, fontWeight = FontWeight(600)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = resultat,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(750),
                            letterSpacing = (-0.4).sp,
                            fontFeatureSettings = "tnum",
                            color = if (userAmount.isEmpty() || userAmount == "0") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = targetCurrency,
                        style = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp, fontWeight = FontWeight(700)),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

