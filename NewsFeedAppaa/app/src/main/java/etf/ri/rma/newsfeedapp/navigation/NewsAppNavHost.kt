package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.model.FilterViewModel
import etf.ri.rma.newsfeedapp.model.NewsViewModel
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen

@Composable
fun NewsFeedAppNavHost(){
    val navController = rememberNavController()
    val filterViewModel: FilterViewModel = viewModel()
    val sharedNewsViewModel: NewsViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "newsFeed",
    ) {
        composable("newsFeed") {
            NewsFeedScreen(
                navController = navController,
                viewModel = filterViewModel,
                newsViewModel = sharedNewsViewModel
            )
        }
        composable("filter") {
            FilterScreen(
                initialCategory = filterViewModel.selectedCategory,
                initialDateRange = filterViewModel.selectedDateRange,
                initialUnwantedWords = filterViewModel.unwantedWords.toList(),
                onApplyFilters = { category, dateRange, unwantedWords ->
                    filterViewModel.selectedCategory = category
                    filterViewModel.selectedDateRange = dateRange
                    filterViewModel.unwantedWords.clear()
                    filterViewModel.unwantedWords.addAll(unwantedWords)
                    navController.popBackStack("newsFeed", inclusive = false)
                },
                onBack = {
                    navController.popBackStack("newsFeed", inclusive = false)
                },
                categories = filterViewModel.categories
            )
        }
        composable("details/{uuid}") { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""
            val newsItems = sharedNewsViewModel.newsItems
            var news = newsItems.find {it.uuid == uuid}
            if (news != null) {
                NewsDetailsScreen(
                    news = news,
                    onBack = { navController.popBackStack("newsFeed", inclusive = false) },
                    onNewsSelected = { selectedNews ->
                        navController.navigate("details/${selectedNews.uuid}")
                    },
                    viewModel = sharedNewsViewModel
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}