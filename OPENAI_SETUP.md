# OpenAI API Key Setup Guide

## What is the OPENAI_API_KEY?

The `OPENAI_API_KEY` is an environment variable that stores your OpenAI API key. This key is required to authenticate with OpenAI's services and use GPT-4o-mini for the AI-powered employee query feature.

## How to Get an OpenAI API Key

1. **Sign up or log in** to OpenAI:
   - Go to https://platform.openai.com/
   - Create an account or log in if you already have one

2. **Navigate to API Keys**:
   - Click on your profile icon (top right)
   - Select "API keys" from the menu
   - Or go directly to: https://platform.openai.com/api-keys

3. **Create a new API key**:
   - Click "Create new secret key"
   - Give it a name (e.g., "Godel Family App")
   - Copy the key immediately (you won't be able to see it again!)
   - Store it securely

## How to Set the Environment Variable

### Option 1: Command Prompt (cmd.exe) - Temporary (Current Session Only)

```cmd
set OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxx
```

Then run your application in the **same** command prompt window:
```cmd
.\mvnw.cmd spring-boot:run
```

### Option 2: PowerShell - Temporary (Current Session Only)

```powershell
$env:OPENAI_API_KEY="sk-proj-xxxxxxxxxxxxxxxxxxxxx"
```

Then run your application in the **same** PowerShell window:
```powershell
.\mvnw.cmd spring-boot:run
```

### Option 3: Windows System Environment Variable - Permanent

1. **Open System Properties**:
   - Press `Win + X` and select "System"
   - Click "Advanced system settings"
   - Click "Environment Variables"

2. **Add User Variable**:
   - Under "User variables", click "New..."
   - Variable name: `OPENAI_API_KEY`
   - Variable value: `sk-proj-xxxxxxxxxxxxxxxxxxxxx` (your actual key)
   - Click OK

3. **Restart your IDE/Terminal**:
   - Close and reopen IntelliJ IDEA or your terminal
   - The environment variable will now be available

4. **Run the application**:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```

### Option 4: IntelliJ IDEA Run Configuration

1. **Open Run Configuration**:
   - Click "Run" → "Edit Configurations..."
   - Select your Spring Boot application

2. **Add Environment Variable**:
   - Find "Environment variables" field
   - Click the folder icon to expand
   - Click "+" to add a new variable
   - Name: `OPENAI_API_KEY`
   - Value: `sk-proj-xxxxxxxxxxxxxxxxxxxxx` (your actual key)
   - Click OK

3. **Run from IntelliJ**:
   - Just click the Run button, the environment variable will be automatically available

## How the Application Uses the API Key

In `application.properties`:
```properties
openai.api.key=${OPENAI_API_KEY}
```

This syntax `${OPENAI_API_KEY}` tells Spring Boot to:
1. Look for an environment variable named `OPENAI_API_KEY`
2. Use its value as the `openai.api.key` property
3. Inject it into the `EmployeeAIService` via `@Value("${openai.api.key}")`

## Verify It's Working

1. **Set the environment variable** using one of the methods above

2. **Run the application**:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```

3. **Check the logs**:
   - If the API key is missing, the AI service will still start but return an error message when you try to use it
   - Look for any errors related to OpenAI in the console

4. **Test the AI feature**:
   - Open http://localhost:8080
   - Type a question in the "Ask AI About Employees" box
   - Click "Ask AI"
   - If configured correctly, you'll get an intelligent response

## Troubleshooting

### "OpenAI API key is not configured"
- **Cause**: The environment variable is not set or not visible to the application
- **Solution**: 
  - Make sure you set the environment variable in the same terminal/IDE session where you run the app
  - For permanent setup, use Windows System Environment Variables (Option 3)
  - Restart your terminal/IDE after setting system environment variables

### "Error processing your question"
- **Cause**: Invalid API key or network issues
- **Solutions**:
  - Verify your API key is correct (it should start with `sk-`)
  - Check your internet connection
  - Verify your OpenAI account has credits/billing set up
  - Check if your API key has been revoked

### "429 Too Many Requests"
- **Cause**: Rate limit exceeded
- **Solution**: Wait a moment and try again, or upgrade your OpenAI plan

### Application starts but AI doesn't respond
- **Cause**: API key might be configured incorrectly
- **Solution**: 
  - Check the application logs for OpenAI-related errors
  - Verify the environment variable is accessible: `echo %OPENAI_API_KEY%` (cmd) or `echo $env:OPENAI_API_KEY` (PowerShell)

## Security Best Practices

⚠️ **IMPORTANT**: Never commit your API key to version control!

- ✅ Use environment variables (as we do)
- ✅ Add `.env` files to `.gitignore` if you use them
- ✅ Rotate your API keys periodically
- ✅ Use separate keys for development and production
- ❌ Never hardcode API keys in source code
- ❌ Never share your API keys publicly
- ❌ Never commit API keys to Git repositories

## Cost Considerations

- GPT-4o-mini is very cost-effective (~$0.15 per 1M input tokens)
- Each query costs a few cents at most
- Set up usage limits in your OpenAI dashboard to prevent unexpected charges
- Monitor your usage at: https://platform.openai.com/usage

## Example Usage

Once set up, you can ask questions like:

```
"How many developers do we have?"
"Who are the senior employees?"
"List all employees in the Java division"
"Which employees have the Lead title?"
"How many junior developers work in Python?"
```

The AI will analyze your employee data and provide intelligent, context-aware answers!

---

**Need help?** Check the main README.md for more information or troubleshooting tips.

