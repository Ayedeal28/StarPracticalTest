package com.example.recipeexplorer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DifficultyFilter(
    selectedDifficulty: String?,
    onDifficultySelected: (String?) -> Unit,
    availableDifficulties: List<String>,
    modifier: Modifier = Modifier
) {
    if (availableDifficulties.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedDifficulty == null,
            onClick = { onDifficultySelected(null) },
            label = { Text("All") }
        )

        availableDifficulties.forEach { difficulty ->
            FilterChip(
                selected = selectedDifficulty == difficulty,
                onClick = { onDifficultySelected(difficulty) },
                label = { Text(difficulty) }
            )
        }
    }
}