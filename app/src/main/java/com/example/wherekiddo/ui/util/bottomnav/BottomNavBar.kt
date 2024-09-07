package com.example.wherekiddo.ui.util.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wherekiddo.ui.theme.spacing
import com.example.wherekiddo.ui.util.ComponentSizes
import com.example.wherekiddo.ui.util.ComponentSizes.bottomNavBarHeight

@Composable
fun BottomNavBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    bottomBarState: Boolean,
    onItemClick: (BottomNavItem) -> Unit
) {

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {

        BottomNavigation(
            modifier = modifier
                .height(bottomNavBarHeight.dp)
                .clip(MaterialTheme.shapes.large),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {

            items.forEach { item ->

                val selected = item.route == currentRoute

                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    icon = {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                                imageVector = item.icon,
                                contentDescription = item.name
                            )

                            if (selected) {

                                Text(
                                    text = item.name,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}