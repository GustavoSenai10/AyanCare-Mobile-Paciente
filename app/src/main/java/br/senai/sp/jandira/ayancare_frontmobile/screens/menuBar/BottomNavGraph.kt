package br.senai.sp.jandira.ayancare_frontmobile.screens.menuBar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.senai.sp.jandira.ayancare_frontmobile.screens.estoque.verEstoque.screen.EstoqueScreen
import br.senai.sp.jandira.ayancare_frontmobile.screens.home.screen.HomeScreen
import br.senai.sp.jandira.ayancare_frontmobile.screens.perfil.screen.ProfileScreen
import br.senai.sp.jandira.ayancare_frontmobile.viewModel.user.CreateAccountView
import br.senai.sp.jandira.ayancare_frontmobile.viewModel.user.PacienteView

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    navRotasController: NavController
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
    ){
        composable(route = BottomBarScreen.Stock.route){
            EstoqueScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Cadendary.route){
            //SettingsScreen(navController = navController)
            //AddContactScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Home.route){
            HomeScreen(navController, navRotasController)
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(navController = navController, navRotasController)
        }
    }
}
