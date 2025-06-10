package com.example.scrollbooker.core.nav.navigators

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list.EmploymentRequestsViewModel
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.acceptTerms.EmploymentAcceptTermsScreen
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.acceptTerms.EmploymentAcceptTermsViewModel
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.assignJob.EmploymentAssignJobScreen
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.assignJob.EmploymentAssignJobViewModel
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.selectEmployee.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.selectEmployee.EmploymentSelectEmployeeViewModel

fun NavGraphBuilder.employmentRequestNavGraph(
    navController: NavHostController
) {
    composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestsViewModel: EmploymentRequestsViewModel = hiltViewModel(parentEntry)
        val employmentSelectEmployeeViewModel: EmploymentSelectEmployeeViewModel = hiltViewModel()

        EmploymentSelectEmployeeScreen(
            globalViewModel = employmentRequestsViewModel,
            localViewModel = employmentSelectEmployeeViewModel,
            onNext = { navController.navigate(MainRoute.EmploymentAssignJob.route) },
            onBack = { navController.popBackStack() }
        )
    }

    composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestsViewModel: EmploymentRequestsViewModel = hiltViewModel(parentEntry)
        val employmentAssignJobViewModel: EmploymentAssignJobViewModel = hiltViewModel()

        EmploymentAssignJobScreen(
            globalViewModel = employmentRequestsViewModel,
            localViewModel = employmentAssignJobViewModel,
            onNext = { navController.navigate(MainRoute.EmploymentAcceptTerms.route) },
            onBack = { navController.popBackStack() }
        )
    }

    composable(MainRoute.EmploymentAcceptTerms.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestsViewModel: EmploymentRequestsViewModel = hiltViewModel(parentEntry)
        val employmentAcceptTermsViewModel: EmploymentAcceptTermsViewModel = hiltViewModel()

        EmploymentAcceptTermsScreen(
            globalViewModel = employmentRequestsViewModel,
            localViewModel = employmentAcceptTermsViewModel,
            onSubmit = {

            },
            onBack = { navController.popBackStack() }
        )
    }
}