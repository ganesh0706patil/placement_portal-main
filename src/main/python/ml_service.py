from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional

# Import your logic classes from the other files
from resume_processor import ResumeProcessor
from ai_shortlister import AIShortlister

# 1. Initialize FastAPI app and your logic classes
app = FastAPI(
    title="AI Resume Shortlisting API",
    description="An API to extract skills from resumes and match them against job descriptions."
)
processor = ResumeProcessor()
shortlister = AIShortlister()

# 2. Define Pydantic models for data validation and API docs
class SkillRequest(BaseModel):
    resume_path: Optional[str] = None
    direct_input: bool = False
    skills: Optional[List[str]] = None
    keywords: Optional[List[str]] = None

class MatchRequest(BaseModel):
    resume_path: Optional[str] = None
    job_description: str
    required_skills: List[str] = []
    direct_test: bool = False
    resume_text: Optional[str] = None
    resume_skills: Optional[List[str]] = None

# 3. Create your API endpoints
@app.post("/api/extract-skills")
def extract_skills(data: SkillRequest):
    if data.direct_input:
        return {'skills': data.skills or [], 'keywords': data.keywords or []}

    if not data.resume_path:
        raise HTTPException(status_code=400, detail="Missing resume path")

    resume_text = processor.extract_text_from_pdf(data.resume_path)
    if resume_text is None:
        raise HTTPException(status_code=400, detail=f"Could not find or extract text from resume at: {data.resume_path}")
    
    return processor.extract_skills(resume_text)

@app.post("/api/match")
def match_resume(data: MatchRequest):
    if data.direct_test:
        result = shortlister.match_resume_to_job(
            resume_text=data.resume_text or "",
            job_description=data.job_description,
            required_skills=data.required_skills,
            resume_skills=data.resume_skills or []
        )
        return result

    if not data.resume_path:
        raise HTTPException(status_code=400, detail="Missing resume path for matching")

    resume_text = processor.extract_text_from_pdf(data.resume_path)
    if resume_text is None:
        raise HTTPException(status_code=400, detail=f"Could not find or extract text from resume at: {data.resume_path}")

    skills_data = processor.extract_skills(resume_text)
    
    result = shortlister.match_resume_to_job(
        resume_text=resume_text,
        job_description=data.job_description,
        required_skills=data.required_skills,
        resume_skills=skills_data.get('skills', [])
    )
    return result

# 4. (Optional) Add a root endpoint for a health check
@app.get("/")
def read_root():
    return {"status": "AI Shortlisting Service is running"}