@echo off
echo Starting ML Service...
cd %~dp0\src\main\python
pip install -r requirements.txt

REM Create directory for NLTK data if it doesn't exist
if not exist nltk_data mkdir nltk_data

echo Starting Python ML service...
python resume_matcher.py