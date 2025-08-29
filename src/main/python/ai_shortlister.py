import os
import json
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from flask import Flask, request, jsonify

class AIShortlister:
    def __init__(self):
        self.vectorizer = TfidfVectorizer()
    
    def match_resume_to_job(self, resume_text, job_description, required_skills, resume_skills=None):
        # If resume_skills is provided directly (for testing without resume)
        if resume_skills is None:
            # This would be handled by resume_processor.py in a real scenario
            # Here we're just providing a fallback
            resume_skills = []
        
        # Create TF-IDF vectors for content similarity
        if resume_text and job_description:
            tfidf_matrix = self.vectorizer.fit_transform([resume_text, job_description])
            cosine_sim = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:2])[0][0]
        else:
            # If testing without resume text, use a default value
            cosine_sim = 0.5
        
        # Calculate skill match percentage
        if required_skills and len(required_skills) > 0:
            matched_skills = [skill for skill in resume_skills if skill in required_skills]
            skill_match_percentage = len(matched_skills) / len(required_skills) if len(required_skills) > 0 else 0
        else:
            matched_skills = []
            skill_match_percentage = 0
        
        # Calculate final score (70% content similarity, 30% skill match)
        final_score = (cosine_sim * 0.7) + (skill_match_percentage * 0.3)
        final_score = min(final_score * 100, 100)  # Scale to 0-100 and cap at 100
        
        return {
            'score': final_score,
            'content_similarity': cosine_sim,
            'skill_match_percentage': skill_match_percentage,
            'matched_skills': matched_skills
        }

# Flask API
app = Flask(__name__)
shortlister = AIShortlister()

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
    
    # In a real scenario, we would extract text from the resume here
    # For simplicity, we'll assume this is handled by the Java service
    # which calls resume_processor.py first
    
    # This is a placeholder - in production, you would actually read the file
    resume_text = "Sample resume text for demonstration"
    
    # Match resume to job
    result = shortlister.match_resume_to_job(resume_text, job_description, required_skills)
    
    return jsonify(result)

# Run the Flask app
if __name__ == '__main__':
    # Use a different port than resume_processor.py
    app.run(host='0.0.0.0', port=5001)