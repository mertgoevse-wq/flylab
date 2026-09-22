-- FlyLab Cloud Save Schema
-- Deployed to Supabase to support Session syncs

CREATE TABLE IF NOT EXISTS public.sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    experiment_id TEXT NOT NULL,
    version INT NOT NULL DEFAULT 1,
    saved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    history_jsonB JSONB NOT NULL,
    user_id UUID REFERENCES auth.users(id) ON DELETE SET NULL
);

-- RLS policies
ALTER TABLE public.sessions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can only read their own sessions." 
ON public.sessions FOR SELECT 
TO authenticated 
USING (auth.uid() = user_id);

CREATE POLICY "Users can insert their own sessions." 
ON public.sessions FOR INSERT 
TO authenticated 
WITH CHECK (auth.uid() = user_id);
