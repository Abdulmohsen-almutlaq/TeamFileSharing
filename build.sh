#!/bin/sh
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
