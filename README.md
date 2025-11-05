**CrewUp** es una aplicación Android desarrollada con Jetpack Compose para facilitar la creación y participación en planes sociales. Permite a los usuarios autenticarse, crear eventos (planes) con detalles como ubicación, fecha, imagen y chat grupal, unirse a planes existentes, gestionar perfiles y chatear en tiempo real. La app integra Firebase para autenticación, almacenamiento de datos y chat, y utiliza Google Maps para ubicaciones.

# Descripción General

## Funcionalidades Principales:

* Autenticación con Google o correo/contraseña (usando Firebase Auth).
* Creación de planes con título, descripción, imagen (subida a Firebase Storage), ubicación (integración con Google Places), fecha/hora y filtros (edad, género, tags).
* Unión y abandono de planes.
* Chat grupal en tiempo real por plan (usando Firestore).
* Gestión de perfil: Editar nombre, foto, ocupación, ciudad, etc.
* Visualización de planes propios y participantes.
* Navegación con Bottom Navigation Bar (Inicio, Perfil).

#
## Tecnologías Usadas:

* Jetpack Compose para UI declarativa.
* Firebase: Auth, Firestore (para datos de planes y chat), Storage (para imágenes).
* Google Places API para sugerencias de ubicaciones.
* Coroutines y Flows para manejo asíncrono y datos reactivos.
* Credential Manager para Google Sign-In.
* Coil para carga de imágenes.
* DataStore para preferencias locales.

## Arquitectura

La app sigue el patrón MVVM (Model-View-ViewModel) con un enfoque en Clean Architecture para separación de preocupaciones:

*Model*:
* Clases de datos como User, Plan, GroupMessage, PlanLocation.
* Repositorios (UserRepository, PlanRepository, ChatRepository) que encapsulan el acceso a datos (Firestore, Storage, Auth).
* Usan Coroutines para operaciones asíncronas y Flows para datos en tiempo real (e.g., mensajes de chat).

*ViewModel*:

AuthViewModel para manejar autenticación y estado de usuario.
Otros ViewModels implícitos en las pantallas (e.g., para creación de planes, edición de perfil).
Manejan lógica de negocio, exponen estados via StateFlow y manejan errores.

*View*:

UI construida enteramente con Jetpack Compose: Composable functions para pantallas como Login, Home, Profile, CreatePlan, Chat.
Navegación con Navigation Compose (AppNavigation).
Componentes reutilizables: Formularios, listas de planes, chat bubbles.

*Capas Adicionales*:

* Data Layer: Firebase como backend. Repositorios abstraen las operaciones CRUD y subidas de archivos.
* Domain Layer: Modelos de datos y lógica de negocio simple (e.g., validaciones en creación de planes).
* UI Layer: Compose para renderizado reactivo. Uso de temas (CrewUpTheme) y estados para manejo de UI (e.g., loading, error).
* Inyección de Dependencias: Manual (e.g., passing instances en constructors), sin Dagger/Hilt por simplicidad.

La arquitectura es escalable, con énfasis en flujos reactivos (e.g., actualizaciones en tiempo real del chat via SnapshotListener) y manejo de errores con Result<T>.

## Requisitos

* Android Studio (versión Giraffe o superior).
* JDK 11+.
* Dispositivo o emulador con Android API 26+ (minSdk 26).
* Cuenta de Firebase: Configura un proyecto en Firebase Console con Auth (Google, Email), Firestore y Storage habilitados.
* Google Maps API Key: Agrega tu clave en strings.xml (google_maps_key).
* Google Services JSON: Descarga google-services.json de Firebase y colócalo en app/.

## Construcción e Instalación
**Clonar el Repositorio**:
* git clone https://github.com/Jonat996/crew-up.git
* cd CrewUp

**Configurar Firebase**:
* Ir a Firebase Console, crear un proyecto.
* Habilitar Authentication (Google y Email/Password).
* Habilitar Firestore y Storage.
* Descargar google-services.json y colócalo en app/.
* Agregar a su Web Client ID en strings.xml para Google Sign-In.


**Abrir en Android Studio**:
* Abra el proyecto en Android Studio.
* Sync Gradle (File > Sync Project with Gradle Files).
* Resuelver dependencias (puede requerir conexión a internet para descargar libs).


**Construir y Ejecutar**:
* Seleccionar un dispositivo/emulador.
* Hagar click en Run (Shift + F10).
* Para build APK: gradle assembleDebug o usa Build > Build Bundle(s)/APK(s).

*Nota: Si hay errores con Google Sign-In, verifica el SHA-1 en Firebase (usar gradlew signingReport para obtenerlo).*


## Uso

***Inicio de Sesión***:

* Abrir la app.
* Iniciar sesión con Google o correo/contraseña.
* Si es nuevo usuario, se crea automáticamente en Firestore.


***Pantalla de Inicio (Home)***:

* Ver planes disponibles (filtrados por tags, ciudad).
* Buscar planes o filtra.
* Unirse a un plan o crear uno nuevo.


***Crear un Plan***:

* Navegar a "Crear Plan".
* Ingresar título, descripción, imagen (subir desde galería).
* Seleccionar ubicación (usando Places Autocomplete).
* Eligir fecha/hora, tags, filtros (edad, género).
* Publicar: Se guarda en Firestore y sube imagen a Storage.


***Unirse/Abandonar Plan***:

* En detalles de plan, presionar "Unirse" o "Abandonar".
* Solo el creador puede editar/eliminar.


***Chat Grupal***:
* En detalles de plan, acceda al chat.
* Envíar mensajes (en tiempo real via Firestore).
* Elimine sus propios mensajes.


***Perfil***:

* Editar datos (nombre, foto, etc.).
* Ver planes creados y participantes.
* Cierrar sesión.

*Notas de Uso*:
* La app maneja estados offline (Firestore cachea datos).
* Errores se muestran via toasts o dialogs.
* *Para testing*: Usar emulador con Google Play Services.

## Contribuciones

**Forkea el repo.**
* Crear una branch: git checkout -b feature/nueva-funcion.
* Commit: git commit -m "Añadir nueva función".
* Push: git push origin feature/nueva-funcion.
* Abrir un Pull Request.

**Contacto**
* Desarrollado por CrewUp. Para issues, abrir un ticket en el repo.
