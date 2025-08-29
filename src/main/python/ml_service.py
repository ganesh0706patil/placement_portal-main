import os
import json
import threading
from flask import Flask, request, jsonify
from resume_processor import processor
from ai_shortlister import shortlister

app = Flask(__name__)

@app.route('/api/extract-skills', methods=['POST'])
def extract_skills():
    data = request.json
    resume_path = data.get('resume_path')
    
    # Direct skills input for testing (without resume)
    if data.get('direct_input'):
        skills_data = {
            'skills': data.get('skills', []),
            'keywords': data.get('keywords', [])
        }
        return jsonify(skills_data)
    
    if not resume_path:
        return jsonify({'error': 'Missing resume path'}), 400
    
    # Extract text from resume PDF
    resume_text = processor.extract_text_from_pdf(resume_path)
    if not resume_text:
        return jsonify({'error': 'Could not extract text from resume'}), 400
    
    # Extract skills
    skills_data = processor.extract_skills(resume_text)
    
    return jsonify(skills_data)

@app.route('/api/match', methods=['POST'])
def match_resume():
    data = request.json
    resume_path = data.get('resume_path')
    job_description = data.get('job_description')
    required_skills = data.get('required_skills', [])
    
    # For testing without resume upload
    direct_test = data.get('direct_test', False)
    
    if direct_test:
        # Use provided skills directly for testing
        resume_text = data.get('resume_text', '')
        resume_skills = data.get('resume_skills', [])
        
        # Match resume to job using provided data
        result = shortlister.match_resume_to_job(
            resume_text, 
            job_description, 
            required_skills, 
            resume_skills
        )
        
        return jsonify(result)
    
    # Regular flow with resume file
    if not resume_path or not job_description:
        return jsonify({'error': 'Missing required parameters'}), 400
    
    # Extract text from resume PDF
    resume_text = processor.extract_text_from_pdf(resume_path)
    if not resume_text:
        return jsonify({'error': 'Could not extract text from resume'}), 400
    
    # Extract skills from resume
    skills_data = processor.extract_skills(resume_text)
    resume_skills = skills_data.get('skills', [])
    
    # Match resume to job
    result = shortlister.match_resume_to_job(
        resume_text, 
        job_description, 
        required_skills, 
        resume_skills
    )
    
    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)