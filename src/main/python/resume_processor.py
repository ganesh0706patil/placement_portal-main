import os
import json
import re
import nltk
import PyPDF2
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from flask import Flask, request, jsonify

# Set NLTK data path to a directory within the project
nltk_data_dir = os.path.join(os.path.dirname(__file__), 'nltk_data')
os.makedirs(nltk_data_dir, exist_ok=True)
nltk.data.path.append(nltk_data_dir)

# Only download if not already present
def download_if_needed(package):
    try:
        nltk.data.find(f'tokenizers/{package}')
    except LookupError:
        nltk.download(package, download_dir=nltk_data_dir)

download_if_needed('punkt')
download_if_needed('stopwords')

class ResumeProcessor:
    def __init__(self):
        self.stop_words = set(stopwords.words('english'))
        
        # Define skill categories and keywords
        self.skill_patterns = {
            'database': ['sql', 'mysql', 'postgresql', 'mongodb', 'nosql', 'oracle', 'db2', 'redis', 'cassandra', 'dynamodb'],
            'cloud': ['aws', 'azure', 'gcp', 'cloud computing', 'serverless', 'lambda', 'ec2', 's3', 'cloud native'],
            'devops': ['docker', 'kubernetes', 'jenkins', 'ci/cd', 'terraform', 'ansible', 'github actions', 'gitlab ci', 'prometheus', 'grafana'],
            'mobile': ['android', 'ios', 'swift', 'kotlin', 'react native', 'flutter', 'xamarin', 'mobile development'],
            'java': ['java', 'j2ee', 'spring', 'hibernate', 'maven', 'gradle', 'junit', 'tomcat', 'jboss', 'websphere'],
            'python': ['python', 'django', 'flask', 'pandas', 'numpy', 'scipy', 'tensorflow', 'pytorch', 'scikit-learn', 'matplotlib'],
            'javascript': ['javascript', 'js', 'react', 'angular', 'vue', 'node', 'express', 'typescript', 'next.js', 'nuxt.js'],
            'web': ['html', 'css', 'sass', 'less', 'bootstrap', 'tailwind', 'responsive design', 'web development'],
            'data': ['data science', 'machine learning', 'deep learning', 'data mining', 'data analysis', 'big data', 'hadoop', 'spark'],
        }
    
    def preprocess_text(self, text):
        # Convert to lowercase
        text = text.lower()
        # Remove special characters and numbers
        text = re.sub(r'[^a-zA-Z\s]', '', text)
        # Tokenize
        tokens = word_tokenize(text)
        # Remove stopwords
        filtered_tokens = [w for w in tokens if w not in self.stop_words]
        return ' '.join(filtered_tokens)
    
    def extract_text_from_pdf(self, pdf_path):
        text = ""
        try:
            if not os.path.exists(pdf_path):
                print(f"PDF file not found: {pdf_path}")
                return text
                
            with open(pdf_path, 'rb') as file:
                reader = PyPDF2.PdfReader(file)
                for page in reader.pages:
                    text += page.extract_text()
        except Exception as e:
            print(f"Error extracting text from PDF: {e}")
        return text
    
    def extract_skills(self, text):
        text_lower = text.lower()
        skills = []
        keywords = []
        
        # Check for skills based on categories
        for skill, variations in self.skill_patterns.items():
            for variation in variations:
                # Use word boundary to avoid partial matches
                pattern = r'\b' + re.escape(variation) + r'\b'
                if re.search(pattern, text_lower):
                    if skill not in skills:
                        skills.append(skill)
                    if variation not in keywords:
                        keywords.append(variation)
                    break
        
        return {
            'skills': skills,
            'keywords': keywords
        }

# Flask API to expose the service
app = Flask(__name__)
processor = ResumeProcessor()

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

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)