@echo off
cd /d "C:\Users\APP MOVILES\AndroidStudioProjects\CriptoApiED2"
git init
git config user.email "developer@criptoapied.com"
git config user.name "CriptoAPI Developer"
git add .
git commit -m "CriptoAPI Release"
git branch -M main
git remote remove origin 2>nul
git remote add origin https://github.com/EDiaz210/APP_CRIPTO.git
git push -u origin main
pause
