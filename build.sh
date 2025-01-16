#!/bin/bash

# Log file
LOG_FILE="build.log"

# Function to log messages
log() {
    echo "$(date +'%Y-%m-%d %H:%M:%S') - $1" | tee -a $LOG_FILE
}

# Check if Java is installed
if ! command -v java &> /dev/null; then
    log "Java is not installed. Please install Java and try again."
    exit 1
else
    log "Java is installed."
fi

# Check if javac is installed
if ! command -v javac &> /dev/null; then
    log "Java compiler (javac) is not installed. Please install JDK and try again."
    exit 1
else
    log "Java compiler (javac) is installed."
fi

# Compile the Java program
log "Compiling SSLDebug.java..."
if javac SSLDebug.java 2>> $LOG_FILE; then
    log "Compilation successful."
else
    log "Compilation failed. Check the log file for details."
    exit 1
fi

# Create the JAR file
log "Creating JAR file SSLDebug.jar..."
if jar cfm SSLDebug.jar MANIFEST.MF SSLDebug.class 2>> $LOG_FILE; then
    log "JAR file created successfully."
else
    log "Failed to create JAR file. Check the log file for details."
    exit 1
fi

# Run the JAR file
log "Running the JAR file..."
if java -jar SSLDebug.jar https://google.com 2>> $LOG_FILE; then
    log "JAR file executed successfully."
else
    log "Failed to execute JAR file. Check the log file for details."
    exit 1
fi
