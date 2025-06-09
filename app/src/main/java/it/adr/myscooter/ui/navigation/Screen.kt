package it.adr.myscooter.ui.navigation

sealed class Screen(val route: String) {
    object ScooterList : Screen("scooter_list_screen")
    object ScooterDetail : Screen("scooter_detail")
}