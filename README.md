# 🌌 Resonantia v1.0.4 PRO

**Resonantia** es una aplicación nativa para Android desarrollada en **Kotlin** y **Jetpack Compose**. Su propósito es proporcionar una herramienta interactiva de exploración vibracional y espectro simbólico, relacionando frecuencias sonoras (Hz) con geometría sagrada/armónica, estados de conciencia y perfiles emocionales/mentales.

Creada con ❤️ por **JAYLIZ**.

**https://github.com/Jaypsmall/Resonantia/releases/download/android-app/Resonantia_v1.0.4.apk**

---

## 📸 Capturas de Pantalla

| Menú Principal & Navegación | Espectro Vibracional (Modo Oscuro) | Análisis de Zona & Transiciones | Modo Claro |
| :---: | :---: | :---: | :---: |
| *(Agrega captura del menú)* | *(Agrega captura del espectro)* | *(Agrega captura de detalles)* | *(Agrega captura en modo claro)* |

---

## ✨ Características Principales

- 🔮 **Interactive Symbolic Spectrum:** Exploración geométrica interactiva en tiempo real vinculada a la frecuencia seleccionada.
- 📡 **Visual Resonance Engine:** Motor de renderizado vectorial/canvas optimizado para simular armónicos, ondas e intensidad visual según el rango de Hz.
- 🏷️ **Zonas Simbólicas Integradas:**
  - Rangos asignados: **Zadquiel, Rafael, Miguel, Gabriel, Uriel, Jofiel, Chamuel**.
  - Detalle por zona: *Estado, Cualidad, Bloqueo y Práctica sugerida*.
- 📊 **Perfil Dinámico de Estados:** Indicadores visuales interactivos en tiempo real para métricas de *Claridad, Expresión, Protección e Integración*.
- 🧭 **Rutas de Transición y Frecuencia Objetivo:** Asistencia para sintonizar con estados deseados calculando la distancia (Hz) y dirección del cambio.
- 🔍 **Búsqueda Inteligente e Historial:** Búsqueda por términos de estado/emoción (ej. *miedo, claridad, culpa...*), selectores de ajuste preciso ($\pm 10\text{ Hz}$) e historial de frecuencias consultadas.
- 🌓 **Temas Dinámicos (Dark / Light Mode):** Alternancia instantánea de la interfaz para adaptarse a cualquier entorno de uso.
- 📜 **Historial de Sesiones:** Seguimiento y registro de trabajo con frecuencias.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100% Nativo)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Diseño declarativo moderno)
- **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel)
- **Visual Engine:** Custom Canvas / DrawScope para gráficos matemáticos en tiempo real.
- **Audio Generator:** Android AudioTrack API / Audio Engine para síntesis de frecuencias de sonido en tiempo real.
- **Asincronía & Flujos:** Kotlin Coroutines + StateFlow / SharedFlow.
- **Inyección de Dependencias:** Hilt / Koin.

---

## 🚀 Requisitos e Instalación

### Prerrequisitos
- **Android Studio:** Ladybug (o posterior) / Electric Eel+
- **JDK:** 17 o superior
- **Dispositivo de prueba / Emulador:** Android 7.0 (API Level 24) o superior

### Pasos para clonar y ejecutar

```bash
# 1. Clonar el repositorio
git clone [https://github.com/tu-usuario/resonantia-android.git](https://github.com/tu-usuario/resonantia-android.git)

# 2. Entrar al directorio del proyecto
cd resonantia-android

# 3. Compilar el proyecto desde la terminal (opcional)
./gradlew assembleDebug
