package com.compose.watchlist.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compose.watchlist.R
import com.compose.watchlist.domain.model.Search
import com.compose.watchlist.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(modifier: Modifier = Modifier, navController: NavController) {
    val movieVM: MovieViewModel = hiltViewModel()
    val movies by movieVM.searchResult.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(), contentPadding = PaddingValues(16.dp),
        state = listState
    ) {
        item {
            Column {
                SearchBar(modifier = Modifier.fillMaxWidth(), movieVm = movieVM)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (movies.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.file),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        } else {
            items(items = movies, key = { it.imdbID }) { item ->
                MovieItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    item = item,
                    navController = navController
                )
            }
        }
    }

}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    movieVm: MovieViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }

    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = searchText,
        label = { Text("Search Movie") },
        onValueChange = { searchText = it },
        shape = CircleShape,
        maxLines = 1,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,  // Removes underline when focused
            unfocusedIndicatorColor = Color.Transparent, // Removes underline when not focused
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )

    LaunchedEffect(searchText) {
        delay(500L)
        if (searchText.isNotEmpty()) {
            Log.d("TAG", "SearchBar: $searchText")
            movieVm.getMovie(searchText)
        }
    }

}


@Composable
fun MovieItem(modifier: Modifier = Modifier, item: Search, navController: NavController) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .height(130.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${item.imdbID}") },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp // Elevation for the card
        )

    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.Poster)
                    .crossfade(true)
                    .build(),
                /*                contentScale = ContentScale.FillWidth,*/
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RectangleShape)
                    .width(110.dp),

                onError = { i ->
                    Log.d("TAG", "HomeItems: error      ${i.result.throwable.localizedMessage}")

                }

            )
            Column(modifier = Modifier.padding(start = 20.dp)) {

                Text(item.title, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(5.dp))
                Text(item.year, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(5.dp))
                Text(item.Type, style = MaterialTheme.typography.labelSmall)

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {


    MovieItem(
        modifier = Modifier,
        item = Search("Name", "1994", "", "Movie", ""),
        navController = NavController(LocalContext.current)
    )


}

@Preview
@Composable
private fun ListPreview() {
    val movies = listOf(
        Search("Name", "1994", "1", "Movie", ""),
        Search("Name", "1994", "2", "Movie", ""),
        Search("Name", "1994", "3", "Movie", ""),
        Search("Name", "1994", "4", "Movie", ""),
        Search("Name", "1994", "5", "Movie", ""),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = movies, key = { it.imdbID }) { item ->
            MovieItem(Modifier, item, NavController(LocalContext.current))
        }
    }


}