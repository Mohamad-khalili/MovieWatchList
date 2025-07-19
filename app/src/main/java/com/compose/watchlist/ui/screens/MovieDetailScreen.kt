package com.compose.watchlist.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compose.watchlist.R
import com.compose.watchlist.domain.model.movieDetail.MovieDetailResponse
import com.compose.watchlist.domain.model.movieDetail.Rating
import com.compose.watchlist.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieId: String?,
    navController: NavHostController
) {
    val vm: MovieViewModel = hiltViewModel()
    val movieDetail by vm.movieDetail.collectAsStateWithLifecycle()
    if (movieId != null) {
        vm.getMovieDetail(movieId)
    }
    movieDetail?.let { MovieDetail(item = it, navController = navController, viewModel = vm) }
}


@Composable
fun MovieDetail(
    modifier: Modifier = Modifier, item: MovieDetailResponse,
    navController: NavHostController,
    viewModel: MovieViewModel
) {
    val context = LocalContext.current

    val isBookmark by viewModel.isBookmarked.collectAsState()

    LaunchedEffect(item.imdbID) {
        viewModel.checkBookmarkStatus(imdbId = item.imdbID)
    }



    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "back",
            Modifier.clickable { navController.popBackStack() }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                item.Title,
                style = MaterialTheme.typography.titleLarge,

                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )



            val painterRes =
                if (isBookmark) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            Icon(
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .clickable {
                        viewModel.toggleBookmark(item)
                    },
                painter = painterResource(painterRes),
                contentDescription = "save"
            )


        }
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
                "IMDB ${item.imdbRating}",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif,
                    shadow = Shadow(Color.Black, Offset.Zero, 1f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(item.Runtime, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(10.dp))
        Text(item.Year, style = MaterialTheme.typography.labelMedium)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(top = 20.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.Poster)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),

                onError = { i ->
                    Log.d("TAG", "HomeItems: error      ${i.result.throwable.localizedMessage}")

                }

            )
        }


        Spacer(modifier = Modifier.height(10.dp))
        Text(item.Genre, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(10.dp))
        Text(item.Plot, style = MaterialTheme.typography.labelMedium)

        Text(
            "Director: ${item.Director}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            "Writer: ${item.Writer}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 5.dp)
        )

    }
}



@Preview(showBackground = true)
@Composable
private fun MovieDetailScreenPreview() {
    val detail = MovieDetailResponse(
        "Amy Adams, Ben Affleck, Henry Cavill",
        "2 nominations",
        "",
        "United States",
        "",
        "Zack Snyder",
        "Action, Adventure, Sci-Fi",
        "English",
        "",
        "Batman is manipulated by Lex Luthor to fear Superman. Superman´s existence is meanwhile dividing the world and he is framed for murder during an international crisis. The heroes clash and force the neutral Wonder Woman to reemerge.",
        "https://m.media-amazon.com/images/M/MV5BOTRlNWQwM2ItNjkyZC00MGI3LThkYjktZmE5N2FlMzcyNTIyXkEyXkFqcGdeQXVyMTEyNzgwMDUw._V1_SX300.jpg",
        "",
        "R",
        listOf(Rating("Internet Movie Database", "7.2/10")),
        "23 Mar 2016",
        "",
        "182 min",
        "Batman v Superman: Dawn of Justice (Ultimate Edition)",
        "movie",
        "",
        "David S. Goyer, Chris Terrio",
        "2016",
        "",
        "7.2",
        "74,123"
    )
//    MovieDetail(item = detail, navController = NavHostController(context = LocalContext.current))
}