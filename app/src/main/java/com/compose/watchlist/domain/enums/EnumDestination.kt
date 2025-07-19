package com.compose.watchlist.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.ui.graphics.vector.ImageVector

enum class EnumDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val description: String
) {
    Search("search","Search", Icons.Default.Search,"Search"),
    WatchList("watchlist","Watch List", Icons.Outlined.Bookmarks,"Watch List")
}