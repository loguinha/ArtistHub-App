package com.example.artisthub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.artisthub.data.allStores
import com.example.artisthub.model.Store
import com.example.artisthub.ui.theme.LightGray

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Filtrar lojas baseado na pesquisa
    val filteredStores = if (searchQuery.isNotEmpty()) {
        allStores.filter { store ->
            store.name.contains(searchQuery, ignoreCase = true) ||
                    store.category.contains(searchQuery, ignoreCase = true) ||
                    store.description.contains(searchQuery, ignoreCase = true)
        }
    } else {
        emptyList()
    }

    // Função para fechar o teclado
    fun closeKeyboard() {
        keyboardController?.hide()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "ARTIST HUB",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Que artesão você gostaria de ver hoje?",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            // Barra de pesquisa corrigida
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .focusRequester(focusRequester),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ícone de Pesquisa",
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                                closeKeyboard()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpar pesquisa",
                                tint = Color.Black
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        text = "Pesquisar artista...",
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        closeKeyboard()
                    }
                )
            )
        }

        // Conteúdo principal
        if (searchQuery.isNotEmpty()) {
            // Mostrar resultados da pesquisa
            SearchResultsSection(
                filteredStores = filteredStores,
                searchQuery = searchQuery,
                navController = navController,
                onResultClick = {
                    closeKeyboard()
                }
            )
        } else {
            // Conteúdo normal quando não há pesquisa
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightGray)
                    .padding(vertical = 24.dp)
            ) {
                // Seção de Categorias
                CategorySection()
                Spacer(modifier = Modifier.height(32.dp))

                // Seção de Destaques
                FeaturedSection(navController)
                Spacer(modifier = Modifier.height(32.dp))

                // Seção de Artistas em Alta
                PopularArtistsSection(navController)
                Spacer(modifier = Modifier.height(32.dp))

                // Seção de Novos Artistas
                NewArtistsSection(navController)
            }
        }

        // Rodapé
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = if (searchQuery.isNotEmpty() && filteredStores.isEmpty()) "Nenhum resultado encontrado" else "Fim dos resultados",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SearchResultsSection(
    filteredStores: List<Store>,
    searchQuery: String,
    navController: NavController,
    onResultClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)
            .padding(vertical = 24.dp)
    ) {
        Text(
            text = "Resultados para \"$searchQuery\"",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredStores.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum artista encontrado com o termo \"$searchQuery\"",
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                filteredStores.forEach { store ->
                    StoreCard(
                        store = store,
                        onClick = {
                            onResultClick()
                            navController.navigate("store/${store.name}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection() {
    val categories = listOf("Cerâmica", "Pintura", "Têxtil", "Joalheria", "Madeira", "Couro")

    Column(modifier = Modifier.padding(start = 24.dp)) {
        Text(
            text = "Descubra Artistas por Categoria",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { categoryName ->
                CategoryItem(name = categoryName)
            }
        }
    }
}

@Composable
fun CategoryItem(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(2),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun FeaturedSection(navController: NavController) {
    val stores = listOf(
        Store("Luarce", "Velas Aromáticas", "Velas artesanais com essências naturais.", "10"),
        Store("Ateliê Mãos de Fada", "Crochê Moderno", "Peças de crochê contemporâneas.", "9"),
        Store("Cerâmica & Cia", "Peças de Cerâmica", "Utensílios e peças decorativas.", "8.5")
    )

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Destaques da Semana",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            stores.forEach { store ->
                StoreCard(store = store, onClick = {
                    navController.navigate("store/${store.name}")
                })
            }
        }
    }
}

@Composable
fun StoreCard(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = store.name.take(2),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = store.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = store.rating,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Avaliação",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "• ${store.category}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = store.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun PopularArtistsSection(navController: NavController) {
    val popularStores = listOf(
        Store("Arte em Vidro", "Vitrais Artesanais", "Trabalhos únicos em vidro colorido.", "9.2"),
        Store("Bordados Manuais", "Bordado Tradicional", "Peças com técnicas tradicionais.", "8.8")
    )

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Artistas em Alta",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            popularStores.forEach { store ->
                StoreCard(store = store, onClick = {
                    navController.navigate("store/${store.name}")
                })
            }
        }
    }
}

@Composable
fun NewArtistsSection(navController: NavController) {
    val newStores = listOf(
        Store("Tecidos Naturais", "Tecelagem", "Tecidos com fibras naturais.", "8.0"),
        Store("Cerâmica Rústica", "Cerâmica Artesanal", "Peças com acabamento rústico.", "8.3")
    )

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Novos Artistas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            newStores.forEach { store ->
                StoreCard(store = store, onClick = {
                    navController.navigate("store/${store.name}")
                })
            }
        }
    }
}