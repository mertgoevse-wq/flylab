# FlyLab MCP Architecture

This document tracks Model Context Protocol (MCP) servers integrated with the FlyLab project. 

## Configured Servers

### Figma
- **Purpose**: Design inspection, component structure, design tokens, layout validation, accessibility review, design-to-code consistency.
- **Transport**: stdio (`npx -y @figma/mcp-server`)
- **Authentication**: Requires Figma Personal Access Token set in environment (`FIGMA_ACCESS_TOKEN`).
- **Required permissions**: Read-only access to Figma files.
- **Security considerations**: Ensure `FIGMA_ACCESS_TOKEN` is never committed.
- **Fallback**: Claude Code can assist with code/UI logic without Figma using heuristics.
- **Verification method**: `claude mcp list` or `/mcp`.

### Supabase
- **Purpose**: Experiment metadata, reproducibility records, remote datasets, remote session synchronization.
- **Transport**: stdio (`npx -y @supabase/supabase-mcp`)
- **Authentication**: Requires Supabase access token/service role key (`SUPABASE_ACCESS_TOKEN`).
- **Required permissions**: Read-write on specific experimental datasets.
- **Security considerations**: Use read-only during discovery migrations. Never commit secrets.
- **Fallback**: Local persistence models in MongoDB or SQLite.
- **Verification method**: `claude mcp list`.

### Playwright
- **Purpose**: Browser automation, testing UI and web representations if applicable.
- **Transport**: stdio (`npx -y @microsoft/playwright-mcp`)
- **Required permissions**: Local browser execution.
- **Security considerations**: Only proxy local resources.
- **Fallback**: Manual testing.
- **Verification method**: `claude mcp list`.

*(Higgsfield is left manually configurable depending on CLI or MCP integration readiness)*
