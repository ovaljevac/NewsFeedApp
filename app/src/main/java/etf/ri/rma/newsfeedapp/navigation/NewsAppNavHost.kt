package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.FilterViewModel
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen

@Composable
fun NewsFeedAppNavHost(){
    val navController = rememberNavController()
    val filterViewModel: FilterViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "newsFeed"
    ) {
        composable("newsFeed") {
            NewsFeedScreen(
                navController = navController,
                viewModel = filterViewModel
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
                }
            )
        }
        composable("details/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId")
            val news = NewsData.getAllNews().find {it.uuid == newsId}
            news?.let {
                NewsDetailsScreen(
                    news = it,
                    onBack = {
                        navController.popBackStack("newsFeed", inclusive = false)
                    },
                    onNewsSelected = { related ->
                        navController.navigate("details/${related.uuid}")
                    }
                )
            }
        }
    }
}