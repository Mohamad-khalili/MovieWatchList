package com.compose.watchlist.ui.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compose.watchlist.R
import com.compose.watchlist.data.local.entities.MovieEntity
import com.compose.watchlist.viewmodel.MovieViewModel

@Composable
fun WatchListScreen(modifier: Modifier = Modifier) {
    val viewModel: MovieViewModel = hiltViewModel()
    viewModel.getMovies()
    val savedMovies by viewModel.savedMovies.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    if (savedMovies.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.file),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text("List is Empty", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding(), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items(items = savedMovies, key = { it.imdbID }) { item ->
                MovieItem(movieEntity = item, viewModel = viewModel)
            }
        }
    }

}

@Composable
fun MovieItem(modifier: Modifier = Modifier, movieEntity: MovieEntity, viewModel: MovieViewModel) {
    val context = LocalContext.current


    LaunchedEffect(movieEntity.imdbID) {
        viewModel.checkBookmarkStatus(imdbId = movieEntity.imdbID)
    }
    var isOpen by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp // Elevation for the card
        )

    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .wrapContentHeight()
                .animateContentSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(movieEntity.poster)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RectangleShape)
                        .width(110.dp),

                    onError = { i ->
                        Log.d("TAG", "HomeItems: error      ${i.result.throwable.localizedMessage}")

                    }

                )
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(5f)
                ) {

                    Text(
                        movieEntity.title,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = modifier
                            .background(
                                color = Color(0xFFF5C518),
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "IMDB ${movieEntity.imdbRate}",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Serif,
                                shadow = Shadow(Color.Black, Offset.Zero, 1f)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        movieEntity.genre,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        movieEntity.runtime,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                Icon(
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .weight(0.5f)
                        .clickable {
                            viewModel.removeBookmarkFromWatchList(movieEntity = movieEntity)

                        },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "save"
                )

            }

            Column(modifier = modifier
                .fillMaxWidth()
                .clickable { isOpen = !isOpen }) {
                val icon =
                    if (!isOpen) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .align(Alignment.CenterHorizontally)

                )
                if (isOpen) {
                    Spacer(modifier.height(10.dp))
                    Text(
                        movieEntity.plot,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun WatchListScreenPrev() {
    val movie = MovieEntity(
        "1",
        title = "thor",
        imdbRate = "7.0",
        genre = "Comedy",
        plot = "sdfjiuhongiosdlufrhbglisduhrgilsuhrgliujh",
        poster = "",
        runtime = "1h"
    )
    MovieItem(movieEntity = movie, viewModel = viewModel())
}