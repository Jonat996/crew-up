package com.crewup.myapplication.ui.components.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.home.SearchHeaderBar
import com.crewup.myapplication.ui.navigation.Routes
import com.crewup.myapplication.viewmodel.PlansListViewModel

@Composable
fun HomeSection(
    navController: NavController,
    viewModel: PlansListViewModel = viewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val planes by viewModel.plans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchHeaderBar(
            query = query,
            onQueryChange = viewModel::updateSearchQuery,
            onSearchClick = { /* cerrar teclado */ },
            onFilterClick = { viewModel.updateSelectedTags(listOf("Bebidas", "Customes")) },
            onProfileClick = { navController.navigate(Routes.Profile.route) }
        )

      Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(planes) { plan ->
                        Text(
                            text = plan.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
