-- ==============================================================================
-- إعداد جداول وقواعد بيانات عمران (Omran Academy) على Supabase
-- قم بنسخ ولصق هذا الملف بالكامل في SQL Editor في لوحة تحكم Supabase واضغط RUN
-- ==============================================================================

-- 1. جدول المعاهد (Institutes)
CREATE TABLE IF NOT EXISTS public.institutes (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    short_code TEXT NOT NULL UNIQUE,
    created_at BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 2. جدول المستخدمين / المعلمين والإداريين (Users Profile)
CREATE TABLE IF NOT EXISTS public.users (
    id TEXT PRIMARY KEY, -- يطابق uid من supabase auth
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'TEACHER', -- 'ADMIN' أو 'TEACHER'
    institute_id TEXT REFERENCES public.institutes(id) ON DELETE SET NULL,
    institute_name TEXT DEFAULT '',
    circle_name TEXT DEFAULT '',
    circle_gender TEXT DEFAULT 'MALE', -- 'MALE' (بنين) أو 'FEMALE' (بنات)
    photo_url TEXT DEFAULT '',
    fcm_token TEXT DEFAULT '',
    created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 3. جدول الطلاب (Students)
CREATE TABLE IF NOT EXISTS public.students (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    age INT DEFAULT 0,
    parent_phone TEXT DEFAULT '',
    teacher_id TEXT,
    teacher_name TEXT DEFAULT '',
    circle_name TEXT DEFAULT '',
    institute_id TEXT NOT NULL REFERENCES public.institutes(id) ON DELETE CASCADE,
    photo_url TEXT DEFAULT '',
    gender TEXT DEFAULT 'MALE', -- 'MALE' (بنين) أو 'FEMALE' (بنات)
    join_date BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 4. جدول الحضور والغياب (Attendance)
CREATE TABLE IF NOT EXISTS public.attendance (
    id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL REFERENCES public.students(id) ON DELETE CASCADE,
    student_name TEXT DEFAULT '',
    teacher_id TEXT,
    institute_id TEXT NOT NULL REFERENCES public.institutes(id) ON DELETE CASCADE,
    status TEXT NOT NULL DEFAULT 'PRESENT', -- 'PRESENT', 'ABSENT', 'LATE'
    date TEXT NOT NULL, -- e.g. "2026-08-26"
    created_at BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 5. جدول التسميع والحفظ الجديد (Memorization)
CREATE TABLE IF NOT EXISTS public.memorization (
    id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL REFERENCES public.students(id) ON DELETE CASCADE,
    student_name TEXT DEFAULT '',
    teacher_id TEXT,
    institute_id TEXT NOT NULL REFERENCES public.institutes(id) ON DELETE CASCADE,
    surah TEXT NOT NULL,
    from_verse INT DEFAULT 0,
    to_verse INT DEFAULT 0,
    amount TEXT DEFAULT '',
    rating TEXT DEFAULT '',
    category TEXT DEFAULT 'NEW',
    note TEXT DEFAULT '',
    date BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 6. جدول الاختبارات (Exams)
CREATE TABLE IF NOT EXISTS public.exams (
    id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL REFERENCES public.students(id) ON DELETE CASCADE,
    student_name TEXT DEFAULT '',
    teacher_id TEXT,
    institute_id TEXT NOT NULL REFERENCES public.institutes(id) ON DELETE CASCADE,
    surah TEXT NOT NULL,
    score INT DEFAULT 0,
    mistakes TEXT DEFAULT '',
    mastery TEXT DEFAULT '',
    note TEXT DEFAULT '',
    date BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- 7. جدول المراجعة (Revisions)
CREATE TABLE IF NOT EXISTS public.revisions (
    id TEXT PRIMARY KEY,
    student_id TEXT NOT NULL REFERENCES public.students(id) ON DELETE CASCADE,
    student_name TEXT DEFAULT '',
    teacher_id TEXT,
    institute_id TEXT NOT NULL REFERENCES public.institutes(id) ON DELETE CASCADE,
    content TEXT DEFAULT '',
    level TEXT DEFAULT '',
    rating TEXT DEFAULT '',
    category TEXT DEFAULT 'NEW',
    note TEXT DEFAULT '',
    date BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
);

-- ==============================================================================
-- الفهارس لتسريع الاستعلامات (Indexes)
-- ==============================================================================
CREATE INDEX IF NOT EXISTS idx_users_institute ON public.users(institute_id);
CREATE INDEX IF NOT EXISTS idx_students_institute ON public.students(institute_id);
CREATE INDEX IF NOT EXISTS idx_students_teacher ON public.students(teacher_id);
CREATE INDEX IF NOT EXISTS idx_attendance_inst_date ON public.attendance(institute_id, date);
CREATE INDEX IF NOT EXISTS idx_attendance_student ON public.attendance(student_id);
CREATE INDEX IF NOT EXISTS idx_memorization_inst_date ON public.memorization(institute_id, date);
CREATE INDEX IF NOT EXISTS idx_exams_inst_date ON public.exams(institute_id, date);
CREATE INDEX IF NOT EXISTS idx_revisions_inst_date ON public.revisions(institute_id, date);

-- ==============================================================================
-- تفعيل سياسات الأمان (Row Level Security - RLS)
-- ==============================================================================
ALTER TABLE public.institutes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.students ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.attendance ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.memorization ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.exams ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.revisions ENABLE ROW LEVEL SECURITY;

-- سياسات عامة للوصول (تسمح للمستخدمين المصادقين والمجهولين في التطبيق بالقراءة والكتابة)
CREATE POLICY "Allow all on institutes" ON public.institutes FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on users" ON public.users FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on students" ON public.students FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on attendance" ON public.attendance FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on memorization" ON public.memorization FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on exams" ON public.exams FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all on revisions" ON public.revisions FOR ALL USING (true) WITH CHECK (true);

-- ==============================================================================
-- إعداد حاوية التخزين لصور الطلاب (Storage Bucket)
-- ==============================================================================
INSERT INTO storage.buckets (id, name, public) 
VALUES ('students', 'students', true)
ON CONFLICT (id) DO NOTHING;

-- صلاحيات التخزين للقراءة والرفع
CREATE POLICY "Public Access students bucket" 
ON storage.objects FOR SELECT 
USING (bucket_id = 'students');

CREATE POLICY "Public Upload students bucket" 
ON storage.objects FOR INSERT 
WITH CHECK (bucket_id = 'students');

CREATE POLICY "Public Update students bucket" 
ON storage.objects FOR UPDATE 
USING (bucket_id = 'students');

-- ==============================================================================
-- ترقية الجداول القائمة (في حال كانت الجداول منشأة سابقاً)
-- ==============================================================================
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS circle_name TEXT DEFAULT '';
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS circle_gender TEXT DEFAULT 'MALE';
ALTER TABLE public.students ADD COLUMN IF NOT EXISTS gender TEXT DEFAULT 'MALE';
