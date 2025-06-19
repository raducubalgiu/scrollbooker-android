package com.example.scrollbooker.core.nav.navigators

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.EmploymentRequestViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms.EmploymentAcceptTermsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms.EmploymentAcceptTermsViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.assignJob.EmploymentAssignJobScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.assignJob.EmploymentAssignJobViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.selectEmployee.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.selectEmployee.EmploymentSelectEmployeeViewModel
import com.example.scrollbooker.shared.employmentRequest.data.mappers.toDto

fun NavGraphBuilder.employmentRequestNavGraph(
    navController: NavHostController
) {
    composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
        val employmentSelectEmployeeViewModel: EmploymentSelectEmployeeViewModel = hiltViewModel()

        EmploymentSelectEmployeeScreen(
            globalViewModel = employmentRequestViewModel,
            localViewModel = employmentSelectEmployeeViewModel,
            onNext = { navController.navigate(MainRoute.EmploymentAssignJob.route) },
            onBack = { navController.popBackStack() }
        )
    }

    composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
        val employmentAssignJobViewModel: EmploymentAssignJobViewModel = hiltViewModel()

        EmploymentAssignJobScreen(
            globalViewModel = employmentRequestViewModel,
            localViewModel = employmentAssignJobViewModel,
            onNext = { navController.navigate(MainRoute.EmploymentAcceptTerms.route) },
            onBack = { navController.popBackStack() }
        )
    }

    composable(MainRoute.EmploymentAcceptTerms.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
        }
        val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
        val employmentAcceptTermsViewModel: EmploymentAcceptTermsViewModel = hiltViewModel()
        val employmentRequestsViewModel: EmploymentRequestsViewModel = hiltViewModel()

        EmploymentAcceptTermsScreen(
            employmentRequestsViewModel = employmentRequestsViewModel,
            globalViewModel = employmentRequestViewModel,
            localViewModel = employmentAcceptTermsViewModel,
            onSubmit = {
                val request = employmentRequestViewModel.buildEmploymentRequest()
                employmentRequestsViewModel.createEmploymentRequest(request.toDto())

                navController.popBackStack(MainRoute.EmploymentsRequests.route, inclusive = false)
            },
            onBack = { navController.popBackStack() }
        )
    }
}