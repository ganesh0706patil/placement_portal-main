import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

class AIShortlister:
    def __init__(self):
        self.vectorizer = TfidfVectorizer()

    def match_resume_to_job(self, resume_text, job_description, required_skills, resume_skills=None):
        if resume_skills is None:
            resume_skills = []

        if resume_text and job_description:
            try:
                tfidf_matrix = self.vectorizer.fit_transform([resume_text, job_description])
                cosine_sim = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:2])[0][0]
            except ValueError: # Happens if job_description or resume_text is empty after preprocessing
                cosine_sim = 0.0
        else:
            cosine_sim = 0.0

        if required_skills:
            matched_skills = [skill for skill in resume_skills if skill in required_skills]
            skill_match_percentage = len(matched_skills) / len(required_skills) if len(required_skills) > 0 else 0.0
        else:
            matched_skills = []
            skill_match_percentage = 0.0

        final_score = (cosine_sim * 0.7) + (skill_match_percentage * 0.3)
        final_score = min(final_score * 100, 100.0)

        return {
            'score': round(final_score, 2),
            'content_similarity': round(cosine_sim, 4),
            'skill_match_percentage': round(skill_match_percentage, 4),
            'matched_skills': matched_skills
        }