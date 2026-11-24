# Android - Diari de Viatges / Crònica de Ruta

Aplicació Android desenvolupada amb **Jetpack Compose** i arquitectura **MVVM** (Model-View-ViewModel) que permet registrar rutes o escapades mitjançant un formulari complet, transferir dades entre pantalles i gestionar la persistència tant a l'emmagatzematge intern (privat de l'app) com a l'emmagatzematge extern (exportable i compartible).

## Arquitectura MVVM

El projecte manté la separació de responsabilitats:

- **Model**: `RouteEntry` (data class immutable que representa una ruta) i `TravelDiaryUiState` (estat de la UI: camps del formulari, errors de validació, històric de rutes guardades i ruta seleccionada per a la postal).
- **ViewModel**: `TravelDiaryViewModel` (AndroidViewModel) gestiona l'estat reactiu amb `MutableStateFlow` privat i `StateFlow` públic (`.asStateFlow()`), actualitzant les propietats de forma immutable amb `.copy()` (via `updateState`). Implementa la validació del formulari i els mètodes de persistència/exportació.
- **View**: `FormScreen` i `PostalScreen` (Jetpack Compose) observen l'estat amb `collectAsStateWithLifecycle()` i deleguen les accions al ViewModel.

Navegació amb **Navigation Compose** (`NavHost`/`NavController`) entre `FormScreen` i `PostalScreen`, compartint l'objecte seleccionat (`selectedRoute`) a través del ViewModel.

## Captures de pantalla

- Pantalla 1: Formulari de nova ruta (FormScreen).
- Pantalla 2: Postal / Crònica de ruta (PostalScreen).

_(Afegir captures de pantalla aquí.)_

## Emmagatzematge intern vs extern

- **Emmagatzematge intern (`filesDir`)**: l'històric de rutes es guarda en un fitxer JSON (`diari_viatges.json`) dins del directori privat de l'aplicació (`context.filesDir`). És privat de l'app i no accessible per altres aplicacions ni per l'usuari directament. **Si es desinstal·la l'app, les dades s'eliminen** juntament amb el directori privat.
- **Emmagatzematge extern (Storage Access Framework)**: l'exportació de la crònica es fa amb `ActivityResultContracts.CreateDocument("text/plain")`, que obre el selector de carpetes del sistema i permet desar el fitxer `.md/.txt` a la carpeta Documents/Descàrregues escollida per l'usuari. Aquestes dades **persisteixen després de desinstal·lar l'app**, ja que es guarden en emmagatzematge compartit del dispositiu.

## Conceptes apresos

- **OutlinedTextField**: camps de text amb validació visual (`isError`, `supportingText`).
- **FilterChip**: selector d'opció única per a la dificultat.
- **DatePicker / DatePickerDialog (Material 3)**: selecció de data amb `rememberDatePickerState`.
- **Switch**: commutador per al viatge en solitari.
- **Navigation Compose**: `NavHost`, `NavController`, `composable`.
- **StateFlow + `.copy()`**: estat reactiu immutable.
- **CreateDocument (SAF)**: exportació de fitxers a emmagatzematge extern.
- **Intent.ACTION_SEND**: compartir text.
- **Serialització JSON**: Gson per persistir l'històric a `filesDir`.

## Compilació

Obrir el projecte a Android Studio i executar `assembleDebug`.

## Llicència

Projecte acadèmic.
