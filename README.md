# HashDeDup

> Oprogramowanie do analizy wydajności jednokierunkowych funkcji skrótu (**MD5**, **SHA-1**, **SHA-2**) implementowanych w różnych językach programowania wraz z systemem deduplikacji danych w architekturze mikroserwisowej.

##  Zakres projektu

1. **Implementacja algorytmów kryptograficznych w:**

   * **Java**
   * **C#**
   * **C++** lub **JavaScript**

2. **Badanie wydajności:** Obiektywna weryfikacja przepustowości (MB/s), czasu obliczeniowego oraz całkowitego opóźnienia end-2-end (czas obliczeniowy + opóźnienia architektury) różnych implementacji algorytmów.

3. **Praktyczne zastosowanie (Deduplikator danych):** Moduł skanujący archiwa `.zip` przesłane przez użytkownika, generujący unikalne sygnatury plików i wykrywający zduplikowane zasoby na podstawie identycznych wartości funkcji skrótu.
## Przykładowa architektura systemu

```text
┌────────────────────────────────────────────────────────────────────────┐
│                              PRZEGLĄDARKA                              │
│                    Klient Webowy (React + Vite + TS)                   │
└───────────────────┬────────────────────────────────▲───────────────────┘
                    │ REST API (Zadania, Upload)     │ WebSocket (Wyniki)
                    ▼                                │
┌────────────────────────────────────────────────────────────────────────┐
│                             CENTRAL HUB                                │
│                             Spring Boot                                │
│  - Obsługa żądań REST i przesyłania archiwów ZIP                       │
│  - Rozpakowywanie archiwów do Shared Storage                           │
│  - Publikacja zadań obliczeniowych do Redis                            │
│  - Odbieranie wyników i wysyłanie aktualizacji do klienta              │
└────────────────────────────────────────────────────────────────────────┘
   │                     ▲                     │
   │ Publikacja zadań    │ Wynik zadań         │ Zapis plików
   ▼                     │                     ▼ 
┌────────────────────────────────┐   ┌──────────────────────────────────┐
│          REDIS BROKER          │   │          SHARED STORAGE          │
│                                │   │          (Docker Volume)         │
│                                │   │                                  │
│  - Zadania dla C++ \ JS        │   │  - Archiwa ZIP                   │
│  - Zadania dla C#              │   │  - Rozpakowane pliki             │
│  - Zadania dla Java            │   │  - Pliki dostępne dla workerów   │
│  - Odbieranie wyników          │   │                                  │
└────────────────────────────────┘   └──────────────────────────────────┘
        │ Zadania dla        ▲                     │  
        │ workerów           │  Hashe/metryki      │  Odczyt pliku z dysku
        ▼                    │                     ▼
┌────────────────────────────────────────────────────────┐
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐ │
│ │  C++ Worker  │ │  C# Worker   │ │   Java Worker    │ │  
│ │              │ │              │ │                  │ │
│ │  - Obliczanie│ │  - Obliczanie│ │  - Obliczanie    │ │
│ │    hashy     │ │    hashy     │ │    hashy         │ │
│ │  - Pomiary   │ │  - Pomiary   │ │  - Pomiary       │ │
│ │    wydajności│ │    wydajności│ │    wydajności    │ │
│ └──────────────┘ └──────────────┘ └──────────────────┘ │
└────────────────────────────────────────────────────────┘
```


## 🛠️ Tech Stack

> **Legenda:**  
> `TBD` (*To Be Determined*) – wersja lub biblioteka zostanie doprecyzowana na etapie implementacji.

### 1. Frontend (Klient Webowy)

| Technologia / Biblioteka | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **React** | `TBD` | Główna biblioteka interfejsu użytkownika |
| **TypeScript** | `TBD` | Statyczne typowanie kodu |
| **Vite** | `TBD` | Środowisko uruchomieniowe i bundler |
| **WebSocket / STOMP Client** | `TBD` | Odbiór wyników i postępu w czasie rzeczywistym |

---

### 2. Central Hub (Backend Główny)

| Technologia / Biblioteka | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **Java** | `TBD` | Środowisko uruchomieniowe (JDK) |
| **Spring Boot** | `TBD` | Główny framework aplikacyjny (REST, DI) |
| **Spring Data Redis** | `TBD` | Klient integracji z brokerem Redis |
| **Spring WebSocket** | `TBD` | Kanał dwukierunkowy z frontendem |

---

### 3. Java Worker

| Technologia / Biblioteka | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **Java** | `TBD` | Środowisko wykonawcze workera |
| **Jedis** | `TBD` | Klient subskrybujący kolejkę Redis |
| **JSON Parser** (Jackson / Gson) | `TBD` | Serializacja i deserializacja komunikatów |

---

### 4. C# Worker

| Technologia / Biblioteka | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **.NET SDK** | `TBD` | Środowisko uruchomieniowe (.NET) |
| **StackExchange.Redis** | `TBD` | Obsługa subskrypcji i publikacji Redis |
| **System.Text.Json** | `TBD` | Deserializacja zadań i budowanie payloadu wynikowego |

---

### 5. C++ Worker *(opcjonalnie JS Worker)*

| Technologia / Biblioteka | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **Standard C++** (np. C++20) | `TBD` | Język bazowy i standard kompilatora |
| **CMake** | `TBD` | System budowania projektu |
| **redis-plus-plus / hiredis** | `TBD` | Klient komunikacji z Redisem w C++ |
| **nlohmann/json** | `TBD` | Parsowanie zleceń i wyników JSON |
| *(Alternatywa) Node.js + ioredis* | `TBD` | *Wariant w przypadku wyboru JS zamiast C++* |

---

### 6. Infrastruktura & Broker

| Technologia / Narzędzie | Wersja | Rola w systemie |
| :--- | :--- | :--- |
| **Redis** | `TBD` | Broker komunikatów (Pub/Sub) |
| **Docker & Docker Compose** | `TBD` | Konteneryzacja i orkiestracja całego środowiska |
| **Shared Volume** | `TBD` | Współdzielony wolumen dyskowy dla rozpakowanych plików |
