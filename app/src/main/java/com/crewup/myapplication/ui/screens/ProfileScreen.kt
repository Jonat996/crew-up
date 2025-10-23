package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.R
import androidx.compose.ui.res.stringResource
import com.crewup.myapplication.ui.components.BottomNavBar
import com.crewup.myapplication.ui.components.ProfileOption
import com.crewup.myapplication.ui.components.ProfileOptionList
import com.crewup.myapplication.ui.components.SectionTitle
import com.crewup.myapplication.ui.components.header.HeaderUserInfo
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.ui.navigation.Routes

@Composable
fun ProfileScreen(
    navController: NavController,
    onSignOut: () -> Unit
){
    val editProfile = stringResource(R.string.edit_profile)
    val security = stringResource(R.string.security)
    val notifications = stringResource(R.string.notifications)
    val privacy = stringResource(R.string.privacy)
    val reportProblem = stringResource(R.string.report_problem)
    val yourCreatedPlans = stringResource(R.string.your_created_plans)
    val logout = stringResource(R.string.logout)
    val helpSupport = stringResource(R.string.help_support)
    val termsAndPolicies = stringResource(R.string.terms_policies)
    val accountSection = stringResource(R.string.account_section)
    val actionsSection = stringResource(R.string.actions_section)
    val supportSection = stringResource(R.string.support_section)

    fun getAccountOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_profile, editProfile, onClick = { navController.navigate(Routes.EditProfile.route) }),
            ProfileOption(R.drawable.icon_privacy, security, onClick = { navController.navigate(Routes.Security.route) }),
            ProfileOption(R.drawable.icon_notification, notifications, onClick = { navController.navigate(Routes.Notifications.route) }),
            ProfileOption(R.drawable.icon_lock, privacy, onClick = { navController.navigate(Routes.Privacy.route) })
        )
    }

    fun getActionsOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_report, reportProblem, onClick = {}),
            ProfileOption(R.drawable.icon_plans, yourCreatedPlans, onClick = { navController.navigate(Routes.PlanLocation.route)}),
            ProfileOption(R.drawable.icon_logout, logout, onClick = onSignOut),
        )
    }

    fun getSupportOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_help, helpSupport, onClick = {}),
            ProfileOption(R.drawable.icon_term, termsAndPolicies, onClick = {}),
        )
    }

    MainLayout(
        header = { HeaderUserInfo() },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SectionTitle(accountSection)
                ProfileOptionList(options = getAccountOptions())
                Spacer(Modifier.height(24.dp))
                SectionTitle(actionsSection)
                ProfileOptionList(options = getActionsOptions())
                Spacer(Modifier.height(24.dp))
                SectionTitle(supportSection)
                ProfileOptionList(options = getSupportOptions())
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, name = "Full Profile Screen")
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        ProfileScreen(navController = navController, onSignOut = {})
    }
}
