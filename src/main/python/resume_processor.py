import os
import re
import nltk
import pypdf
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

# Set NLTK data path to a directory within the project
nltk_data_dir = os.path.join(os.path.dirname(__file__), 'nltk_data')
os.makedirs(nltk_data_dir, exist_ok=True)
nltk.data.path.append(nltk_data_dir)

# Only download if not already present
def download_if_needed(package, subfolder):
    try:
        nltk.data.find(f'{subfolder}/{package}')
    except LookupError:
        nltk.download(package, download_dir=nltk_data_dir)

download_if_needed('punkt', 'tokenizers')
download_if_needed('stopwords', 'corpora')

class ResumeProcessor:
    def __init__(self):
        self.stop_words = set(stopwords.words('english'))
        # Your skill_patterns dictionary remains the same
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

    def extract_text_from_pdf(self, pdf_path):
        text = ""
        try:
            if not os.path.exists(pdf_path):
                print(f"PDF file not found: {pdf_path}")
                return None
            with open(pdf_path, 'rb') as file:
                reader = pypdf.PdfReader(file)
                for page in reader.pages:
                    text += page.extract_text()
        except Exception as e:
            print(f"Error extracting text from PDF: {e}")
            return None
        return text
    
    def extract_skills(self, text):
        text_lower = text.lower()
        skills = []
        keywords = []
        for skill, variations in self.skill_patterns.items():
            for variation in variations:
                pattern = r'\b' + re.escape(variation) + r'\b'
                if re.search(pattern, text_lower):
                    if skill not in skills:
                        skills.append(skill)
                    if variation not in keywords:
                        keywords.append(variation)
        return {'skills': skills, 'keywords': keywords}

# --- NO FLASK APP CODE HERE ---