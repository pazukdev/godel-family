# GitHub Copilot Instructions

## Maven Commands

When running Maven commands in this project, always use the Maven Wrapper:

```bash
.\mvnw.cmd
```

**Examples:**
- Clean and compile: `.\mvnw.cmd clean compile`
- Run tests: `.\mvnw.cmd test`
- Run application: `.\mvnw.cmd spring-boot:run`
- Package: `.\mvnw.cmd clean package`

**Never use:**
- ❌ `mvn` (direct Maven command)
- ❌ `mvnw.cmd` (without the `.\` prefix in PowerShell)

**Always use:**
- ✅ `.\mvnw.cmd` (Maven Wrapper with proper path)

