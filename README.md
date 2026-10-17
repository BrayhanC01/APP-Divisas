# App rick y morty

## carpetas usadas

<img width="437" height="645" alt="image" src="https://github.com/user-attachments/assets/66aff49b-ca6b-4819-a1b1-e9de4fbc4b60" />
Esta es la estructura limpia para una aplicacion movil
<br><br>

### 1. Capa de Datos (`data`)

Maneja la lógica de negocio, la obtención de datos y su transformación.

<br>

1.1: `model` (`Character.kt`): Define las entidades o estructuras de datos que maneja la app.
Es una clase Kotlin (generalmente un `data class`) que representa a un personaje con sus propiedades 
(por ejemplo: id, name, status, image).
<img width="366" height="469" alt="image" src="https://github.com/user-attachments/assets/777df7c4-58ce-49f2-a0e2-23370f4509df" />

<br>

1.2: `remote` (`RickAndMortyApi.kt`): Se encarga de la comunicación directa con el servidor/API externo.
Aquí se definen las peticiones HTTP (normalmente usando librerías como `Retrofit`)
para consumir los datos de la API de Rick and Morty.
<img width="556" height="667" alt="image" src="https://github.com/user-attachments/assets/2e9d158e-2dae-4c06-b802-4e5be99eced7" />

<br>

1.3: `repository` (`CharacterRepository.kt`): Actúa como intermediario y única fuente de verdad para el resto de la app.
Pide los datos a `remote` (o a una base de datos local si la hubiera), procesa la respuesta y se la entrega a la interfaz.
Aisla a la `UI` de los detalles de implementación de la red.
<img width="649" height="434" alt="image" src="https://github.com/user-attachments/assets/c323953a-1ac1-4e93-bfdc-ec0103bceba7" />


<br><br>
### 2. Capa de Interfaz (`ui`)
Maneja todo lo que el usuario ve y escucha en la pantalla.
<br>

`ui`: Paquete raíz de la interfaz visual.
<br>

2.1: `screens` (`CharacterScreen.kt`): Contiene los componentes de vista (usualmente composables de Jetpack Compose)
que arman la pantalla completa donde se muestran los personajes y se gestiona la interacción del usuario.
<br>

2.2: `theme` (`Color.kt`, `Theme.kt`,` Type.kt`): Configura la identidad visual global de la app.
<br>

- `Color.kt`: Define la paleta de colores.

- `Type.kt`: Define los estilos tipográficos (fuentes, tamaños, pesos).

- `Theme.kt`: Une los colores, tipografías y formas para aplicar un tema unificado (modo claro/oscuro) a todos los componentes visuales.



<br><br>

## 🔌 Módulo de Hardware Nativo (Multisensor)

Pantalla que integra el hardware del dispositivo Android usando Kotlin y Jetpack Compose. Se accede desde la opción **Hardware** de la barra de navegación inferior del Home.

<p align="center">
  <img src="docs/screenshots/home.png" width="220" alt="Home con botón Hardware">
  <img src="docs/screenshots/hardware-general.png" width="220" alt="Pantalla Multisensor">
</p>

### Funcionalidades

| Componente | Descripción | Tecnología |
|---|---|---|
| 📷 Cámara | Vista previa en vivo y captura de fotos guardadas en almacenamiento interno | CameraX (`Preview` + `ImageCapture`) |
| 🎙️ Micrófono | Grabación de audio de 5 segundos (AAC/MPEG-4) y reproducción | `MediaRecorder` + `MediaPlayer` |
| 📡 Bluetooth | Escaneo de dispositivos BLE cercanos durante 10 s, mostrando nombre, MAC y señal (dBm) | `BluetoothLeScanner` |
| 🔴 Infrarrojo | Envío de una señal IR (patrón NEC a 38 kHz) en dispositivos con emisor | `ConsumerIrManager` |
| 🎯 Nivel de burbuja | Lectura en tiempo real de los ejes X, Y, Z con indicador visual de nivelación | `SensorManager` (acelerómetro) |
| 🔦 Linterna SOS | Señal SOS con linterna y vibración sincronizadas | `CameraManager.setTorchMode` + `Vibrator` |

### 📸 Capturas
<img width="1907" height="1013" alt="Captura de pantalla 2026-10-17 120802" src="https://github.com/user-attachments/assets/50129be6-7826-400c-81a3-6564c7813042" />
Vista previa en vivo con CameraX y captura de foto guardada en el almacenamiento interno de la app.

#### Micrófono
<img width="1907" height="1013" alt="Captura de pantalla 2026-10-17 120802" src="https://github.com/user-attachments/assets/76f4565f-1d9d-4807-8179-c6c16886d1b7" />
Graba 5 segundos de audio y permite reproducirlo desde la misma pantalla.

#### Bluetooth
<img width="1917" height="1017" alt="Captura de pantalla 2026-10-17 121054" src="https://github.com/user-attachments/assets/9c370d2d-2af1-4ef5-a3af-87b04790347d" />
Escaneo BLE de 10 segundos con listado de dispositivos ordenados por intensidad de señal.

#### Infrarrojo
<img src="docs/screenshots/infrarrojo.png" width="220" alt="Infrarrojo">

Detecta si el dispositivo tiene emisor IR y envía una señal NEC a 38 kHz.

#### Nivel de burbuja
<img src="docs/screenshots/nivel-burbuja.png" width="220" alt="Nivel de burbuja">

Usa el acelerómetro para mostrar la inclinación del dispositivo en tiempo real.

#### Linterna SOS
<img src="docs/screenshots/linterna-sos.png" width="220" alt="Linterna SOS">

Emite el patrón SOS (· · · — — — · · ·) con la linterna y la vibración sincronizadas.

### Arquitectura

```
app/src/main/java/com/danidev/apprickmorty/
├── hardware/
│   ├── Permisos.kt          # Permisos según versión de Android
│   ├── CamaraHelper.kt      # CameraX
│   ├── AudioHelper.kt       # MediaRecorder + MediaPlayer
│   ├── BleHelper.kt         # Escaneo BLE
│   ├── IrHelper.kt          # Emisor infrarrojo
│   └── SensoresHelper.kt    # Acelerómetro, linterna y vibración
└── ui/screens/
    └── HardwareScreen.kt    # Pantalla Compose que coordina todo
```

### Buenas prácticas implementadas

- Permisos solicitados **en tiempo de ejecución** y solo cuando se usa cada función. Si se deniegan de forma permanente, se ofrece abrir los Ajustes.
- Permisos Bluetooth adaptados a Android 12+ (`BLUETOOTH_SCAN` / `BLUETOOTH_CONNECT`) y a versiones anteriores.
- Hardware declarado como opcional (`uses-feature required="false"`) y verificado en código, por lo que la app no falla en dispositivos sin emisor IR, linterna o vibrador.
- **Liberación de recursos** en `onStop()`: se detienen la grabación, la reproducción, el escaneo BLE, el sensor, la linterna y la vibración.
- El acelerómetro solo se registra mientras la pantalla es visible, para ahorrar batería.

### Permisos requeridos

`CAMERA`, `RECORD_AUDIO`, `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `TRANSMIT_IR`, `VIBRATE`

### Requisitos y pruebas

- Se recomienda probar en un **dispositivo físico**: el emulador no soporta infrarrojo y tiene soporte limitado de Bluetooth, linterna y vibración.
- El infrarrojo solo funciona en teléfonos que incluyan emisor IR.
