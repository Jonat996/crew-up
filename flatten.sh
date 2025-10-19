#!/bin/bash

OUTPUT_FILE="repositorio_aplanado.txt"
MAX_FILE_SIZE=200000  # bytes → 200 KB

# Limpia el archivo de salida si ya existe
> "$OUTPUT_FILE"

# Extensiones consideradas como código fuente o archivos útiles
include_exts=(
    "kt" "kts" "java" "xml" "gradle" "properties"
    "md" "txt" "json" "yml" "yaml" "toml"
)

# Directorios a ignorar
ignore_dirs=(
    ".git"
    ".gradle"
    "build"
    ".idea"
    "out"
    "node_modules"
    "dist"
    ".vscode"
    ".kotlin"
    ".cache"
)

# Archivos específicos a ignorar
ignore_files=(
    "gradlew"
    "gradlew.bat"
    "local.properties"
    "gradle-wrapper.jar"
    "package-lock.json"
)

# Función para determinar si se debe ignorar un archivo o directorio
should_ignore() {
    local path="$1"

    # Ignorar directorios no deseados
    for dir in "${ignore_dirs[@]}"; do
        if [[ "$path" == *"/$dir/"* ]]; then
            return 0
        fi
    done

    # Ignorar archivos específicos
    local filename
    filename=$(basename "$path")
    for ignore_file in "${ignore_files[@]}"; do
        if [[ "$filename" == "$ignore_file" ]]; then
            return 0
        fi
    done

    # Incluir solo las extensiones válidas
    local ext="${path##*.}"
    local include=false
    for inc in "${include_exts[@]}"; do
        if [[ "$ext" == "$inc" ]]; then
            include=true
            break
        fi
    done

    if [[ "$include" == false ]]; then
        return 0
    fi

    # Ignorar archivos demasiado grandes
    if [[ -f "$path" && $(stat -c%s "$path") -gt $MAX_FILE_SIZE ]]; then
        return 0
    fi

    return 1
}

# Recorrer todos los archivos y concatenarlos en el archivo de salida
while IFS= read -r -d '' file; do
    if ! should_ignore "$file"; then
        echo "========== archivo: $file ==========" >> "$OUTPUT_FILE"
        cat "$file" >> "$OUTPUT_FILE"
        echo -e "\n" >> "$OUTPUT_FILE"
    fi
done < <(find . -type f -print0)

echo "✅ Se generó el archivo '$OUTPUT_FILE' con solo el código fuente relevante (Kotlin/Compose)."
