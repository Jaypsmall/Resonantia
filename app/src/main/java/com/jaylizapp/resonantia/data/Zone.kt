package com.jaylizapp.resonantia.data

import androidx.compose.ui.graphics.Color

data class Zone(
    val name: String,
    val min: Int,
    val max: Int,
    val state: String,
    val quality: String,
    val block: String,
    val practice: String,
    val description: String,
    val exit: String,
    val direction: String,
    val color: Color,
    val secondaryColor: Color
)

val zones = listOf(
    Zone(
        name = "Zadquiel",
        min = 100,
        max = 200,
        state = "Duelo · culpa · tristeza",
        quality = "Transmutación",
        block = "Aferramiento al pasado",
        practice = "Aceptar lo ocurrido sin convertirlo en identidad",
        description = "Zona simbólicamente asociada con transformación emocional, liberación y cierre de ciclos.",
        exit = "Reconoce la emoción sin convertirla en identidad. Convierte la carga en comprensión.",
        direction = "→ Rafael · Recuperación",
        color = Color(0xFFE91E63),
        secondaryColor = Color.White
    ),
    Zone(
        name = "Rafael",
        min = 200,
        max = 350,
        state = "Recuperación · equilibrio",
        quality = "Sanación",
        block = "Negación del desgaste",
        practice = "Descanso, cuidado y reconstrucción",
        description = "Zona simbólicamente vinculada con recuperación, equilibrio y restauración.",
        exit = "Deja de luchar contra todo al mismo tiempo. Recupera recursos y reconstruye.",
        direction = "→ Miguel · Protección",
        color = Color(0xFFB2DFDB),
        secondaryColor = Color(0xFF004D40)
    ),
    Zone(
        name = "Miguel",
        min = 350,
        max = 500,
        state = "Determinación · protección",
        quality = "Voluntad",
        block = "Miedo · indecisión",
        practice = "Establecer límites y actuar",
        description = "Zona simbólicamente asociada con voluntad, protección, decisión y límites.",
        exit = "Identifica qué estás evitando y toma una decisión concreta.",
        direction = "→ Gabriel · Expresión",
        color = Color(0xFFE3F2FD),
        secondaryColor = Color(0xFF1565C0)
    ),
    Zone(
        name = "Gabriel",
        min = 500,
        max = 650,
        state = "Expresión consciente",
        quality = "Comunicación",
        block = "Silenciar lo necesario",
        practice = "Comunicar con claridad",
        description = "Zona simbólicamente asociada con expresión, comunicación y creatividad.",
        exit = "Transforma la emoción acumulada en comunicación consciente.",
        direction = "→ Uriel · Claridad",
        color = Color(0xFFF3E5F5),
        secondaryColor = Color(0xFF6A1B9A)
    ),
    Zone(
        name = "Uriel",
        min = 650,
        max = 750,
        state = "Comprensión · claridad",
        quality = "Discernimiento",
        block = "Confusión",
        practice = "Observar antes de reaccionar",
        description = "Zona simbólicamente relacionada con comprensión, discernimiento y observación.",
        exit = "Separa hechos de interpretaciones y decide desde aquello que realmente sabes.",
        direction = "→ Jofiel · Conciencia",
        color = Color(0xFFC8E6C9),
        secondaryColor = Color(0xFF2E7D32)
    ),
    Zone(
        name = "Jofiel",
        min = 750,
        max = 850,
        state = "Conciencia · perspectiva",
        quality = "Comprensión",
        block = "Pensamiento repetitivo",
        practice = "Cambiar la perspectiva",
        description = "Zona simbólicamente asociada con percepción, aprendizaje y reconocimiento de patrones.",
        exit = "Cambia el ángulo desde el que estás observando el problema.",
        direction = "→ Chamuel · Integración",
        color = Color(0xFFF8BBD0),
        secondaryColor = Color(0xFF880E4F)
    ),
    Zone(
        name = "Chamuel",
        min = 850,
        max = 1000,
        state = "Integración · conexión",
        quality = "Aceptación",
        block = "Separación",
        practice = "Integrar las partes en conflicto",
        description = "Zona simbólicamente asociada con integración, conexión y aceptación.",
        exit = "Intenta comprender las partes que están en conflicto en lugar de eliminarlas.",
        direction = "Zona superior del modelo · Integración",
        color = Color(0xFFFFD6E7),
        secondaryColor = Color(0xFF880E4F)
    )
)
