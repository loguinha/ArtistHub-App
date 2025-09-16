package com.example.artisthub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artisthub.ui.screens.HomeScreen
import com.example.artisthub.ui.screens.StoreScreen

// Define as rotas para as diferentes telas do app para evitar erros de digitação
object Routes {
    const val HOME = "home"
    // A rota da loja precisa de um argumento {storeId} para saber qual loja mostrar
    const val STORE = "store/{storeId}"

    // Função auxiliar para construir a rota para uma loja específica
    fun storeRoute(storeId: String) = "store/$storeId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // O NavHost é o container que vai trocar as telas (Composables)
    NavHost(navController = navController, startDestination = Routes.HOME) {
        // Define a tela para a rota "home"
        composable(Routes.HOME) {
            HomeScreen(
                onStoreClick = { storeId ->
                    // Navega para a tela da loja quando um item for clicado
                    navController.navigate(Routes.storeRoute(storeId))
                }
            )
        }

        // Define a tela para a rota "store/{storeId}"
        composable(Routes.STORE) { backStackEntry ->
            // Extrai o argumento "storeId" da rota
            val storeId = backStackEntry.arguments?.getString("storeId")
            requireNotNull(storeId) { "Store ID não pode ser nulo!" }
            StoreScreen(
                storeId = storeId,
                onBackClick = {
                    // Função para voltar para a tela anterior
                    navController.popBackStack()
                }
            )
        }
    }
}